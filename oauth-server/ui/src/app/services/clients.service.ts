import { Injectable } from '@angular/core';
import { Client } from '../models/client.model';
import { SERVER_URL } from '../shared/consts';
import { SubResourceService } from './sub-resource.service';
import { HttpClientService } from './http-client.service';

@Injectable({
  providedIn: 'root'
})
export class ClientsService extends SubResourceService<Client> {

  constructor(
    httpBase: HttpClientService) {
    super(
      httpBase
    );
    this.url = `${SERVER_URL}api/v1/clients`;
  }

  updateSecret = (id: number, password: string) =>
    this.updateWithUrl({ password }, `${this.url}/${id}/updatepassword`)
}
