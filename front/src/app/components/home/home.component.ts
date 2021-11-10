import { Component, OnInit } from '@angular/core';
import { Ticket } from 'src/app/models/ticket.model';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import {Airline} from 'src/app/models/airline.model';
import {Flight} from 'src/app/models/flight.model'
import {User} from 'src/app/models/user.model'
import { AirlineService } from 'src/app/services/airlines/airline.service';
import { FlightService } from 'src/app/services/flight/flight.service';
import {  formatDate, DatePipe } from '@angular/common';
import { BookingService } from 'src/app/services/booking/booking.service';
import * as moment from 'moment';
import { Booking } from 'src/app/models/booking.model';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public searchForm: FormGroup
  public ticketForm : FormGroup
  public tickets: Ticket[]
  public role: String
  public isAdmin: Boolean
  public signupForm: FormGroup
  public airlines : Airline[]
  public flights : Flight[]
  public user : User

  constructor(private ticketService: TicketService, private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder,
    private airlineService: AirlineService, 
    private flightService : FlightService,
    private bookingService : BookingService) {

    this.searchForm = this.formBuilder.group({
      origin: '',
      destination: '',
      departDate: '',
      returnDate: '',
      oneway:''
    })
    this.signupForm = this.formBuilder.group({
      newusername: ['', [Validators.required]],
      newpassword: ['', [Validators.required, Validators.minLength(6), Validators.pattern(/^[a-z0-9\-]+$/)]],
      newtype:['', [Validators.required]]
    })

    this.ticketForm = this.formBuilder.group({
      flight: ['', [Validators.required]],
      departDateTicket: ['', [Validators.required]],
      returnDateTicket: '',
      airline: ['', [Validators.required]],
      countTicket:  ['', [Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/)]]
    })
  }

  ngOnInit(): void {
    if(localStorage.getItem('jwt') === ''){
      this.router.navigate['/']
    }

    this.userService.getUserByUsername(localStorage.getItem('username')).subscribe(
      data => { this.user = data; },
      error => alert('Error! '+ error.error));

    this.ticketService.getTickets().subscribe(
      data => { this.tickets = data; },
      error => alert('Error! '+ error.error));

    this.role = localStorage.getItem('role')
    if (this.role === '[ROLE_ADMIN]') {
      this.isAdmin = true
    } else {
      this.isAdmin = false
    }
    this.airlineService.getAirlines().subscribe(
      data => { this.airlines = data; },
      error => alert('Error! '+ error.error));

    this.flightService.getFlights().subscribe(
      data => { this.flights = data; },
      error => alert('Error! '+ error.error));
  
  }

  public searchTickets(searchData) {
    searchData.airline = "";
    this.ticketService.filterTickets(searchData).subscribe(
      data => { this.tickets = data; },
      error => alert('Error! '+ error.error));
  }

  public newTicketForm(data) {
    
    let oneway : boolean = false;
    if(data.returnDateTicket==='') oneway = true;
  
    let airlineObj : Airline 

    for (let i = 0; i < this.airlines.length; i++) {
      if(this.airlines[i].id == data.airline)
        airlineObj = this.airlines[i]
    }
  
    let flightObj : Flight

    for (let i = 0; i < this.flights.length; i++) {
      if(this.flights[i].id == data.flight)
         flightObj = this.flights[i]
    }
   
    let departD : Date= moment(data.departDateTicket, 'YYYY-MM-DD').toDate();
    let returnd : Date= moment(data.returnDateTicket, 'YYYY-MM-DD').toDate();

    let valid3= moment(data.departDateTicket, 'YYYY-MM-DD');
    let valid4= moment(data.departDateTicket, 'YYYY-MM-DD');
    

    if(data.countTicket <= 0){
      alert("Count has to be > 0!");
      return;
    }

    if(!(valid3.isValid() && valid4.isValid())){
      alert("Wrong format of date or date doesn't exist!");
      return;
    }

 
    if(data.returnDateTicket!==''){
      if(departD > returnd){
        alert("Depart date can't be after return date!");
        return;
      }
    }
    
  
    let ticket : Ticket = {airline: airlineObj, flight: flightObj, departDate:departD,
                  returnDate : returnd, count: data.countTicket, id : 35, oneway:oneway}
  
    this.ticketService.addTicket(ticket).subscribe(
      data => { window.location.reload(); },
      error => alert('Error! '+ error.error));
  }

  public createUser(credentials) {
    this.userService.add(credentials).subscribe(
      data => { alert('User is created!'); 
      window.location.reload();
    },
      error => alert('Error! '+ error.error));
    
 } 

 public deleteTicket(event, id){
  
  this.ticketService.deleteTicket(id).subscribe(
    data => {  window.location.reload(); },
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


public canUserBookATicket(event, ticket): boolean{
  let answer : Boolean
  if(ticket.count > 0) return true;
  else return false;
}



public get flight() {
  return this.ticketForm.get('flight')
}
public get airline() {
  return this.ticketForm.get('airline')
}
public get departDateTicket() {
  return this.ticketForm.get('departDateTicket')
}
public get returnDateTicket() {
  return this.ticketForm.get('returnDateTicket')
} 
public get countTicket() {
  return this.ticketForm.get('countTicket')
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

public get newusername() {
  return this.signupForm.get('newusername')
}

public get newpassword() {
  return this.signupForm.get('newpassword')
}

public get newtype() {
  return this.signupForm.get('newtype')
}

public logout(event){
  localStorage.setItem('username', "");
  localStorage.setItem('jwt',"");
  localStorage.setItem('role',"")
  this.router.navigate['/']
}



}
