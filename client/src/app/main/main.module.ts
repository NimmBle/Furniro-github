import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MainRoutingModule } from './main-routing.module';
import { HomeComponent } from './components/home/home.component';
import { ShopComponent } from './components/shop/shop.component';
import { ProductComponent } from './components/product/product.component';
import { QualitiesComponent } from './components/shared/qualities/qualities.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { PagesHeroComponent } from './components/shared/pages-hero/pages-hero.component';
import { CartComponent } from './components/cart/cart.component';


@NgModule({
  declarations: [
    HomeComponent,
    ShopComponent,
    ProductComponent,
    QualitiesComponent,
    CheckoutComponent,
    PagesHeroComponent,
    CartComponent
  ],
  imports: [
    CommonModule,
    MainRoutingModule
  ]
})
export class MainModule { }
