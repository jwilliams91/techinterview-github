import { Component, OnInit, EventEmitter } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { GHService } from '../GHService';
import { User } from '../models/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'GitHub Api Test';
  loggedIn = false;

  constructor(private ghService: GHService,
              private activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.loggedIn = sessionStorage.getItem('currentUser') ? true : false;
    this.ghService.logonSuccess$.subscribe((message) => this.loggedIn = true);
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      if (params['code']) {
        this.ghService.authorizeUser(params['code']);
      }
    });
  }

}
