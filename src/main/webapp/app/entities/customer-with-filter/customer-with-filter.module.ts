import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerWithFilterComponent } from './list/customer-with-filter.component';
import { CustomerWithFilterDetailComponent } from './detail/customer-with-filter-detail.component';
import { CustomerWithFilterUpdateComponent } from './update/customer-with-filter-update.component';
import { CustomerWithFilterDeleteDialogComponent } from './delete/customer-with-filter-delete-dialog.component';
import { CustomerWithFilterRoutingModule } from './route/customer-with-filter-routing.module';

@NgModule({
  imports: [SharedModule, CustomerWithFilterRoutingModule],
  declarations: [
    CustomerWithFilterComponent,
    CustomerWithFilterDetailComponent,
    CustomerWithFilterUpdateComponent,
    CustomerWithFilterDeleteDialogComponent,
  ],
})
export class CustomerWithFilterModule {}
