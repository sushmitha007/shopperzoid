import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private productName: string;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  toSearch(event) {
    this.productName = event.target.value;
    this.router.navigate(['']).then(() => 
      {
        this.router.navigate(['./search', { name: this.productName }])
      }
    );
  }

}
