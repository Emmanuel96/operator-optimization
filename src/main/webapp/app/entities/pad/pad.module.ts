import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PadComponent } from './list/pad.component';
import { PadDetailComponent } from './detail/pad-detail.component';
import { PadUpdateComponent } from './update/pad-update.component';
import { PadDeleteDialogComponent } from './delete/pad-delete-dialog.component';
import { PadRoutingModule } from './route/pad-routing.module';

@NgModule({
  imports: [SharedModule, PadRoutingModule],
  declarations: [PadComponent, PadDetailComponent, PadUpdateComponent, PadDeleteDialogComponent],
})
export class PadModule {}
