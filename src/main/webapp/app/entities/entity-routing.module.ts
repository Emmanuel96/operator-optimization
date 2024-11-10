import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pad',
        data: { pageTitle: 'operatorOptimisationApp.pad.home.title' },
        loadChildren: () => import('./pad/pad.module').then(m => m.PadModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'operatorOptimisationApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'operational-item',
        data: { pageTitle: 'operatorOptimisationApp.operationalItem.home.title' },
        loadChildren: () => import('./operational-item/operational-item.module').then(m => m.OperationalItemModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
