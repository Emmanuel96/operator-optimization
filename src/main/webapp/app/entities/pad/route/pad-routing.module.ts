import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PadComponent } from '../list/pad.component';
import { PadDetailComponent } from '../detail/pad-detail.component';
import { PadUpdateComponent } from '../update/pad-update.component';
import { PadRoutingResolveService } from './pad-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const padRoute: Routes = [
  {
    path: '',
    component: PadComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PadDetailComponent,
    resolve: {
      pad: PadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PadUpdateComponent,
    resolve: {
      pad: PadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PadUpdateComponent,
    resolve: {
      pad: PadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(padRoute)],
  exports: [RouterModule],
})
export class PadRoutingModule {}
