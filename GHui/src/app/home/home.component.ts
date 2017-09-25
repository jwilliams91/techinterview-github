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
  eventCols = [
    {field: 'username', header: 'Username'},
    {field: 'eventType', header: 'Event Type'},
    {field: 'repo', header: 'Repository'}
  ];
  eventsByUser = 'public';

  loggedIn: boolean = false;
  loading: boolean = true;

  constructor(private ghService: GHService, private activatedRoute: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.loggedIn = sessionStorage.getItem('currentUser') ? true : false;

    //On Successful Logon event, populate the Event feed
    this.ghService.logonSuccess$.subscribe((message) => {
      this.loggedIn = true;
      this.updateTable();
    });

    if(this.loggedIn) {
      this.updateTable(this.events);
    }
    
    //This component's route is also the callbackUrl for Github authentication
    //If the code parameter is present, complete the authentication process
    this.activatedRoute.queryParams.subscribe((params: Params) => {
      if (params['code']) {
        this.ghService.authorizeUser(params['code']).then(() => this.router.navigate(['/home']));
      }
    });
  }

  updateTable(events?:SimpleEvent[]) {
    if(!events) {
      this.loading = true;
      this.ghService.getEvents(this.eventsByUser).then(() => this.loading = false);
    }
    else {
      this.loading = false;
    }
  }

  //Get event data from shared service
  get events(): SimpleEvent[] {
    return this.ghService.eventData;
  }

  //Table select event method, navigates to details page
  navToDetails() {
    if(event) {
      this.router.navigate(['/details', this.selectedEvent.username, this.selectedEvent.eventId]);
    }
  }

  navToRepo(repo: string) {
    window.open(`https://github.com/${repo}`);
  }
}
