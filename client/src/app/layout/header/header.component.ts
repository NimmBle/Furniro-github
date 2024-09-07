import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  isCartOpen: boolean = false;

  toggleCart() {
    this.isCartOpen = !this.isCartOpen;
    console.log(this.isCartOpen);
  }

  ngOnInit(): void {
    localStorage.getItem("cart")
  }

}
