import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { LocationFormService, LocationFormGroup } from './location-form.service';
import { ILocation } from '../location.model';
import { LocationService } from '../service/location.service';
import { IPad } from 'app/entities/pad/pad.model';
import { PadService } from 'app/entities/pad/service/pad.service';
import { LocationType } from 'app/entities/enumerations/location-type.model';

@Component({
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html',
})
export class LocationUpdateComponent implements OnInit {
  isSaving = false;
  location: ILocation | null = null;
  locationTypeValues = Object.keys(LocationType);

  padsSharedCollection: IPad[] = [];

  editForm: LocationFormGroup = this.locationFormService.createLocationFormGroup();

  constructor(
    protected locationService: LocationService,
    protected locationFormService: LocationFormService,
    protected padService: PadService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePad = (o1: IPad | null, o2: IPad | null): boolean => this.padService.comparePad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      this.location = location;
      if (location) {
        this.updateForm(location);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const location = this.locationFormService.getLocation(this.editForm);
    if (location.id !== null) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>): void {
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

  protected updateForm(location: ILocation): void {
    this.location = location;
    this.locationFormService.resetForm(this.editForm, location);

    this.padsSharedCollection = this.padService.addPadToCollectionIfMissing<IPad>(this.padsSharedCollection, location.pad);
  }

  protected loadRelationshipsOptions(): void {
    this.padService
      .query()
      .pipe(map((res: HttpResponse<IPad[]>) => res.body ?? []))
      .pipe(map((pads: IPad[]) => this.padService.addPadToCollectionIfMissing<IPad>(pads, this.location?.pad)))
      .subscribe((pads: IPad[]) => (this.padsSharedCollection = pads));
  }
}
