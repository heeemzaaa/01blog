package com.blog01.backend.post.service;

import org.springframework.stereotype.Service;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.auth.repository.UserRepository;
import com.blog01.backend.auth.response.UserResponse;
import com.blog01.backend.common.response.ResponseData;
import com.blog01.backend.notification.model.Notification.NotificationType;
import com.blog01.backend.notification.service.NotificationService;
import com.blog01.backend.post.dto.PostRequest;
import com.blog01.backend.post.model.Post;
import com.blog01.backend.post.repository.*;
import com.blog01.backend.post.response.PostResponse;
import com.blog01.backend.postmedia.service.PostMediaService;
import com.blog01.backend.subscribes.model.Subscribe;
import com.blog01.backend.subscribes.repository.SubscribesRepository;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

        private final UserRepository userRepository;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;
        private final LikeRepository likeRepository;
        private final NotificationService notificationService;
        private final SubscribesRepository subscribesRepository;
        private final PostMediaService postMediaService;

        public ResponseData<List<PostResponse>> getFeedPosts(String email) {

                User currentUser = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // users I follow
                List<Subscribe> subscriptions = subscribesRepository.findBySubscriber(currentUser);

                List<User> followedUsers = subscriptions.stream()
                                .map(Subscribe::getUser)
                                .toList();

                List<Post> posts = postRepository
                                .findByUserInOrderByCreatedAtDesc(followedUsers);

                List<PostResponse> response = posts.stream()
                                .map(post -> mapToPostResponse(post, currentUser))
                                .toList();

                return ResponseData.success("Feed posts fetched successfully", response);
        }

        public ResponseData<PostResponse> createPost(
                        String email,
                        PostRequest postToCreate,
                        List<MultipartFile> medias) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("There is no user with this email !"));

                Post post = Post.builder()
                                .user(user)
                                .title(postToCreate.getTitle())
                                .content(postToCreate.getContent())
                                .build();

                Post saved = postRepository.save(post);

                // ✅ MEDIA HANDLING
                postMediaService.handlePostMedias(saved, medias);

                // Notifications
                List<User> followers = subscribesRepository.findSubscribersByUser(user);
                for (User follower : followers) {
                        notificationService.sendNotification(
                                        follower,
                                        user,
                                        NotificationType.NEW_POST,
                                        saved.getId());
                }

                return ResponseData.success(
                                "Post created successfully !",
                                mapToPostResponse(saved, user));
        }

        public ResponseData<List<PostResponse>> getProfilePosts(UUID userId, String email) {

                User viewer = userRepository.findByEmail(email).orElseThrow();
                User profileUser = userRepository.findById(userId).orElseThrow();

                List<Post> posts;

                if (viewer.getId().equals(profileUser.getId())) {
                        posts = postRepository.findByUserOrderByCreatedAtDesc(profileUser);
                } else {
                        posts = postRepository.findByUserOrderByCreatedAtDesc(profileUser);
                        // later: privacy rules
                }

                return ResponseData.success(
                                "Profile posts fetched",
                                mapToPostResponse(posts, viewer));
        }

        public ResponseData<PostResponse> updatePost(
                        String email,
                        UUID id,
                        PostRequest postToUpdate,
                        List<MultipartFile> medias) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("There is no user with this email !"));

                Post post = postRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Cannot find this post !"));

                if (!post.getUser().getId().equals(user.getId())) {
                        return ResponseData.error("You can't modify this post because it is not yours !");
                }

                post.setTitle(postToUpdate.getTitle());
                post.setContent(postToUpdate.getContent());

                Post saved = postRepository.save(post);

                postMediaService.handlePostMedias(saved, medias);

                return ResponseData.success(
                                "Post updated successfully !",
                                mapToPostResponse(saved, user));
        }

        public ResponseData<String> deletePost(String email, UUID postId) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));

                if (!post.getUser().getId().equals(user.getId())) {
                        return ResponseData.error("You are not authorized to delete this post");
                }

                postMediaService.deleteAllByPost(post);

                postRepository.delete(post);

                return ResponseData.success("Post deleted successfully", null);
        }

        private PostResponse mapToPostResponse(Post p, User currentUser) {

                long likesCount = likeRepository.countByPost(p);
                long commentsCount = commentRepository.countByPost(p);
                boolean isLiked = likeRepository.existsByUserAndPost(currentUser, p);

                return PostResponse.builder()
                                .id(p.getId())
                                .title(p.getTitle())
                                .content(p.getContent())
                                .medias(postMediaService.buildResponses(p))
                                .createdAt(p.getCreatedAt())
                                .likesCount(likesCount)
                                .commentsCount(commentsCount)
                                .isLiked(isLiked)
                                .user(UserResponse.builder()
                                                .id(p.getUser().getId())
                                                .firstName(p.getUser().getFirstName())
                                                .lastName(p.getUser().getLastName())
                                                .username(p.getUser().getUsername())
                                                .profileImage(p.getUser().getProfileImage())
                                                .build())
                                .build();
        }

        private List<PostResponse> mapToPostResponse(List<Post> posts, User currentUser) {
                return posts.stream()
                                .map(p -> mapToPostResponse(p, currentUser))
                                .toList();
        }

}
