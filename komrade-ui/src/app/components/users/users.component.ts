import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { MatPaginator, MatSort, MatTableDataSource, MatDialog } from '@angular/material';
import { UsersService, UserDataModel } from 'src/app/services/users.service';
import { User } from 'src/app/models/user.model';
import { CustomMatTableDataSource } from 'src/app/shared/custom-mat-table-datasource';
import { DEFAULT_PAGE_SIZE, PAGE_SIZE_OPTIONS, FORM_DIALOG_WIDTH, FORM_DIALOG_WIDTH_VW } from 'src/app/constants';
import { UserFormDialogComponent } from './user-form-dialog/user-form-dialog.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.sass']
})
export class UsersComponent implements OnInit, AfterViewInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  public PAGE_SIZE = DEFAULT_PAGE_SIZE;
  public PAGE_SIZE_OPTIONS = PAGE_SIZE_OPTIONS;

  readonly cols = [
    { name: 'name', text: 'Name' },
    { name: 'firstName', text: 'First Name' },
    { name: 'lastName', text: 'Last Name' },
    { name: 'email', text: 'Email' },
    { name: 'isSystem', text: 'System?' },
    { name: 'isDisabled', text: 'Disabled?' },
    { name: 'actions', text: 'Actions' },
  ];

  readonly initColumns$ = new BehaviorSubject([]);
  public columnsToDisplay$ = new BehaviorSubject([]);
  public isLoading$ = new BehaviorSubject<boolean>(false);
  public dataSource$ = new BehaviorSubject(new CustomMatTableDataSource<User>());

  constructor(
    private service: UsersService,
    private dialog: MatDialog,
  ) { }

  ngOnInit() {
    this.getUsers();
  }

  ngAfterViewInit() {
    this.dataSource$.getValue().paginator = this.paginator;
    this.dataSource$.getValue().sort = this.sort;
  }

  getUsers() {
    this.isLoading$.next(true);
    this.service.getUsers$()
      .subscribe((_: UserDataModel) => {
        this.initColumns$.next(this.cols);
        this.columnsToDisplay$.next(this.cols.map(col => col.name));
        this.dataSource$.getValue().data = _.result;
        this.isLoading$.next(false);
      },
        (err) => {
          this.isLoading$.next(false);
        }
      );
  }

  openFormDialog = (user: User) =>
    this.dialog.open(UserFormDialogComponent, {
      data: { user },
      minWidth: FORM_DIALOG_WIDTH,
      width: FORM_DIALOG_WIDTH_VW,
      maxWidth: FORM_DIALOG_WIDTH
    })
      .afterClosed()
      .subscribe(_ => {
        this.getUsers();
      })

}

