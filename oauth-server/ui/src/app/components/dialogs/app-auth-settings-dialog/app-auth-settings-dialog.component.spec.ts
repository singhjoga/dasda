import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppAuthSettingsDialogComponent } from './app-auth-settings-dialog.component';

describe('AppAuthSettingsDialogComponent', () => {
  let component: AppAuthSettingsDialogComponent;
  let fixture: ComponentFixture<AppAuthSettingsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppAuthSettingsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppAuthSettingsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
