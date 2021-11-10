import { Injectable, OnDestroy,OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators'

@Injectable({
  providedIn: 'root'
})
export class LoginService implements OnDestroy,OnInit {

  private readonly loginUrl = 'http://localhost:8081/auth/login'

  constructor(private http: HttpClient) { }

  ngOnDestroy(): void {
    this.logout()
  }

  ngOnInit(): void {
    localStorage.removeItem("jwt")
  }


  login(credentials) {
    const data = {'username': credentials.username, 'password': credentials.password};
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json') };
    return this.http.post<any>(this.loginUrl, data, config)
      .pipe(map(res => {
            console.log(res.jwt);
            localStorage.setItem('username', credentials.username);
            localStorage.setItem('jwt', res.jwt);
            localStorage.setItem('role', res.type);
      }))
    }

  logout(){
    localStorage.removeItem("jwt")
    localStorage.clear()
  }
}
