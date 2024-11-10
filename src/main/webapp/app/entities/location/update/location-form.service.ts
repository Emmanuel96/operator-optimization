import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILocation, NewLocation } from '../location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocation for edit and NewLocationFormGroupInput for create.
 */
type LocationFormGroupInput = ILocation | PartialWithRequiredKeyOf<NewLocation>;

type LocationFormDefaults = Pick<NewLocation, 'id'>;

type LocationFormGroupContent = {
  id: FormControl<ILocation['id'] | NewLocation['id']>;
  name: FormControl<ILocation['name']>;
  uwi: FormControl<ILocation['uwi']>;
  latitude: FormControl<ILocation['latitude']>;
  longitude: FormControl<ILocation['longitude']>;
  type: FormControl<ILocation['type']>;
  pad: FormControl<ILocation['pad']>;
};

export type LocationFormGroup = FormGroup<LocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocationFormService {
  createLocationFormGroup(location: LocationFormGroupInput = { id: null }): LocationFormGroup {
    const locationRawValue = {
      ...this.getFormDefaults(),
      ...location,
    };
    return new FormGroup<LocationFormGroupContent>({
      id: new FormControl(
        { value: locationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(locationRawValue.name, {
        validators: [Validators.required],
      }),
      uwi: new FormControl(locationRawValue.uwi),
      latitude: new FormControl(locationRawValue.latitude, {
        validators: [Validators.required],
      }),
      longitude: new FormControl(locationRawValue.longitude, {
        validators: [Validators.required],
      }),
      type: new FormControl(locationRawValue.type, {
        validators: [Validators.required],
      }),
      pad: new FormControl(locationRawValue.pad),
    });
  }

  getLocation(form: LocationFormGroup): ILocation | NewLocation {
    return form.getRawValue() as ILocation | NewLocation;
  }

  resetForm(form: LocationFormGroup, location: LocationFormGroupInput): void {
    const locationRawValue = { ...this.getFormDefaults(), ...location };
    form.reset(
      {
        ...locationRawValue,
        id: { value: locationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LocationFormDefaults {
    return {
      id: null,
    };
  }
}
