import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPad } from '../pad.model';

@Component({
  selector: 'jhi-pad-detail',
  templateUrl: './pad-detail.component.html',
})
export class PadDetailComponent implements OnInit {
  pad: IPad | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pad }) => {
      this.pad = pad;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
