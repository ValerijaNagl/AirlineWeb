import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Ticket } from 'src/app/models/ticket.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import {Airline} from 'src/app/models/airline.model';
import {Flight} from 'src/app/models/flight.model'
import { AirlineService } from 'src/app/services/airlines/airline.service';
import { FlightService } from 'src/app/services/flight/flight.service';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/models/user.model';
import { BookingService } from 'src/app/services/booking/booking.service';
import { Booking } from 'src/app/models/booking.model';

@Component({
  selector: 'app-airline-page',
  templateUrl: './airline-page.component.html',
  styleUrls: ['./airline-page.component.css']
})

export class AirlinePageComponent implements OnInit {

  public airline : Airline
  public id : number
  public tickets : Ticket[]
  public isAdmin: Boolean
  public role: String
  private newAirlineForm : FormGroup
  private changeAirlineForm : FormGroup
  public searchForm: FormGroup
  public user : User
  

  constructor(private activatedRoute: ActivatedRoute, private router: Router, 
              private ticketService : TicketService,  private formBuilder: FormBuilder,
              private airlineService: AirlineService, private flightService : FlightService,
              private userService : UserService, private bookingService : BookingService) { 

    this.newAirlineForm = this.formBuilder.group({
      newairline: ['', [Validators.required]]
    })

    this.changeAirlineForm = this.formBuilder.group({
      changeairline: ['', [Validators.required]]
    })

    this.searchForm = this.formBuilder.group({
      origin: '',
      destination: '',
      departDate: '',
      returnDate: '',
      oneway:''
    })

  }

ngOnInit(): void {
  if(localStorage.getItem('jwt') === '') this.router.navigate['/']

  this.userService.getUserByUsername(localStorage.getItem('username')).subscribe(data => {
    this.user = data;
  })
  this.role = localStorage.getItem('role')
  if (this.role === '[ROLE_ADMIN]') {
    this.isAdmin = true
  } else {
    this.isAdmin = false
  }
  this.activatedRoute.paramMap.subscribe(params => {
    this.id = Number(params.get('id'))
    console.log(this.id)

    this.airlineService.getAirlineById(this.id).subscribe(
      data => { this.airline = data;},
      error => alert('Error! '+ error.error)
      );

    this.ticketService.getTicketByAirlineId(this.id).subscribe(
      data => { this.tickets = data;},
      error => alert('Error! '+ error.error)
      );

  })
}

public canUserBookATicket(event, ticket): boolean{
  if(ticket.count > 0) return true;
  else return false;
}

public deleteTicket(event, id){
     this.ticketService.deleteTicket(id).subscribe(
      data => {  window.location.reload(); },
      error => alert('Error! '+ error.error));
}


public deleteAirline(event, id){
  this.airlineService.deleteAirline(this.id).subscribe(data => {
    this.router.navigate(['/home']);
  });
}

public changeAirline(name) {
  this.airlineService.changeAirline(name, this.id).subscribe(
    data => {  window.location.reload(); },
    error => alert('Error! '+ error.error));
} 


public createAirline(name) {
  this.airlineService.createAirline(name).subscribe(
    data => {  window.location.reload(); },
    error => alert('Error! '+ error.error));
} 

public searchTickets(searchData) {
  searchData.airline = this.airline.name
  this.ticketService.filterTickets(searchData).subscribe(
    data => { this.tickets = data; },
    error => alert('Error! '+ error.error));
}



public bookTicket(event, ticket : Ticket){
  let IsAvailable : boolean 
  let today= new Date()
  
  if(today > new Date(ticket.departDate)){
    IsAvailable = false
  }else{
    IsAvailable = true
  }
  let booking : Booking = {'id':35, 'flight':ticket.flight, 'ticket': ticket, 'available':IsAvailable}

  let answer : Boolean
  this.bookingService.canUserBookATicket(ticket.id).subscribe(
    data => { 
    if(data){
      this.userService.makeUsersBooking(booking, this.user.username).subscribe(user =>{
        window.location.reload();
      })
    }else{
      alert("Wait for the ticket to be available!");
    }
    
    }
  );

}

public get newairline() {
  return this.newAirlineForm.get('newairline')
}

public get changeairline() {
  return this.changeAirlineForm.get('changeairline')
}

public get origin() {
  return this.searchForm.get('origin')
}
public get destination() {
  return this.searchForm.get('destination')
}
public get departDate() {
  return this.searchForm.get('departDate')
}
public get returnDate() {
  return this.searchForm.get('returnDate')
}

public get oneway() {
  return this.searchForm.get('oneway')
}

}
