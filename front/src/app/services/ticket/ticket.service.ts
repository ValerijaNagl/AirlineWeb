import { Injectable } from '@angular/core';
import { Ticket } from 'src/app/models/ticket.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  private readonly url = 'http://localhost:8081/api/tickets'
  private tickets: Observable<Ticket[]>
  private ticket : Observable<Ticket>

  constructor(private http: HttpClient) { }

  public getTickets(): Observable<Ticket[]> {
    let url1 = this.url + "/all";
    this.tickets = this.http.get<Ticket[]>(url1, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    return this.tickets
  }

  public filterTickets(search): Observable<Ticket[]> {

    let params = new HttpParams().set('oneway', search.oneway)
    .set('departDate', search.departDate).set('returnDate',search.returnDate)
    .set('origin', search.origin).set('destination',search.destination)
    .set('airline',search.airline)

    console.log(params)
    
    let url1 = this.url + "/search";
    this.tickets = this.http.get<Ticket[]>(url1, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    return this.tickets
  }

  public editTicket(ticket : Ticket){
    
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                                          .set('Authorization','Bearer '+localStorage.getItem("jwt")) };
    return this.http.put<Ticket>(this.url, JSON.stringify(ticket), config)
    
  }

  public addTicket(ticket : Ticket): Observable<Ticket> {
    
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                                          .set('Authorization','Bearer '+localStorage.getItem("jwt")) };
    return this.http.post<Ticket>(this.url, JSON.stringify(ticket), config)
  }

  public deleteTicket(id){
    
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                                          .set('Authorization','Bearer '+localStorage.getItem("jwt")) };
    return this.http.delete<Ticket>(this.url +"/"+id, config)
  }

  public getTicketById(id): Observable<Ticket> {
    let params = new HttpParams().set('ticketId', id);
 
    console.log(params)
    
    this.ticket = this.http.get<Ticket>(this.url, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    console.log(this.ticket)
    return this.ticket
  }

  public getTicketByAirlineId(id): Observable<Ticket[]> {
    let params = new HttpParams().set('airlineId', id);
 
    this.tickets = this.http.get<Ticket[]>(this.url+"/airline", {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
  
    return this.tickets
  }




}
