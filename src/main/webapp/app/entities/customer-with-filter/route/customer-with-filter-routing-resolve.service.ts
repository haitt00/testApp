import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerWithFilter } from '../customer-with-filter.model';
import { CustomerWithFilterService } from '../service/customer-with-filter.service';

@Injectable({ providedIn: 'root' })
export class CustomerWithFilterRoutingResolveService implements Resolve<ICustomerWithFilter | null> {
  constructor(protected service: CustomerWithFilterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerWithFilter | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerWithFilter: HttpResponse<ICustomerWithFilter>) => {
          if (customerWithFilter.body) {
            return of(customerWithFilter.body);
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
