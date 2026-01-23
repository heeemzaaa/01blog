import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterOutlet } from '@angular/router';
import { NavBar } from '../../components/nav-bar/nav-bar';
import { Notifications } from '../../components/notifications/notifications';

@Component({
  selector: 'app-home-layout',
  imports: [RouterOutlet, MatIconModule, NavBar],
  templateUrl: './homePage.html',
  styleUrl: './homePage.css',
})
export class HomeLayout {

}