import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
 
  slides = [1, 2, 3, 4];
  activeSlideIndex: number = 0;
  slidesToggle(index: number) {
    this.activeSlideIndex = index;
  }
}
