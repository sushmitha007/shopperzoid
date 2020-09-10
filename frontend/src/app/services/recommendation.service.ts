import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment'

const httpOptions = {
  headers: new HttpHeaders({'Content-Type':'application/json'})
};
@Injectable({
  providedIn: 'root'
})
export class RecommendationService {

  private recomsUrl: string;
 
  constructor(private http: HttpClient) {
    this.recomsUrl = environment.recommendationUrl;
    //this.recomsUrl = "http://localhost:8110/rest/neo4j";
  }

  public findBuyer(buyerEmail) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      })
    };
    return this.http.get(this.recomsUrl+`/buyer/${buyerEmail}/`,httpOptions);
  }

  public findBuyerRecoms(buyerId) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      })
    };
    return this.http.get(this.recomsUrl+`/buyer/products/recommendation/${buyerId}/`,httpOptions);
  }

  public buyProduct(buyerId,productName){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      })
    };
    this.http.get(this.recomsUrl+`/buyer/Buy/${buyerId}/${productName}`,httpOptions);
  }

  public addProductToCart(buyerId,productName){
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      })
    };
    this.http.get(this.recomsUrl+`/buyer/Cart/${buyerId}/${productName}`,httpOptions);
  }

}
