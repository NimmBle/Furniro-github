import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  private categoriesPath: string = environment.apiUrl + 'categories';

  constructor(private httpClient: HttpClient) {

  }

  getCategories(): Observable<any> {
    return this.httpClient.get<any>(this.categoriesPath);
  }
}
