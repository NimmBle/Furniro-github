import { Component } from '@angular/core';
import { ProductsService } from '../../services/products.service';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent {

  sizes: string[] = ["L", "XL", "XS"];
  produtTitle: string = "Asgaard sofa";
  price: string = "250,000.00";
  currentPrimaryImage: any = 0;
  currentColor: any = 0;
  test: any = 1;
  colors: string[] = ["purple", "black", "burlywood"];
  quantity: number = 1;
  productId!: number;

  constructor(private productService: ProductsService,
              private route: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.route.params.subscribe((param: Params) => {
      this.productId = param['id']
    })

    // this.productService.getProduct(this.productId).subscribe(
    //   rep => {
    //     rep.
    // })
  }

  selectSize(i: number) {
    this.currentPrimaryImage = i;
  }
  
  selectColor(i: number) {
    this.currentColor = i;
  } 

  decreaseQuantity() {
    if (this.quantity == 1) {
      return;
    }
    this.quantity--;
  }

  increaseQuantity() {
    this.quantity++;  
  }

}
