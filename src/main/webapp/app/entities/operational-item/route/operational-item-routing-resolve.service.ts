import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperationalItem } from '../operational-item.model';
import { OperationalItemService } from '../service/operational-item.service';

@Injectable({ providedIn: 'root' })
export class OperationalItemRoutingResolveService implements Resolve<IOperationalItem | null> {
  constructor(protected service: OperationalItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperationalItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operationalItem: HttpResponse<IOperationalItem>) => {
          if (operationalItem.body) {
            return of(operationalItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
