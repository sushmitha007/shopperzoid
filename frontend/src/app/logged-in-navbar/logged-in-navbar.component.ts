import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { NgxSpinnerService } from 'ngx-spinner';


@Component({
  selector: 'app-logged-in-navbar',
  templateUrl: './logged-in-navbar.component.html',
  styleUrls: ['./logged-in-navbar.component.css']
})
export class LoggedInNavbarComponent implements OnInit {

  private productName: string;
  public notOnPayment: Boolean = true;
  private token: string;
  private decToken: any;
  public isBuyer: boolean = true;

  constructor(private spinner: NgxSpinnerService, 
    private authService: AuthenticationService, 
    private router: Router) { }

  ngOnInit() {
    this.token = localStorage.getItem('token');
    this.decToken = this.checkToken(this.token);
    if (this.decToken.role === 'seller') {
      this.isBuyer = false;
    }
  }

  toSearch(event) {
    this.productName = event.target.value;
    this.spinner.show();
    this.router.navigate(['']).then(() => 
      {
        this.router.navigate(['./search', { name: this.productName }])
      }
    );
  }

  logOut() {
    this.authService.logOut();
    this.router.navigate(['']);
  }

  goToBuyerProfile() {
    if (this.decToken.role === 'buyer') {
      this.router.navigate(['./buyer-profile', { eMail: localStorage.getItem('emailId') }]);
    }
    if (this.decToken.role === 'seller') {
      this.router.navigate(['./seller-dashboard', { eMail: localStorage.getItem('emailId') }]);
    }
  }

  checkToken(tokenStr) {
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(tokenStr);
    return decodedToken;
  }
}
