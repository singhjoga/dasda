import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectRolesDialogComponent } from './select-roles-dialog.component';

describe('SelectRolesDialogComponent', () => {
  let component: SelectRolesDialogComponent;
  let fixture: ComponentFixture<SelectRolesDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectRolesDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectRolesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
