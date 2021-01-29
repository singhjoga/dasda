import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Resource } from '../models/resource.model';
import { HttpClientService } from './http-client.service';

@Injectable({
  providedIn: 'root'
})
export class SubResourceService<T extends Resource> {
  public url: string;
  constructor(
    private httpClientService: HttpClientService) { }

  public create(item: T): Observable<T> {
    return this.createWithUrl(item, `${this.url}`);
  }

  public createWithUrl<C>(item: C, url: string) {
    return this.httpClientService.post(item, url);
  }

  public update(item: T): Observable<T> {
    const id = item.id;
    delete item.id;
    return this.updateWithUrl(item, `${this.url}/${id}`);
  }

  public updateWithUrl<C>(item: C, url: string): Observable<C> {
    return this.httpClientService.patch(item, url);
  }

  getOne(id: number): Observable<T> {
    return this.getOneWithUrl(id, `${this.url}/${id}`);
  }

  getOneWithUrl<C>(id: number, url: string): Observable<C> {
    return this.httpClientService.getById(id, url);
  }

  getAll(): Observable<T[]> {
    return this.list(`${this.url}`);
  }

  list<C>(url: string): Observable<C[]> {
    return this.httpClientService.get(url);
  }

  delete(id: string) {
    return this.deleteWithUrl(`${this.url}/${id}`);
  }

  deleteWithUrl(url: string) {
    return this.httpClientService.detele(url);
  }
}
