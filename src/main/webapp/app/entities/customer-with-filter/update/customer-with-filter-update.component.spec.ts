import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CustomerWithFilterFormService } from './customer-with-filter-form.service';
import { CustomerWithFilterService } from '../service/customer-with-filter.service';
import { ICustomerWithFilter } from '../customer-with-filter.model';

import { CustomerWithFilterUpdateComponent } from './customer-with-filter-update.component';

describe('CustomerWithFilter Management Update Component', () => {
  let comp: CustomerWithFilterUpdateComponent;
  let fixture: ComponentFixture<CustomerWithFilterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerWithFilterFormService: CustomerWithFilterFormService;
  let customerWithFilterService: CustomerWithFilterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CustomerWithFilterUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CustomerWithFilterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerWithFilterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerWithFilterFormService = TestBed.inject(CustomerWithFilterFormService);
    customerWithFilterService = TestBed.inject(CustomerWithFilterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customerWithFilter: ICustomerWithFilter = { id: 456 };

      activatedRoute.data = of({ customerWithFilter });
      comp.ngOnInit();

      expect(comp.customerWithFilter).toEqual(customerWithFilter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerWithFilter>>();
      const customerWithFilter = { id: 123 };
      jest.spyOn(customerWithFilterFormService, 'getCustomerWithFilter').mockReturnValue(customerWithFilter);
      jest.spyOn(customerWithFilterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerWithFilter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerWithFilter }));
      saveSubject.complete();

      // THEN
      expect(customerWithFilterFormService.getCustomerWithFilter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerWithFilterService.update).toHaveBeenCalledWith(expect.objectContaining(customerWithFilter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerWithFilter>>();
      const customerWithFilter = { id: 123 };
      jest.spyOn(customerWithFilterFormService, 'getCustomerWithFilter').mockReturnValue({ id: null });
      jest.spyOn(customerWithFilterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerWithFilter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerWithFilter }));
      saveSubject.complete();

      // THEN
      expect(customerWithFilterFormService.getCustomerWithFilter).toHaveBeenCalled();
      expect(customerWithFilterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerWithFilter>>();
      const customerWithFilter = { id: 123 };
      jest.spyOn(customerWithFilterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerWithFilter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerWithFilterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
