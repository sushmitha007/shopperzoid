import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
   image: string;
  constructor() { }

  addImage(image){
    console.log(image);
    this.image=image;
  }

  getImage(){
    return this.image;
  }
}
