import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-pages-hero',
  templateUrl: './pages-hero.component.html',
  styleUrls: ['./pages-hero.component.scss']
})
export class PagesHeroComponent {

  @Input() pageTitle: string = "Shop";
}
