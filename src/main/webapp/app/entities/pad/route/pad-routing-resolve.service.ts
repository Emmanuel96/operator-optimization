import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPad } from '../pad.model';
import { PadService } from '../service/pad.service';

@Injectable({ providedIn: 'root' })
export class PadRoutingResolveService implements Resolve<IPad | null> {
  constructor(protected service: PadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPad | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pad: HttpResponse<IPad>) => {
          if (pad.body) {
            return of(pad.body);
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
