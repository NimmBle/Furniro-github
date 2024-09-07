import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ShopComponent } from './components/shop/shop.component';
import { ProductComponent } from './components/product/product.component';

const routes: Routes = [
 { path: '', component: HomeComponent },
 { path: 'shop', component: ShopComponent },
 { path: 'product/:id', component: ProductComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
