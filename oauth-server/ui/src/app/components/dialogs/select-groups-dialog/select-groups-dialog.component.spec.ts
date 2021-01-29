import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectGroupsDialogComponent } from './select-groups-dialog.component';

describe('SelectGroupsDialogComponent', () => {
  let component: SelectGroupsDialogComponent;
  let fixture: ComponentFixture<SelectGroupsDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelectGroupsDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectGroupsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
