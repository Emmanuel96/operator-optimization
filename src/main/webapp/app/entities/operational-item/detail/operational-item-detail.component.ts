import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperationalItem } from '../operational-item.model';

@Component({
  selector: 'jhi-operational-item-detail',
  templateUrl: './operational-item-detail.component.html',
})
export class OperationalItemDetailComponent implements OnInit {
  operationalItem: IOperationalItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operationalItem }) => {
      this.operationalItem = operationalItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
