import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterOutlet } from '@angular/router';
import { NavBar } from '../../components/nav-bar/nav-bar.component';
import { Notifications } from '../../components/notifications/notifications.component';

@Component({
  selector: 'app-home-layout',
  imports: [RouterOutlet, MatIconModule, NavBar],
  templateUrl: './homePage.html',
  styleUrl: './homePage.css',
})
export class HomeLayout {

}