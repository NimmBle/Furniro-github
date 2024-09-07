import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  private productsPath: string = environment.apiUrl + 'products';

  constructor(private httpClient: HttpClient) {

  }
  getProduct(id: number): Observable<Product> {
    return this.httpClient.get<Product>(this.productsPath + '/' + id) 
  }
  
  getProducts(page: string, size: string): Observable<Product[]> {
    let params = new HttpParams() 
      .set('page', page)
      .set('size', size);
    return this.httpClient.get<Product[]>(this.productsPath, { params });
  }
}
