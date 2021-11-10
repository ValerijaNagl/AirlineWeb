import { Injectable } from '@angular/core';
import { Flight } from 'src/app/models/flight.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FlightService {


  private readonly url = 'http://localhost:8081/api/flights'
  private flights: Observable<Flight[]>
  private ticket : Observable<Flight>

  constructor(private http: HttpClient) { }

  public getFlights(): Observable<Flight[]> {
    let url1 = this.url + "/all";
   
    this.flights = this.http.get<Flight[]>(url1, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    return this.flights
  }

  public getFlightById(id): Observable<Flight> {
    let params = new HttpParams().set('ticketId', id);
 
    console.log(params)
    
    this.ticket = this.http.get<Flight>(this.url, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    console.log(this.ticket)
    return this.ticket
  }
}
