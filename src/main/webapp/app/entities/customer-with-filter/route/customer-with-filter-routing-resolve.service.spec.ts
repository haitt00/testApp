import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICustomerWithFilter } from '../customer-with-filter.model';
import { CustomerWithFilterService } from '../service/customer-with-filter.service';

import { CustomerWithFilterRoutingResolveService } from './customer-with-filter-routing-resolve.service';

describe('CustomerWithFilter routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CustomerWithFilterRoutingResolveService;
  let service: CustomerWithFilterService;
  let resultCustomerWithFilter: ICustomerWithFilter | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CustomerWithFilterRoutingResolveService);
    service = TestBed.inject(CustomerWithFilterService);
    resultCustomerWithFilter = undefined;
  });

  describe('resolve', () => {
    it('should return ICustomerWithFilter returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerWithFilter = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCustomerWithFilter).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerWithFilter = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCustomerWithFilter).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ICustomerWithFilter>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCustomerWithFilter = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCustomerWithFilter).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
