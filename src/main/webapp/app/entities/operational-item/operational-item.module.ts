import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OperationalItemComponent } from './list/operational-item.component';
import { OperationalItemDetailComponent } from './detail/operational-item-detail.component';
import { OperationalItemUpdateComponent } from './update/operational-item-update.component';
import { OperationalItemDeleteDialogComponent } from './delete/operational-item-delete-dialog.component';
import { OperationalItemRoutingModule } from './route/operational-item-routing.module';

@NgModule({
  imports: [SharedModule, OperationalItemRoutingModule],
  declarations: [
    OperationalItemComponent,
    OperationalItemDetailComponent,
    OperationalItemUpdateComponent,
    OperationalItemDeleteDialogComponent,
  ],
})
export class OperationalItemModule {}
