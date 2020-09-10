import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { SellerDashboardService } from '../services/seller-dashboard.service';

@Component({
  selector: 'app-seller-dashboard-profile',
  templateUrl: './seller-dashboard-profile.component.html',
  styleUrls: ['./seller-dashboard-profile.component.css']
})
export class SellerDashboardProfileComponent implements OnInit {

  private eMail: string;
  private seller: any;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private _sellerDashboardService: SellerDashboardService) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.eMail = params.get('eMail');
      this.eMail = localStorage.getItem('emailId');
      console.log(this.eMail);

      this._sellerDashboardService.getFromDatabase(this.eMail).subscribe(response =>
        {
          this.seller = response;
          console.log(response);
          console.log(this.seller);
        },
        error => {
          console.log(error);
        }
      );

    });
  }

  goToSellerDashboard(){
    this.router.navigate(['./seller-dashboard', { eMail: this.eMail }]);
  }

  goToInventory(){
    this.router.navigate(['./seller-dashboard-inventory', { eMail: this.eMail }]);
  }

}
