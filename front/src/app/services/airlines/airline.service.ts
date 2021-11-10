import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {Airline} from 'src/app/models/airline.model'

@Injectable({
  providedIn: 'root'
})
export class AirlineService {

  private readonly url = 'http://localhost:8081/api/airlines'
  private airlines: Observable<Airline[]>
  private airline : Observable<Airline>

  constructor(private http: HttpClient) { }

  public getAirlines(): Observable<Airline[]> {
    let url1 = this.url + "/all";
    this.airlines = this.http.get<Airline[]>(url1, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    return this.airlines
  }

  public getAirlineById(id): Observable<Airline> {
    let params = new HttpParams().set('airlineId', id);
    this.airline = this.http.get<Airline>(this.url, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    console.log(this.airline)
    return this.airline
  }

  public createAirline(name): Observable<Airline> {
    let airline = {'id':35, 'name':name.newairline }
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                  .set('Authorization','Bearer '+localStorage.getItem("jwt")) };

    this.airline =  this.http.post<Airline>(this.url, JSON.stringify(airline), config)
    return this.airline
  }


  public changeAirline(name,id): Observable<Airline> {
    let airline = {'id':id, 'name':name.changeairline }
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                  .set('Authorization','Bearer '+localStorage.getItem("jwt")) };
                  
    this.airline =  this.http.put<Airline>(this.url, JSON.stringify(airline), config)
    return this.airline
  }

  public deleteAirline(id){
    
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                                          .set('Authorization','Bearer '+localStorage.getItem("jwt")) };
    return this.http.delete<Airline>(this.url +"/"+id, config)
   
  }

 
}
