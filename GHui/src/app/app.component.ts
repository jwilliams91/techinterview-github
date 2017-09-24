import { Component, OnInit } from '@angular/core';
import { GHService } from './GHService';
import { Constants } from './Constants';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  loggedIn: boolean;
  username: string;
  avatarUrl: string;
  loaded: boolean;

  constructor(private ghService: GHService, private router: Router) { }

  ngOnInit() {
    if(sessionStorage.getItem('currentUser')) {
      this.username = sessionStorage.getItem('currentUser');
      this.ghService.getUserAvatar(this.username).then((url) => {
        this.avatarUrl = url;
        this.loaded = true;
      });
      this.loggedIn = true;
    }
    this.ghService.logonSuccess$.subscribe((message) => {
      this.username = sessionStorage.getItem('currentUser');
      this.ghService.getUserAvatar(this.username).then((url) => {
        this.avatarUrl = url;
        this.loaded = true;
      });
      this.loggedIn = true;
    });
  }

  login(): void {
    window.location.href = `https://github.com/login/oauth/authorize?client_id=${Constants.CLIENT_ID}&scope=${Constants.DEFAULT_SCOPES}`;
  }

  logout(): void {
    sessionStorage.removeItem('currentUser');
    this.loggedIn = false;
    this.router.navigate(['/']);
  }
}
