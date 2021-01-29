import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/components/common/menuitem';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'Harbois O-Auth UI';
  private items: MenuItem[];

  ngOnInit() {
    this.items = [{
      label: 'File',
      items: [
        { label: 'New', icon: 'fa fa-plus' },
        { label: 'Open', icon: 'fa fa-download' }
      ]
    },
    {
      label: 'Edit',
      items: [
        { label: 'Undo', icon: 'fa fa-refresh' },
        { label: 'Redo', icon: 'fa fa-repeat' }
      ]
    }];
  }

}
