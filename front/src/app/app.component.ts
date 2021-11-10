import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'cas2';
 

  constructor(private router: Router) { }

  ngOnInit(): void {
    
    // console.log(this.datePipeString); 
  }

  public logout(event){
    localStorage.setItem('username', "");
    localStorage.setItem('jwt',"");
    localStorage.setItem('role',"")
    this.router.navigate['/']
  }
}
