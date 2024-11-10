import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OperationalItemComponent } from '../list/operational-item.component';
import { OperationalItemDetailComponent } from '../detail/operational-item-detail.component';
import { OperationalItemUpdateComponent } from '../update/operational-item-update.component';
import { OperationalItemRoutingResolveService } from './operational-item-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const operationalItemRoute: Routes = [
  {
    path: '',
    component: OperationalItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperationalItemDetailComponent,
    resolve: {
      operationalItem: OperationalItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperationalItemUpdateComponent,
    resolve: {
      operationalItem: OperationalItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperationalItemUpdateComponent,
    resolve: {
      operationalItem: OperationalItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(operationalItemRoute)],
  exports: [RouterModule],
})
export class OperationalItemRoutingModule {}
