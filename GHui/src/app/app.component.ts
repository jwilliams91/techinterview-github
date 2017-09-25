import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GHService } from './GHService';
import { User } from './models/User';
import { Constants } from './Constants';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  loggedIn: boolean;
  user: User;
  avatarUrl: string;

  constructor(private ghService: GHService, private router: Router) { }

  ngOnInit() {
    //If Session has user, get the avatar
    if(sessionStorage.getItem('currentUser')) {
      this.user = JSON.parse(sessionStorage.getItem('currentUser'));
      this.ghService.getUserAvatar(this.user.username).then((url) => {
        this.avatarUrl = url;
        this.loggedIn = true;
      });
    }

    //On Successful Logon event, get the avatar
    this.ghService.logonSuccess$.subscribe((message) => {
      this.user = JSON.parse(sessionStorage.getItem('currentUser'));
      this.ghService.getUserAvatar(this.user.username).then((url) => {
        this.avatarUrl = url;
        this.loggedIn = true;
      });
    });
  }

  //Github OAuth2 Authentication Link
  login(): void {
    window.location.href = `https://github.com/login/oauth/authorize?client_id=${Constants.CLIENT_ID}&scope=${Constants.DEFAULT_SCOPES}`;
  }

  logout(): void {
    sessionStorage.removeItem('currentUser');
    this.loggedIn = false;
    this.router.navigate(['/']);
  }
}
