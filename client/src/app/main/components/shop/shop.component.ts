import { Component, ElementRef, ViewChild } from '@angular/core';
import { Product } from '../../models/product';
import { ProductsService } from '../../services/products.service';
import { convertToParamMap, Router } from '@angular/router';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.scss']
})
export class ShopComponent {
  products: Array<Product> = []
  numberOfPages: number = 0;
  productsPerPage = "16";
  currentProductPageIndex = 0;
  numberOfPagesArray!: number[];
  pageTitle: string = "Shop";

  constructor(private productService: ProductsService, private router: Router) {
  }

  ngOnInit(): void {
    this.productService.getProducts("0", this.productsPerPage).subscribe(
      res => {
        this.products = res;
      }
    )
    this.numberOfPages = Math.ceil(this.products.length / Number(this.productsPerPage));
    this.numberOfPagesArray = this.getNumberArray(this.numberOfPages);
  }

  updateShowPerPage(event: any) {
    this.productsPerPage = event.target.value;
    this.productService.getProducts("0", this.productsPerPage).subscribe(
      res => {
        this.products = res;
      }
    )
    this.numberOfPages = Math.ceil(this.products.length / Number(this.productsPerPage));
    this.numberOfPagesArray = this.getNumberArray(this.numberOfPages);
  }

  getNumberArray(n: number): number[] {
    return Array.from({ length: n }, (_, i) => i);
  }

  nextPage(index: number) {
    this.currentProductPageIndex = index;
    this.productService.getProducts(String(Number(this.productsPerPage) * this.currentProductPageIndex), this.productsPerPage).subscribe(
      res => {
        this.products = res;
      }
    )
  }

  redirectToProductDetailPage(id: any) {
    this.productService.getProduct(id).subscribe(
      res => {
        this.router.navigate(['/main/product/', id]);
      }
    )
  }
}
