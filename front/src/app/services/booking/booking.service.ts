import { Injectable } from '@angular/core';
import { Ticket } from 'src/app/models/ticket.model';
import { TicketService } from 'src/app/services/ticket/ticket.service';
import { UserService } from 'src/app/services/user/user.service';
import {Airline} from 'src/app/models/airline.model';
import {Flight} from 'src/app/models/flight.model'
import {User} from 'src/app/models/user.model'
import { AirlineService } from 'src/app/services/airlines/airline.service';
import {Booking} from 'src/app/models/booking.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private readonly url = 'http://localhost:8081/api/bookings'
  private bookings: Observable<Booking[]>
  private booking : Observable<Booking>

  constructor(private http: HttpClient, private userService : UserService) { }

  public getBookings(): Observable<Booking[]> {
    let url1 = this.url + "/all";
    this.bookings = this.http.get<Booking[]>(url1, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    return this.bookings
  }


  public createBooking(ticket, available): Observable<Booking> {
    
    let booking : Booking = {'id':35, 'flight':ticket.flight, 'ticket': ticket, 'available':available}
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                                          .set('Authorization','Bearer '+localStorage.getItem("jwt")) };
    this.booking =  this.http.post<Booking>(this.url, JSON.stringify(booking), config)
    return this.booking
  }


  public deleteBooking(booking, username){
    
    let params = new HttpParams().set('username', username)
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                                          .set('Authorization','Bearer '+localStorage.getItem("jwt")) };

    return this.http.delete(this.url +"/"+booking.id, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })


    return this.bookings
  }


  
  public deleteBookingByTicketId(id){
    
    let params = new HttpParams().set('ticketId', id)
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                                          .set('Authorization','Bearer '+localStorage.getItem("jwt")) };

    return this.http.delete(this.url +"/ticket/"+id, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })


    return this.bookings
  }

  public buyTickets(username){

    let params = new HttpParams().set('username', username)
    
    const config = { headers: new HttpHeaders().set('Authorization','Bearer '+localStorage.getItem("jwt")) };

    return this.http.delete(this.url +"/buy", {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    
  }

  
  public canUserBookATicket(id){
    let params = new HttpParams().set('ticketId', id)
    let url1 = this.url + "/bookvalid";
    return this.http.get<Boolean>(url1, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    
  }
  

}
