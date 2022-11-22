import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICustomerWithFilter } from '../customer-with-filter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../customer-with-filter.test-samples';

import { CustomerWithFilterService } from './customer-with-filter.service';

const requireRestSample: ICustomerWithFilter = {
  ...sampleWithRequiredData,
};

describe('CustomerWithFilter Service', () => {
  let service: CustomerWithFilterService;
  let httpMock: HttpTestingController;
  let expectedResult: ICustomerWithFilter | ICustomerWithFilter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerWithFilterService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CustomerWithFilter', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const customerWithFilter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(customerWithFilter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerWithFilter', () => {
      const customerWithFilter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(customerWithFilter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerWithFilter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerWithFilter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CustomerWithFilter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCustomerWithFilterToCollectionIfMissing', () => {
      it('should add a CustomerWithFilter to an empty array', () => {
        const customerWithFilter: ICustomerWithFilter = sampleWithRequiredData;
        expectedResult = service.addCustomerWithFilterToCollectionIfMissing([], customerWithFilter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerWithFilter);
      });

      it('should not add a CustomerWithFilter to an array that contains it', () => {
        const customerWithFilter: ICustomerWithFilter = sampleWithRequiredData;
        const customerWithFilterCollection: ICustomerWithFilter[] = [
          {
            ...customerWithFilter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCustomerWithFilterToCollectionIfMissing(customerWithFilterCollection, customerWithFilter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerWithFilter to an array that doesn't contain it", () => {
        const customerWithFilter: ICustomerWithFilter = sampleWithRequiredData;
        const customerWithFilterCollection: ICustomerWithFilter[] = [sampleWithPartialData];
        expectedResult = service.addCustomerWithFilterToCollectionIfMissing(customerWithFilterCollection, customerWithFilter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerWithFilter);
      });

      it('should add only unique CustomerWithFilter to an array', () => {
        const customerWithFilterArray: ICustomerWithFilter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const customerWithFilterCollection: ICustomerWithFilter[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerWithFilterToCollectionIfMissing(customerWithFilterCollection, ...customerWithFilterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerWithFilter: ICustomerWithFilter = sampleWithRequiredData;
        const customerWithFilter2: ICustomerWithFilter = sampleWithPartialData;
        expectedResult = service.addCustomerWithFilterToCollectionIfMissing([], customerWithFilter, customerWithFilter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerWithFilter);
        expect(expectedResult).toContain(customerWithFilter2);
      });

      it('should accept null and undefined values', () => {
        const customerWithFilter: ICustomerWithFilter = sampleWithRequiredData;
        expectedResult = service.addCustomerWithFilterToCollectionIfMissing([], null, customerWithFilter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerWithFilter);
      });

      it('should return initial array if no CustomerWithFilter is added', () => {
        const customerWithFilterCollection: ICustomerWithFilter[] = [sampleWithRequiredData];
        expectedResult = service.addCustomerWithFilterToCollectionIfMissing(customerWithFilterCollection, undefined, null);
        expect(expectedResult).toEqual(customerWithFilterCollection);
      });
    });

    describe('compareCustomerWithFilter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCustomerWithFilter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCustomerWithFilter(entity1, entity2);
        const compareResult2 = service.compareCustomerWithFilter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCustomerWithFilter(entity1, entity2);
        const compareResult2 = service.compareCustomerWithFilter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCustomerWithFilter(entity1, entity2);
        const compareResult2 = service.compareCustomerWithFilter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
