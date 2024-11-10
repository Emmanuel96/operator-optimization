import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOperationalItem, NewOperationalItem } from '../operational-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOperationalItem for edit and NewOperationalItemFormGroupInput for create.
 */
type OperationalItemFormGroupInput = IOperationalItem | PartialWithRequiredKeyOf<NewOperationalItem>;

type OperationalItemFormDefaults = Pick<NewOperationalItem, 'id'>;

type OperationalItemFormGroupContent = {
  id: FormControl<IOperationalItem['id'] | NewOperationalItem['id']>;
  type: FormControl<IOperationalItem['type']>;
  priorityScore: FormControl<IOperationalItem['priorityScore']>;
  dueDate: FormControl<IOperationalItem['dueDate']>;
  location: FormControl<IOperationalItem['location']>;
};

export type OperationalItemFormGroup = FormGroup<OperationalItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OperationalItemFormService {
  createOperationalItemFormGroup(operationalItem: OperationalItemFormGroupInput = { id: null }): OperationalItemFormGroup {
    const operationalItemRawValue = {
      ...this.getFormDefaults(),
      ...operationalItem,
    };
    return new FormGroup<OperationalItemFormGroupContent>({
      id: new FormControl(
        { value: operationalItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      type: new FormControl(operationalItemRawValue.type, {
        validators: [Validators.required],
      }),
      priorityScore: new FormControl(operationalItemRawValue.priorityScore, {
        validators: [Validators.required],
      }),
      dueDate: new FormControl(operationalItemRawValue.dueDate),
      location: new FormControl(operationalItemRawValue.location, {
        validators: [Validators.required],
      }),
    });
  }

  getOperationalItem(form: OperationalItemFormGroup): IOperationalItem | NewOperationalItem {
    return form.getRawValue() as IOperationalItem | NewOperationalItem;
  }

  resetForm(form: OperationalItemFormGroup, operationalItem: OperationalItemFormGroupInput): void {
    const operationalItemRawValue = { ...this.getFormDefaults(), ...operationalItem };
    form.reset(
      {
        ...operationalItemRawValue,
        id: { value: operationalItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OperationalItemFormDefaults {
    return {
      id: null,
    };
  }
}
