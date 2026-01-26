import { Routes } from '@angular/router';
import { HomeComponent } from '../../components/home/home';
import { AddPost } from '../../components/add-post/add-post';
import { Notifications } from '../../components/notifications/notifications';
import { Profile } from '../../components/profile/profile';


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