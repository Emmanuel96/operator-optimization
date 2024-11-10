import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PadFormService, PadFormGroup } from './pad-form.service';
import { IPad } from '../pad.model';
import { PadService } from '../service/pad.service';

@Component({
  selector: 'jhi-pad-update',
  templateUrl: './pad-update.component.html',
})
export class PadUpdateComponent implements OnInit {
  isSaving = false;
  pad: IPad | null = null;

  editForm: PadFormGroup = this.padFormService.createPadFormGroup();

  constructor(protected padService: PadService, protected padFormService: PadFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pad }) => {
      this.pad = pad;
      if (pad) {
        this.updateForm(pad);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pad = this.padFormService.getPad(this.editForm);
    if (pad.id !== null) {
      this.subscribeToSaveResponse(this.padService.update(pad));
    } else {
      this.subscribeToSaveResponse(this.padService.create(pad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPad>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pad: IPad): void {
    this.pad = pad;
    this.padFormService.resetForm(this.editForm, pad);
  }
}
