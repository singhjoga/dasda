import { Component, OnInit } from '@angular/core';
import { UsersService } from 'src/app/services/users.service';
import { DynamicDialogRef, DynamicDialogConfig, ConfirmationService } from 'primeng/api';
import { User } from 'src/app/models/user.model';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-select-users-dialog',
  templateUrl: './select-users-dialog.component.html',
  styleUrls: ['./select-users-dialog.component.sass']
})
export class SelectUsersDialogComponent implements OnInit {
  constructor(
    private userService: UsersService,
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private confirmationService: ConfirmationService,
  ) { }

  oldSelectedUsers: User[];
  newSelectedUsers: User[] = [];
  users$ = new BehaviorSubject([]);

  ngOnInit() {
    this.oldSelectedUsers = this.config.data.selectedUsers ? this.config.data.selectedUsers : [];

    const selectedIds = [];
    this.oldSelectedUsers.forEach(element => {
      selectedIds.push(element.id);
    });

    this.userService.getAll()
      .subscribe((_: User[]) => {
        this.users$.next(_.filter(ii => !selectedIds.includes(ii.id)));
      });
  }

  selectUser(user: User) {
    this.ref.close(user);
  }

  onChange(event, user) {
    if (event) {
      this.newSelectedUsers.push(user);
    } else {
      const index = this.newSelectedUsers.indexOf(user);
      this.newSelectedUsers.splice(index, 1);
    }
  }

  addSelected() {
    this.confirmationService.confirm({
      message: 'Are you sure to remove these users?',
      accept: () => {
        this.ref.close(this.newSelectedUsers);
      }
    });
  }
}
