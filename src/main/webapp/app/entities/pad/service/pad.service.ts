import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPad, NewPad } from '../pad.model';

export type PartialUpdatePad = Partial<IPad> & Pick<IPad, 'id'>;

export type EntityResponseType = HttpResponse<IPad>;
export type EntityArrayResponseType = HttpResponse<IPad[]>;

@Injectable({ providedIn: 'root' })
export class PadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pad: NewPad): Observable<EntityResponseType> {
    return this.http.post<IPad>(this.resourceUrl, pad, { observe: 'response' });
  }

  update(pad: IPad): Observable<EntityResponseType> {
    return this.http.put<IPad>(`${this.resourceUrl}/${this.getPadIdentifier(pad)}`, pad, { observe: 'response' });
  }

  partialUpdate(pad: PartialUpdatePad): Observable<EntityResponseType> {
    return this.http.patch<IPad>(`${this.resourceUrl}/${this.getPadIdentifier(pad)}`, pad, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPadIdentifier(pad: Pick<IPad, 'id'>): number {
    return pad.id;
  }

  comparePad(o1: Pick<IPad, 'id'> | null, o2: Pick<IPad, 'id'> | null): boolean {
    return o1 && o2 ? this.getPadIdentifier(o1) === this.getPadIdentifier(o2) : o1 === o2;
  }

  addPadToCollectionIfMissing<Type extends Pick<IPad, 'id'>>(padCollection: Type[], ...padsToCheck: (Type | null | undefined)[]): Type[] {
    const pads: Type[] = padsToCheck.filter(isPresent);
    if (pads.length > 0) {
      const padCollectionIdentifiers = padCollection.map(padItem => this.getPadIdentifier(padItem)!);
      const padsToAdd = pads.filter(padItem => {
        const padIdentifier = this.getPadIdentifier(padItem);
        if (padCollectionIdentifiers.includes(padIdentifier)) {
          return false;
        }
        padCollectionIdentifiers.push(padIdentifier);
        return true;
      });
      return [...padsToAdd, ...padCollection];
    }
    return padCollection;
  }
}
