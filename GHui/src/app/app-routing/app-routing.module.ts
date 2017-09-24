import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from '../app.component';
import { HomeComponent } from '../home/home.component';
import { EventDetailsComponent } from '../event-details/event-details.component';

const routes: Routes = [
  { path: '', component: AppComponent},
  { path: 'home', component: HomeComponent},
  { path: 'details/:username/:eventId', component: EventDetailsComponent}
  ];

@NgModule ({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}