import { Component, OnInit } from '@angular/core';
import { ProductDetailsService } from '../services/product-details.service';
import { IncartProducts } from './incartProducts';
import { summaryFileName } from '@angular/compiler/src/aot/util';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../services/order.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

export class Product 
    {
      productName: string;
      productDescription: string;
      productPrice: any; // what type ?
      productImage: string;
      sellerEmail: string;
      productQty: number;
      productId: string;
    }

export class Order 
    {

      rating: any; // what type ??
      products: Product[];
      buyerPhone: any;
      deliveryAddress: string;
      status: string;
      buyerEmail: string;
    }

@Component({
  selector: 'app-incart-products',
  templateUrl: './incart-products.component.html',
  styleUrls: ['./incart-products.component.css']
})


export class IncartProductsComponent implements OnInit {

              private order: Order = {
                    rating: '',
                    products: [],
                    buyerPhone: '',
                    deliveryAddress: '',
                    status: '',
                    buyerEmail: ''
              };
              // incartProductArray = [];
              private incartProductArray: IncartProducts[];
              productQty = 1;
              productTotal = 0;
              totalAmount = 0;

              private i = 1;
              private total = [];
              private sum = 0;
              private count = 0;
              private addressAdded = true;

              private email: string;
              private name = 'user';
              private checkAddress = "Enter Your Address before making payment";
              private Address = "You Can't place your order without adding address.";

              // private paymentForm = new FormGroup({
              //   address: new FormControl("", Validators.required)
              // });

              constructor(private router: ActivatedRoute,
              private _productDetailsService: ProductDetailsService,
              private route: Router,
              private orderService: OrderService) { }
              
              ngOnInit() {
                this.email = localStorage.getItem('emailId');
                console.log('local storage email is: ' + this.email);
            
                this._productDetailsService.getInProductList().subscribe(
                  list => {
                    
                    this.incartProductArray = list.map(item => {
                      console.log(item.key)
                      let $key = item.key 
                      return {...item.payload.val(), $key };
                     });
                    console.log(this.incartProductArray);
                    this.order.products = this.incartProductArray;
                    console.log(this.order,"Order appended");
                    
                  });
              
            
                this.order.buyerEmail = this.email;
                this.count = 0;
            
              }

  addProductInOrder(incart): any {
        console.log(incart);
        this.order.products[this.count].sellerEmail = incart.sellerEmail;
        this.order.products[this.count].productDescription = incart.productDescription;
        this.order.products[this.count].productImage = incart.productImage;
        this.order.products[this.count].productName = incart.productName;
        this.order.products[this.count].productPrice = incart.productPrice;
        this.order.products[this.count].productQty = incart.productQty;
        this.count++;
        console.log(this.count);
        return this.order.products[this.count - 1].productName;
  }

  // getAddress(){
  //   this.addressType = this.addressFormGroup.value.address1;
  //   console.log(this.addressType);
  //   if(this.addressType==='Other'){
  //     this.isOther=true;
  //     this.deliveryAddress=this.addressFormGroup.value.otherAddress;
  //   }
  //   else if(this.addressType==='Home'){
  //     this.isOther=false;
  //     //this.deliveryAddress=this.buyer.buyerHomeAddress;
  //     this.deliveryAddress="this.buyer.buyerHomeAddress";
  //   }
  //   else if(this.addressType==='Office'){
  //     this.isOther=false;
  //     //this.deliveryAddress=this.buyer.buyerOfficeAddress;
  //     this.deliveryAddress="this.buyer.buyerOfficeAddress";
  //   }
  //   this.check();
  // }
  // check(){
  //   console.log(this.deliveryAddress);
  // }

  saveAddress(address){
    
  }

  increase(incart) {
        console.log('In function increase()');

        if (incart.productQty < 5) {
          (incart.productQty)++;
          // this.total=this.total+incart.productPrice;
          this._productDetailsService.updateIncartProduct(incart);
        }
  }

  isLogin() {
        const user = localStorage.getItem('emailId');
        console.log(user);

        if (user == null) {
          this.route.navigate['/login-page'];
        }
  }

  addPrice(incart) {
    this.total.push(incart.productPrice * incart.productQty);
    console.log(this.total);
    return incart.productPrice * incart.productQty;
  }

  serialNumber() {
    return this.i++;
  }

  getTotal(): Number {
    this.sum = 0;
    if (this.total.length != 0) {
      this.total.forEach(element => {
        this.sum = this.sum + element;
      });
      this.total = []; console.log(this.sum);
      return this.sum;

    } else { return 0; }
  }

  decrease(incart) {
    console.log('In function decrease()');

    if (incart.productQty > 1) {
      incart.productQty--;
      // this.total=(this.total)-incart.productPrice;
      this._productDetailsService.updateIncartProduct(incart);
    }
  }

  onDelete($key) {
    console.log('delete incart', $key);
    this._productDetailsService.deleteIncartProduct($key);
  }

  goToPayment(incart,address) {
    // this.product.productName=this.incartProductArray.
    // this.order.products.push()
    
    if(address===""){
      // this.addressAdded=false;
      this.checkAddress = "please add address before making payment";
      console.log(this.Address);
    }
    else{
    this.order.deliveryAddress=address;
    console.log(this.order.deliveryAddress);
    this.orderService.saveToOrdersOnBuyerProfile(this.order).subscribe();

    this.route.navigate(['/payment', { email: this.email, name: this.name, amount: this.sum }]);
    this._productDetailsService.deleteIncartOnPay(incart);
    console.log(this.order);
    }     
  }

}
