import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerDashboardProfileComponent } from './seller-dashboard-profile.component';

describe('SellerDashboardProfileComponent', () => {
  let component: SellerDashboardProfileComponent;
  let fixture: ComponentFixture<SellerDashboardProfileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SellerDashboardProfileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SellerDashboardProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
