import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from '../app.component';
import { HomeComponent } from '../home/home.component';
import { EventDetailsComponent } from '../event-details/event-details.component';

const routes: Routes = [
  { path: '', component: AppComponent, pathMatch: 'full'},
  { path: 'home', component: HomeComponent, pathMatch: 'full'},
  { path: 'details/:username/:eventId', component: EventDetailsComponent, pathMatch: 'full'}
  ];

@NgModule ({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule {}