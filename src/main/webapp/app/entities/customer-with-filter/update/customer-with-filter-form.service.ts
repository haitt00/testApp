import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICustomerWithFilter, NewCustomerWithFilter } from '../customer-with-filter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomerWithFilter for edit and NewCustomerWithFilterFormGroupInput for create.
 */
type CustomerWithFilterFormGroupInput = ICustomerWithFilter | PartialWithRequiredKeyOf<NewCustomerWithFilter>;

type CustomerWithFilterFormDefaults = Pick<NewCustomerWithFilter, 'id'>;

type CustomerWithFilterFormGroupContent = {
  id: FormControl<ICustomerWithFilter['id'] | NewCustomerWithFilter['id']>;
  name: FormControl<ICustomerWithFilter['name']>;
  address: FormControl<ICustomerWithFilter['address']>;
  phone: FormControl<ICustomerWithFilter['phone']>;
};

export type CustomerWithFilterFormGroup = FormGroup<CustomerWithFilterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerWithFilterFormService {
  createCustomerWithFilterFormGroup(customerWithFilter: CustomerWithFilterFormGroupInput = { id: null }): CustomerWithFilterFormGroup {
    const customerWithFilterRawValue = {
      ...this.getFormDefaults(),
      ...customerWithFilter,
    };
    return new FormGroup<CustomerWithFilterFormGroupContent>({
      id: new FormControl(
        { value: customerWithFilterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(customerWithFilterRawValue.name),
      address: new FormControl(customerWithFilterRawValue.address),
      phone: new FormControl(customerWithFilterRawValue.phone),
    });
  }

  getCustomerWithFilter(form: CustomerWithFilterFormGroup): ICustomerWithFilter | NewCustomerWithFilter {
    return form.getRawValue() as ICustomerWithFilter | NewCustomerWithFilter;
  }

  resetForm(form: CustomerWithFilterFormGroup, customerWithFilter: CustomerWithFilterFormGroupInput): void {
    const customerWithFilterRawValue = { ...this.getFormDefaults(), ...customerWithFilter };
    form.reset(
      {
        ...customerWithFilterRawValue,
        id: { value: customerWithFilterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CustomerWithFilterFormDefaults {
    return {
      id: null,
    };
  }
}
