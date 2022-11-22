import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CustomerWithFilterFormService, CustomerWithFilterFormGroup } from './customer-with-filter-form.service';
import { ICustomerWithFilter } from '../customer-with-filter.model';
import { CustomerWithFilterService } from '../service/customer-with-filter.service';

@Component({
  selector: 'jhi-customer-with-filter-update',
  templateUrl: './customer-with-filter-update.component.html',
})
export class CustomerWithFilterUpdateComponent implements OnInit {
  isSaving = false;
  customerWithFilter: ICustomerWithFilter | null = null;

  editForm: CustomerWithFilterFormGroup = this.customerWithFilterFormService.createCustomerWithFilterFormGroup();

  constructor(
    protected customerWithFilterService: CustomerWithFilterService,
    protected customerWithFilterFormService: CustomerWithFilterFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerWithFilter }) => {
      this.customerWithFilter = customerWithFilter;
      if (customerWithFilter) {
        this.updateForm(customerWithFilter);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerWithFilter = this.customerWithFilterFormService.getCustomerWithFilter(this.editForm);
    if (customerWithFilter.id !== null) {
      this.subscribeToSaveResponse(this.customerWithFilterService.update(customerWithFilter));
    } else {
      this.subscribeToSaveResponse(this.customerWithFilterService.create(customerWithFilter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerWithFilter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(customerWithFilter: ICustomerWithFilter): void {
    this.customerWithFilter = customerWithFilter;
    this.customerWithFilterFormService.resetForm(this.editForm, customerWithFilter);
  }
}
