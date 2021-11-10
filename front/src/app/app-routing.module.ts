import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { EditTicketComponent } from './components/edit-ticket/edit-ticket.component';
import { AirlinePageComponent  } from './components/airline-page/airline-page.component';
import {UsersBookingsComponent} from './components/users-bookings/users-bookings.component';

const routes: Routes = [
  // http://localhost:4200/
  { path: '', component: LoginComponent },
  //http://localhost:4200/home
  { path: 'home', component: HomeComponent },
  //http://localhost:4200/editTicket/id
  { path: 'editTicket/:id', component: EditTicketComponent },
  //http://localhost:4200/airline/id
  { path: 'airline/:id', component: AirlinePageComponent },
  //http://localhost:4200/bookings
  { path: 'bookings', component: UsersBookingsComponent }
 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
