import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Order } from '../models/order';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  private orderPath: string = environment.apiUrl + 'orders/add';

  constructor(private httpClient: HttpClient) { 

  }

  postOrder(order: Order) : Observable<Order> {
    return this.httpClient.post<Order>(this.orderPath, order);
  }
}
