import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';
import { AppAuthSetting } from '../models/app-auth-setting.model';
import { SERVER_URL } from '../shared/consts';
import { SubResourceService } from './sub-resource.service';
import { HttpClientService } from './http-client.service';

@Injectable({
  providedIn: 'root'
})
export class AppAuthSettingService extends SubResourceService<AppAuthSetting> {

  constructor(
    httpBase: HttpClientService
  ) {
    super(httpBase);
    this.url = `${SERVER_URL}api/v1/authsettings`;
  }
}
