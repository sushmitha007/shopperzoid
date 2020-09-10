import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { SellerDashboardService } from '../services/seller-dashboard.service';
import { SingleDataSet, Label } from 'ng2-charts';
import { ChartType } from 'chart.js';
import { AddProductService } from '../services/add-product.service';


export interface SellerInventoryData {
  productName: string;
  productImage: string;
  productDescription: string;
  productCategory: string;
  // productSubcategory: string;
}

@Component({
  selector: 'app-seller-dashboard-inventory',
  templateUrl: './seller-dashboard-inventory.component.html',
  styleUrls: ['./seller-dashboard-inventory.component.css']
})

export class SellerDashboardInventoryComponent implements OnInit {

  private eMail: string;
  private seller: any;
  public dataSource: SellerInventoryData[];

  private toggleAnalytics = false;
  private sellerAnalyticsObject: any;

  private polarAreaChartLabels: Label[] = ['Units in Stock', 'Units Sold', 'Units Returned'];
  private polarAreaChartData: SingleDataSet;
  private polarAreaLegend = true;
  public polarAreaChartType: ChartType = 'polarArea';

  constructor(private router: Router,
              private route: ActivatedRoute,
              private _sellerDashboardService: SellerDashboardService,
              private _addProductService: AddProductService) { }

  // Beginning of ngOnInit
  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.eMail = params.get('eMail');
      this.eMail = localStorage.getItem('emailId');
      console.log(this.eMail);

      this._sellerDashboardService.getFromDatabase(this.eMail).subscribe(response =>
        {
          this.seller = response;
          console.log(response);

          this.dataSource = this.seller.sellerProducts;
          console.log(this.dataSource);
          console.log(this.seller);
        },
        error => {
          console.log(error);
        }
      );

    });
  }
  // End of ngOnInit


  // Table columns & data source configurations
  displayedColumns: string[] = [
    'productName', 
    'productImage', 
    'productDescription',
    'productCategory', 
    // 'productSubcategory', 
    'editProduct'
  ];


  // Product analytics
  openAnalytics(productName) {
    this._addProductService.getSeller(this.eMail, productName).subscribe(response => {
      this.sellerAnalyticsObject = response;
      console.log(this.sellerAnalyticsObject);
    });

    this.polarAreaChartData = [
      this.sellerAnalyticsObject.productStock,
      this.sellerAnalyticsObject.productSold,
      this.sellerAnalyticsObject.productReturned
    ];

    this.toggleAnalytics = !this.toggleAnalytics;
  }


  addProduct(){
    this.router.navigate(['./update-product', { eMail: this.eMail }]);
  }

  goToSellerDashboard(){
    this.router.navigate(['./seller-dashboard', { eMail: this.eMail }]);
  }

  editProduct(prName){
    this.router.navigate(['./edit-product', { eMail: this.eMail, prName: prName }]);
  }

  goToSellerDashboardProfile() {
    this.router.navigate(['./seller-dashboard-profile', { eMail: this.eMail }]);
  }

}