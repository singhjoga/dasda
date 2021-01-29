import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppAuthSettingsComponent } from './app-auth-settings.component';

describe('AppAuthSettingsComponent', () => {
  let component: AppAuthSettingsComponent;
  let fixture: ComponentFixture<AppAuthSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppAuthSettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppAuthSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
