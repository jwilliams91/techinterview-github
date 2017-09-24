import { Component, OnInit, EventEmitter } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { GHService } from '../GHService';
import { SimpleEvent } from '../models/SimpleEvent';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'Event Feed';
  selectedEvent: SimpleEvent;
  events: SimpleEvent[];
  eventCols = [
    {field: 'username', header: 'Username'},
    {field: 'eventType', header: 'Event Type'},
    {field: 'repo', header: 'Repository'}
  ];
  loggedIn = false;
  eventsByUser = 'public';
  loading = true;

  constructor(private ghService: GHService, private activatedRoute: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.loggedIn = sessionStorage.getItem('currentUser') ? true : false;
    this.ghService.logonSuccess$.subscribe((message) => {
      this.loggedIn = true;
      this.updateTable();
      this.ghService.getUserAvatar(sessionStorage.getItem('currentUser'));
    });
    if(this.loggedIn) {
      this.updateTable();
      this.ghService.getUserAvatar(sessionStorage.getItem('currentUser'));
    }
      
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      if (params['code']) {
        this.ghService.authorizeUser(params['code']).then(() => this.router.navigate(['/home']));
      }
    });
  }

  updateTable() {
    this.loading = true;
    if(this.eventsByUser == 'public') {
      this.ghService.getEvents().then((events) => {
        this.events = events;
        this.ghService.getEventDetails(this.events[0].username, this.events[0].eventId).then((details) => console.log(details));
        this.loading = false;
      });
    }
    else {
      this.ghService.getEventsByCurrent().then((events) => {
        this.events = events;
        this.loading = false;
      });
    }
  }

  logSelected() {
    console.log(this.selectedEvent);
  }

  navToRepo(repo: string) {
    window.open(`https://github.com/${repo}`);
  }
}
