import { Component, OnInit } from '@angular/core';
import { BuyerDashboardServiceService } from '../services/buyer-dashboard-service.service';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { RecommendationService } from '../services/recommendation.service';
import { DealsService } from '../services/deals.service';

@Component({
  selector: 'app-buyer-dashboard',
  templateUrl: './buyer-dashboard.component.html',
  styleUrls: ['./buyer-dashboard.component.css']
})
export class BuyerDashboardComponent implements OnInit {

  public recoms = {};
  private eMail: string;
  private productName: string;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private _buyerService: BuyerDashboardServiceService,
              private _recommendationService: RecommendationService,
              private _dealsService: DealsService) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.eMail = params.get('emailId');
      console.log(this.eMail);
    });

    this.eMail = localStorage.getItem('emailId');

    this._recommendationService.findBuyerRecoms(this.eMail).subscribe(data => 
      {
        if(data) {
          console.log(data);
          this.recoms = data;
        }

        console.log(this.recoms);
      },
      error => {
        console.log(error);
      }
    );
  }
  // ngOnInit end


  // routing on clicking the recommendation cards
  goToProductDetails(productName){
    this.router.navigate(['./search', { name:productName }]);
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
