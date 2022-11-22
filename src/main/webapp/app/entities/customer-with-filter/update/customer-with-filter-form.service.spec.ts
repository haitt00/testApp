import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../customer-with-filter.test-samples';

import { CustomerWithFilterFormService } from './customer-with-filter-form.service';

describe('CustomerWithFilter Form Service', () => {
  let service: CustomerWithFilterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomerWithFilterFormService);
  });

  describe('Service methods', () => {
    describe('createCustomerWithFilterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCustomerWithFilterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
            phone: expect.any(Object),
          })
        );
      });

      it('passing ICustomerWithFilter should create a new form with FormGroup', () => {
        const formGroup = service.createCustomerWithFilterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            address: expect.any(Object),
            phone: expect.any(Object),
          })
        );
      });
    });

    describe('getCustomerWithFilter', () => {
      it('should return NewCustomerWithFilter for default CustomerWithFilter initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCustomerWithFilterFormGroup(sampleWithNewData);

        const customerWithFilter = service.getCustomerWithFilter(formGroup) as any;

        expect(customerWithFilter).toMatchObject(sampleWithNewData);
      });

      it('should return NewCustomerWithFilter for empty CustomerWithFilter initial value', () => {
        const formGroup = service.createCustomerWithFilterFormGroup();

        const customerWithFilter = service.getCustomerWithFilter(formGroup) as any;

        expect(customerWithFilter).toMatchObject({});
      });

      it('should return ICustomerWithFilter', () => {
        const formGroup = service.createCustomerWithFilterFormGroup(sampleWithRequiredData);

        const customerWithFilter = service.getCustomerWithFilter(formGroup) as any;

        expect(customerWithFilter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICustomerWithFilter should not enable id FormControl', () => {
        const formGroup = service.createCustomerWithFilterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCustomerWithFilter should disable id FormControl', () => {
        const formGroup = service.createCustomerWithFilterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
