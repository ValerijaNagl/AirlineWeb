import { Injectable } from '@angular/core';
import { User } from '../../models/user.model';
import { HttpClient, HttpHeaders,HttpParams  } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Booking} from 'src/app/models/booking.model';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly usersUrl = 'http://localhost:8081/api/users'
  private users: Observable<User[]>
  private korisnik : Observable<User>
  private bookings: Observable<Booking[]>

  constructor(private http: HttpClient) {
   }

   public getUsers(): Observable<User[]> {
     return this.users
   }

   public fetchUsers(): Observable<User[]> {
     this.users = this.http.get<User[]>(this.usersUrl,{
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
     })
     return this.users
   }

   public getUserByUsername(username): Observable<User> {
      let url1 = this.usersUrl + "/username";
      let params = new HttpParams().set('username', username)
      
      this.korisnik= this.http.get<User>(url1, {
        params,
        headers: {
          Authorization: "Bearer " + localStorage.getItem("jwt")
        }
      })
      return this.korisnik
  }

   public editUser(user): Observable<User> {
    const headers = new HttpHeaders()
    .set("Authorization", "Bearer " + localStorage.getItem("jwt"));
      this.korisnik = this.http.put<User>(this.usersUrl, {
      'user': user
    },
    {headers})
    console.log(this.korisnik);
    
    return this.korisnik
  }

  public add(credentials){
    let s : Observable<string>
    const data = {'username': credentials.newusername, 'password': credentials.newpassword,'type':credentials.newtype};
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json').set('Accept', 'application/json')
                  .set('Authorization','Bearer '+localStorage.getItem('jwt')) };
    return this.http.post<any>(this.usersUrl, JSON.stringify(data), config);
  }


  public makeUsersBooking(booking, username){
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json')
                  .set('Authorization','Bearer '+localStorage.getItem('jwt')) };
     return this.http.post<any>(this.usersUrl +"/booking/" + username, JSON.stringify(booking), config)
     
  }


  public getUsersBookings(username): Observable<Booking[]> {
    let url = this.usersUrl + "/bookings";
    let params = new HttpParams().set('username', username)
    
    this.bookings = this.http.get<Booking[]>(url, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    return this.bookings
  }

  public deleteUsersBooking(username, id){
    let url = this.usersUrl + "/bookings";
    let params = new HttpParams().set('bookingId', id)
    
    return this.http.delete(this.usersUrl +"/booking/" + username, {
      params,
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    
  }


  public buyTickets(username){
    let url = this.usersUrl + "/buy/" + username;
  
    return this.http.delete(url, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("jwt")
      }
    })
    
  }


  public deleteMe(id): void {
    const options = {
      headers: new HttpHeaders({
        "Authorization": "Bearer " + localStorage.getItem("jwt"),
      })
    };
    console.log(options);
    
    // this.korisnik = this.http.delete<User>(this.usersUrl + "/" + id, options)
    let url = this.usersUrl + "/" + id
    console.log(url)
    this.http.delete(url, options).subscribe((s) => {
    console.log(s);
    })
    // return this.korisnik
  }
}
