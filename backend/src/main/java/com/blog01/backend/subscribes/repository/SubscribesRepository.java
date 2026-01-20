package com.blog01.backend.subscribes.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog01.backend.auth.model.User;
import com.blog01.backend.subscribes.model.Subscribe;

import java.util.*;

@Repository
public interface SubscribesRepository extends JpaRepository<Subscribe, UUID> {
    boolean existsBySubscriberAndUser(User subscriber, User user);

    Optional<Subscribe> findBySubscriberAndUser(User subscriber, User user);

    List<Subscribe> findByUser(User user);

    List<Subscribe> findBySubscriber(User subscriber);

    @Query("SELECT s.subscriber FROM Subscribe s WHERE s.user = :user")
    List<User> findSubscribersByUser(@Param("user") User user);

    long countBySubscriber(User subscriber);

    long countByUser(User user);
}
