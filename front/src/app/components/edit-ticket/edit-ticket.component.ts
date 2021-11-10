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
import * as moment from 'moment';
import { UserService } from 'src/app/services/user/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-edit-ticket',
  templateUrl: './edit-ticket.component.html',
  styleUrls: ['./edit-ticket.component.css']
})
export class EditTicketComponent implements OnInit {

  public ticket : Ticket
  public id : number
  public editTicketForm: FormGroup
  public airlines : Airline[]
  public flights : Flight[]
  public returndate : string
  public departdate : string
  public user : User

  constructor(private activatedRoute: ActivatedRoute, private router: Router, 
              private ticketService : TicketService,  private formBuilder: FormBuilder,
              private airlineService: AirlineService, private flightService : FlightService,
              private userService : UserService) {

      this.editTicketForm = this.formBuilder.group({
        departDate: ['', [Validators.required]],
        returnDate: '',
        airlineForm:['', [Validators.required]],
        flightForm:['', [Validators.required]],
        count:['', [Validators.required]]
      })
  }

  ngOnInit(): void {
    
    if(localStorage.getItem('jwt') === '') this.router.navigate['/']

    this.userService.getUserByUsername(localStorage.getItem('username')).subscribe(
      data => { this.user = data },
      error => alert('Error! '+ error.error));

    this.activatedRoute.paramMap.subscribe(params => {
      this.id = Number(params.get('id'))
      console.log(this.id)

      this.ticketService.getTicketById(this.id).subscribe(data => {
        console.log(data);
        this.ticket = data
        this.departdate = new Date(this.ticket.departDate).toISOString().substr(0, 10)
        this.returndate = new Date(this.ticket.returnDate).toISOString().substr(0, 10)
      })   
      
      this.airlineService.getAirlines().subscribe(
        data => { this.airlines = data },
        error => alert('Error! '+ error.error));

      this.flightService.getFlights().subscribe(
      data => { this.flights = data },
      error => alert('Error! '+ error.error));

    })
  }


public submitForm(searchData) {
  let oneway : boolean = false;
  if(searchData.returnDate==='') oneway = true;

  let departD : Date= moment(searchData.departDate, 'YYYY-MM-DD').toDate();
  let returnd : Date= moment(searchData.returnDate, 'YYYY-MM-DD').toDate();


  if(searchData.count <= 0){
    alert("Count has to be > 0!");
    return;
  }


  if(departD > returnd){
    alert("Depart date can't be after return date!");
    return;
  }else{
    console.log("nije")
  }

  let airlineObj : Airline 

  for (let i = 0; i < this.airlines.length; i++) {
    if(this.airlines[i].id == searchData.airlineForm)
      airlineObj = this.airlines[i]
  }

  let flightObj : Flight

  for (let i = 0; i < this.flights.length; i++) {
    if(this.flights[i].id == searchData.flightForm)
        flightObj = this.flights[i]
  }

  this.ticket.airline = airlineObj
  this.ticket.flight = flightObj
  this.ticket.departDate = departD
  this.ticket.returnDate = returnd
  this.ticket.oneway = oneway
  this.ticket.count = searchData.count

  this.ticketService.editTicket(this.ticket).subscribe(
      data => { alert('Ticket is successfully changed! Go to home page.')},
      error => alert('Error! '+ error.error)
      );
}


public goToHomePage(event){
  this.router.navigate(['/home'])
}

public get origin() {
  return this.editTicketForm.get('origin')
}
public get destination() {
  return this.editTicketForm.get('destination')
}
public get departDate() {
  return this.editTicketForm.get('departDate')
}
public get returnDate() {
  return this.editTicketForm.get('returnDate')
}

public get airlineForm() {
  return this.editTicketForm.get('airlineForm')
}

public get flightForm() {
  return this.editTicketForm.get('flightForm')
}

public get count() {
  return this.editTicketForm.get('count')
}

}