import { Component } from '@angular/core';
import { Product } from 'src/app/main/models/product';
import { ProductsService } from 'src/app/main/services/products.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  isCartOpen: boolean = false;
  cartProducts!: Array<Product>;
  cart = [];
  products: any;

  constructor (private productService: ProductsService) {}

  async toggleCart() {

    let cart = JSON.parse(localStorage.getItem("cart")!);
    for (let i = 0; i < cart.length; i++) {
      this.productService.getProduct(cart[i].id).subscribe(
        res => {
          this.cartProducts.push(res);
          console.log(this.cartProducts);
        }
      )
    }
    

    this.toggleCartMenu();
  }
  toggleCartMenu() {
    this.isCartOpen = !this.isCartOpen;
  }

  async fetchAllCartProducts() {
  }

  removeItemFromCart(i: number) {

  }
}
