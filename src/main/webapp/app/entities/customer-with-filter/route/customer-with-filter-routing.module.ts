import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerWithFilterComponent } from '../list/customer-with-filter.component';
import { CustomerWithFilterDetailComponent } from '../detail/customer-with-filter-detail.component';
import { CustomerWithFilterUpdateComponent } from '../update/customer-with-filter-update.component';
import { CustomerWithFilterRoutingResolveService } from './customer-with-filter-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const customerWithFilterRoute: Routes = [
  {
    path: '',
    component: CustomerWithFilterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerWithFilterDetailComponent,
    resolve: {
      customerWithFilter: CustomerWithFilterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerWithFilterUpdateComponent,
    resolve: {
      customerWithFilter: CustomerWithFilterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerWithFilterUpdateComponent,
    resolve: {
      customerWithFilter: CustomerWithFilterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerWithFilterRoute)],
  exports: [RouterModule],
})
export class CustomerWithFilterRoutingModule {}
