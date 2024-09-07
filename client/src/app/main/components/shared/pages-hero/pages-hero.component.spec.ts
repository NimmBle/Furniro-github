import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagesHeroComponent } from './pages-hero.component';

describe('PagesHeroComponent', () => {
  let component: PagesHeroComponent;
  let fixture: ComponentFixture<PagesHeroComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PagesHeroComponent]
    });
    fixture = TestBed.createComponent(PagesHeroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
