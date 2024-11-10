import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPad, NewPad } from '../pad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPad for edit and NewPadFormGroupInput for create.
 */
type PadFormGroupInput = IPad | PartialWithRequiredKeyOf<NewPad>;

type PadFormDefaults = Pick<NewPad, 'id'>;

type PadFormGroupContent = {
  id: FormControl<IPad['id'] | NewPad['id']>;
  name: FormControl<IPad['name']>;
};

export type PadFormGroup = FormGroup<PadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PadFormService {
  createPadFormGroup(pad: PadFormGroupInput = { id: null }): PadFormGroup {
    const padRawValue = {
      ...this.getFormDefaults(),
      ...pad,
    };
    return new FormGroup<PadFormGroupContent>({
      id: new FormControl(
        { value: padRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(padRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getPad(form: PadFormGroup): IPad | NewPad {
    return form.getRawValue() as IPad | NewPad;
  }

  resetForm(form: PadFormGroup, pad: PadFormGroupInput): void {
    const padRawValue = { ...this.getFormDefaults(), ...pad };
    form.reset(
      {
        ...padRawValue,
        id: { value: padRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PadFormDefaults {
    return {
      id: null,
    };
  }
}
