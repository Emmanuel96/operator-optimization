import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperationalItem } from '../operational-item.model';
import { OperationalItemService } from '../service/operational-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './operational-item-delete-dialog.component.html',
})
export class OperationalItemDeleteDialogComponent {
  operationalItem?: IOperationalItem;

  constructor(protected operationalItemService: OperationalItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operationalItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
