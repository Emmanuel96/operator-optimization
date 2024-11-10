import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperationalItem, NewOperationalItem } from '../operational-item.model';

export type PartialUpdateOperationalItem = Partial<IOperationalItem> & Pick<IOperationalItem, 'id'>;

type RestOf<T extends IOperationalItem | NewOperationalItem> = Omit<T, 'dueDate'> & {
  dueDate?: string | null;
};

export type RestOperationalItem = RestOf<IOperationalItem>;

export type NewRestOperationalItem = RestOf<NewOperationalItem>;

export type PartialUpdateRestOperationalItem = RestOf<PartialUpdateOperationalItem>;

export type EntityResponseType = HttpResponse<IOperationalItem>;
export type EntityArrayResponseType = HttpResponse<IOperationalItem[]>;

@Injectable({ providedIn: 'root' })
export class OperationalItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operational-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operationalItem: NewOperationalItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operationalItem);
    return this.http
      .post<RestOperationalItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(operationalItem: IOperationalItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operationalItem);
    return this.http
      .put<RestOperationalItem>(`${this.resourceUrl}/${this.getOperationalItemIdentifier(operationalItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(operationalItem: PartialUpdateOperationalItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(operationalItem);
    return this.http
      .patch<RestOperationalItem>(`${this.resourceUrl}/${this.getOperationalItemIdentifier(operationalItem)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOperationalItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOperationalItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOperationalItemIdentifier(operationalItem: Pick<IOperationalItem, 'id'>): number {
    return operationalItem.id;
  }

  compareOperationalItem(o1: Pick<IOperationalItem, 'id'> | null, o2: Pick<IOperationalItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperationalItemIdentifier(o1) === this.getOperationalItemIdentifier(o2) : o1 === o2;
  }

  addOperationalItemToCollectionIfMissing<Type extends Pick<IOperationalItem, 'id'>>(
    operationalItemCollection: Type[],
    ...operationalItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operationalItems: Type[] = operationalItemsToCheck.filter(isPresent);
    if (operationalItems.length > 0) {
      const operationalItemCollectionIdentifiers = operationalItemCollection.map(
        operationalItemItem => this.getOperationalItemIdentifier(operationalItemItem)!
      );
      const operationalItemsToAdd = operationalItems.filter(operationalItemItem => {
        const operationalItemIdentifier = this.getOperationalItemIdentifier(operationalItemItem);
        if (operationalItemCollectionIdentifiers.includes(operationalItemIdentifier)) {
          return false;
        }
        operationalItemCollectionIdentifiers.push(operationalItemIdentifier);
        return true;
      });
      return [...operationalItemsToAdd, ...operationalItemCollection];
    }
    return operationalItemCollection;
  }

  protected convertDateFromClient<T extends IOperationalItem | NewOperationalItem | PartialUpdateOperationalItem>(
    operationalItem: T
  ): RestOf<T> {
    return {
      ...operationalItem,
      dueDate: operationalItem.dueDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restOperationalItem: RestOperationalItem): IOperationalItem {
    return {
      ...restOperationalItem,
      dueDate: restOperationalItem.dueDate ? dayjs(restOperationalItem.dueDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOperationalItem>): HttpResponse<IOperationalItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOperationalItem[]>): HttpResponse<IOperationalItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
