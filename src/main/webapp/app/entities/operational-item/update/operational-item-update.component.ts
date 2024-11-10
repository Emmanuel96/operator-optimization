import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OperationalItemFormService, OperationalItemFormGroup } from './operational-item-form.service';
import { IOperationalItem } from '../operational-item.model';
import { OperationalItemService } from '../service/operational-item.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { OperationalItemTypes } from 'app/entities/enumerations/operational-item-types.model';

@Component({
  selector: 'jhi-operational-item-update',
  templateUrl: './operational-item-update.component.html',
})
export class OperationalItemUpdateComponent implements OnInit {
  isSaving = false;
  operationalItem: IOperationalItem | null = null;
  operationalItemTypesValues = Object.keys(OperationalItemTypes);

  locationsSharedCollection: ILocation[] = [];

  editForm: OperationalItemFormGroup = this.operationalItemFormService.createOperationalItemFormGroup();

  constructor(
    protected operationalItemService: OperationalItemService,
    protected operationalItemFormService: OperationalItemFormService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operationalItem }) => {
      this.operationalItem = operationalItem;
      if (operationalItem) {
        this.updateForm(operationalItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operationalItem = this.operationalItemFormService.getOperationalItem(this.editForm);
    if (operationalItem.id !== null) {
      this.subscribeToSaveResponse(this.operationalItemService.update(operationalItem));
    } else {
      this.subscribeToSaveResponse(this.operationalItemService.create(operationalItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperationalItem>>): void {
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

  protected updateForm(operationalItem: IOperationalItem): void {
    this.operationalItem = operationalItem;
    this.operationalItemFormService.resetForm(this.editForm, operationalItem);

    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsSharedCollection,
      operationalItem.location
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.operationalItem?.location)
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }
}
