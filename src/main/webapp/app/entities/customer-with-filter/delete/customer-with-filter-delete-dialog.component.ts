import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerWithFilter } from '../customer-with-filter.model';
import { CustomerWithFilterService } from '../service/customer-with-filter.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './customer-with-filter-delete-dialog.component.html',
})
export class CustomerWithFilterDeleteDialogComponent {
  customerWithFilter?: ICustomerWithFilter;

  constructor(protected customerWithFilterService: CustomerWithFilterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerWithFilterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
