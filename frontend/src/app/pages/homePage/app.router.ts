import { Routes } from '@angular/router';
import { HomeComponent } from '../../components/home/home.component';
import { AddPost } from '../../components/add-post/add-post.component';
import { Notifications } from '../../components/notifications/notifications.component';
import { Profile } from '../../components/profile/profile.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'add-post', component: AddPost },
  { path: 'notifications', component: Notifications },
  { path: 'profile/:id', component: Profile },
  // { path: 'post/:id', component: Notifications },
  // { path: 'notifications', component: Notifications },
  // { path: 'notifications', component: Notifications },

//   { path: 'blogs/:id', component: BlogDetail },

//   { path: 'create_blog', component: BlogFormComponent },
//   { path: 'profile/:id', component: ProfileComponent },
];