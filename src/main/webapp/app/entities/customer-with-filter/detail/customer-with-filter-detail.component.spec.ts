import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomerWithFilterDetailComponent } from './customer-with-filter-detail.component';

describe('CustomerWithFilter Management Detail Component', () => {
  let comp: CustomerWithFilterDetailComponent;
  let fixture: ComponentFixture<CustomerWithFilterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerWithFilterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ customerWithFilter: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CustomerWithFilterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CustomerWithFilterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load customerWithFilter on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.customerWithFilter).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
