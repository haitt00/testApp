import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerWithFilter } from '../customer-with-filter.model';

@Component({
  selector: 'jhi-customer-with-filter-detail',
  templateUrl: './customer-with-filter-detail.component.html',
})
export class CustomerWithFilterDetailComponent implements OnInit {
  customerWithFilter: ICustomerWithFilter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerWithFilter }) => {
      this.customerWithFilter = customerWithFilter;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
