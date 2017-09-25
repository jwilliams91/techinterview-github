import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { GHService } from '../GHService';
import { SimpleEvent } from '../models/SimpleEvent';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {

  selectedEvent: SimpleEvent;
  loading: boolean = true;


  constructor(private ghService: GHService, private activatedRoute: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    //Uses the route parameters to query the api for the event to display
    this.activatedRoute.params.subscribe((params: Params) => {
        this.ghService.getEventDetails(params['username'], params['eventId']).then((event) => {
          this.selectedEvent = event;
          this.loading = false;
        });
      });    
  }

  get events() {
    return this.ghService.eventData;
  }

}
