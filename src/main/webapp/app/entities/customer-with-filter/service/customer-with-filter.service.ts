import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerWithFilter, NewCustomerWithFilter } from '../customer-with-filter.model';

export type PartialUpdateCustomerWithFilter = Partial<ICustomerWithFilter> & Pick<ICustomerWithFilter, 'id'>;

export type EntityResponseType = HttpResponse<ICustomerWithFilter>;
export type EntityArrayResponseType = HttpResponse<ICustomerWithFilter[]>;

@Injectable({ providedIn: 'root' })
export class CustomerWithFilterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-with-filters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerWithFilter: NewCustomerWithFilter): Observable<EntityResponseType> {
    return this.http.post<ICustomerWithFilter>(this.resourceUrl, customerWithFilter, { observe: 'response' });
  }

  update(customerWithFilter: ICustomerWithFilter): Observable<EntityResponseType> {
    return this.http.put<ICustomerWithFilter>(
      `${this.resourceUrl}/${this.getCustomerWithFilterIdentifier(customerWithFilter)}`,
      customerWithFilter,
      { observe: 'response' }
    );
  }

  partialUpdate(customerWithFilter: PartialUpdateCustomerWithFilter): Observable<EntityResponseType> {
    return this.http.patch<ICustomerWithFilter>(
      `${this.resourceUrl}/${this.getCustomerWithFilterIdentifier(customerWithFilter)}`,
      customerWithFilter,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICustomerWithFilter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICustomerWithFilter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCustomerWithFilterIdentifier(customerWithFilter: Pick<ICustomerWithFilter, 'id'>): number {
    return customerWithFilter.id;
  }

  compareCustomerWithFilter(o1: Pick<ICustomerWithFilter, 'id'> | null, o2: Pick<ICustomerWithFilter, 'id'> | null): boolean {
    return o1 && o2 ? this.getCustomerWithFilterIdentifier(o1) === this.getCustomerWithFilterIdentifier(o2) : o1 === o2;
  }

  addCustomerWithFilterToCollectionIfMissing<Type extends Pick<ICustomerWithFilter, 'id'>>(
    customerWithFilterCollection: Type[],
    ...customerWithFiltersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const customerWithFilters: Type[] = customerWithFiltersToCheck.filter(isPresent);
    if (customerWithFilters.length > 0) {
      const customerWithFilterCollectionIdentifiers = customerWithFilterCollection.map(
        customerWithFilterItem => this.getCustomerWithFilterIdentifier(customerWithFilterItem)!
      );
      const customerWithFiltersToAdd = customerWithFilters.filter(customerWithFilterItem => {
        const customerWithFilterIdentifier = this.getCustomerWithFilterIdentifier(customerWithFilterItem);
        if (customerWithFilterCollectionIdentifiers.includes(customerWithFilterIdentifier)) {
          return false;
        }
        customerWithFilterCollectionIdentifiers.push(customerWithFilterIdentifier);
        return true;
      });
      return [...customerWithFiltersToAdd, ...customerWithFilterCollection];
    }
    return customerWithFilterCollection;
  }
}
