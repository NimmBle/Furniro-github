import { Component, ElementRef, ViewChild } from '@angular/core';
import { ProductsService } from '../../services/products.service';
import { CategoriesService } from '../../services/categories.service';
import { Product } from '../../models/product';
import anime from 'animejs/lib/anime.es.js';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  @ViewChild('wrapper') carouselWrapper!: ElementRef;

  constructor(
    private productService: ProductsService,
    private categoryService: CategoriesService ) 
  {}
  
  products: Array<Product> = [];
  numberOfProducts: number[] = [1, 2, 3, 4, 5, 6, 7, 8];
  slides = [1, 2, 3, 4];
  activeSlideIndex: number = 0;
  productDiscount: number = 30;
  newLabel: string = "New";

  nextSlide() {  
    let distanceBetweenSlides = 424;
    this.carouselWrapper!.nativeElement.scrollLeft += (distanceBetweenSlides);

  }
  slidesToggle(index: number) {
    this.nextSlide();
    this.activeSlideIndex = index;
  }

  ngOnInit(): void {
    this.productService.getProducts("0", "8").subscribe(
      res => {
        this.products = res.data;
      }
    );
  }
}
