import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { AddProductService } from '../services/add-product.service';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';

export class Seller{
  sellerId: string;
  sellerName: string;
  sellerRatings: number;
  productReturned: number;
  productPrice: number;
  productStock: number;
  productSold: number;
  sellerIndex: number;
 }  

@Component({
  selector: 'app-seller-edit-product',
  templateUrl: './seller-edit-product.component.html',
  styleUrls: ['./seller-edit-product.component.css']
})
 
export class SellerEditProductComponent implements OnInit {
  private eMail: string;
  private productName: string;
  private desc: string;
  private editForm: FormGroup;
  private seller: Seller={
    sellerId:'',
    sellerIndex:0,
    sellerName:'',
    sellerRatings:0,
    productPrice:0,
    productReturned:0,
    productSold:0,
    productStock:0
  };

  constructor(private route: ActivatedRoute, private addProd: AddProductService, private _formBuilder: FormBuilder,private router:Router) { }

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.eMail = params.get('eMail');
      this.productName = params.get('prName');
    });
    this.eMail=localStorage.getItem('emailId');
    this.addProd.searchProduct(this.productName).subscribe(data => {
      if (data) {
        this.desc = data.productDescription;
      }
    });
   
    this.editForm = this._formBuilder.group({
      price: ['', Validators.required],
      quantity: ['', Validators.required]
    });   
    
  }
  
  doEdit(){
      this.addProd.getSeller(this.eMail,this.productName).subscribe(data =>{
      this.seller.sellerId=this.eMail;
      this.seller.productPrice=this.editForm.value.price;
      this.seller.productReturned=data.productReturned;
      this.seller.productSold=data.productSold;
      this.seller.productStock=this.editForm.value.quantity;
      this.seller.sellerIndex=data.sellerIndex;
      this.seller.sellerName=data.sellerName;
      this.seller.sellerRatings=data.sellerRatings;
      this.addProd.editSellerProduct(this.seller).subscribe();
      this.router.navigate(['./seller-dashboard-inventory']);
    });

  }


}
