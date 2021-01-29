import { Component, OnInit } from '@angular/core';
import { GroupService } from 'src/app/services/group.service';
import { DynamicDialogRef, DynamicDialogConfig, ConfirmationService } from 'primeng/api';
import { Group } from 'src/app/models/group.model';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-select-groups-dialog',
  templateUrl: './select-groups-dialog.component.html',
  styleUrls: ['./select-groups-dialog.component.sass']
})
export class SelectGroupsDialogComponent implements OnInit {
  constructor(
    private groupService: GroupService,
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private confirmationService: ConfirmationService,
  ) { }

  oldSelectedGroups: Group[];
  newSelectedGroups: Group[] = [];
  groups$ = new BehaviorSubject([]);

  ngOnInit() {
    this.oldSelectedGroups = this.config.data.selectedGroups ? this.config.data.selectedGroups : [];

    const selectedIds = [];
    this.oldSelectedGroups.forEach(element => {
      selectedIds.push(element.id);
    });

    this.groupService.getAll()
      .subscribe((_: Group[]) => {
        this.groups$.next(_.filter(ii => !selectedIds.includes(ii.id)));
      });
  }

  selectGroup(group: Group) {
    this.ref.close(group);
  }

  onChange(event, group) {
    if (event) {
      this.newSelectedGroups.push(group);
    } else {
      const index = this.newSelectedGroups.indexOf(group);
      this.newSelectedGroups.splice(index, 1);
    }
  }

  addSelected() {
    this.confirmationService.confirm({
      message: 'Are you sure to remove these groups?',
      accept: () => {
        this.ref.close(this.newSelectedGroups);
      }
    });
  }

}
