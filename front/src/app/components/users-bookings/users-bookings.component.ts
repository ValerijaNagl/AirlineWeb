import { Component, OnInit } from '@angular/core';
import {User} from 'src/app/models/user.model';
import {Booking} from 'src/app/models/booking.model';
import { UserService } from 'src/app/services/user/user.service';
import { BookingService } from 'src/app/services/booking/booking.service';
import * as moment from 'moment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users-bookings',
  templateUrl: './users-bookings.component.html',
  styleUrls: ['./users-bookings.component.css']
})
export class UsersBookingsComponent implements OnInit {

  public role: String
  public isAdmin: Boolean
  public bookings : Booking[]
  public user : User

  constructor(private userService : UserService, private bookingService: BookingService, private router: Router) { }

  ngOnInit(): void {
    if(localStorage.getItem('jwt')) this.router.navigate['/']

    this.userService.getUserByUsername(localStorage.getItem('username')).subscribe(
      data => { this.user = data; },
      error => alert('Error! '+ error.error));

    this.userService.getUsersBookings(localStorage.getItem('username')).subscribe(
      data => { this.bookings = data },
      error => alert('Error! '+ error.error));
  }

  public deleteBooking(event, booking) {
    
      this.bookingService.deleteBooking(booking, this.user.username).subscribe(
        data => window.location.reload(),
        error => alert('Error! '+ error.error));
  }

  public canUserDeleteBooking(event, ticket): boolean{
     
      let now = moment(new Date()); //todays date
      let departDate = moment(ticket.departDate);
      let duration = moment.duration(departDate.diff(now));
      let hours = duration.asHours();

      if(hours >= 24) return true;
      else return false;
  }

  public buyTickets(event) {
    this.bookingService.buyTickets(this.user.username).subscribe(
      data => window.location.reload(),
      error => alert('Error! '+ error.error));
  }

}
