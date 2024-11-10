import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../operational-item.test-samples';

import { OperationalItemFormService } from './operational-item-form.service';

describe('OperationalItem Form Service', () => {
  let service: OperationalItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OperationalItemFormService);
  });

  describe('Service methods', () => {
    describe('createOperationalItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOperationalItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            priorityScore: expect.any(Object),
            dueDate: expect.any(Object),
            location: expect.any(Object),
          })
        );
      });

      it('passing IOperationalItem should create a new form with FormGroup', () => {
        const formGroup = service.createOperationalItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            priorityScore: expect.any(Object),
            dueDate: expect.any(Object),
            location: expect.any(Object),
          })
        );
      });
    });

    describe('getOperationalItem', () => {
      it('should return NewOperationalItem for default OperationalItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOperationalItemFormGroup(sampleWithNewData);

        const operationalItem = service.getOperationalItem(formGroup) as any;

        expect(operationalItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewOperationalItem for empty OperationalItem initial value', () => {
        const formGroup = service.createOperationalItemFormGroup();

        const operationalItem = service.getOperationalItem(formGroup) as any;

        expect(operationalItem).toMatchObject({});
      });

      it('should return IOperationalItem', () => {
        const formGroup = service.createOperationalItemFormGroup(sampleWithRequiredData);

        const operationalItem = service.getOperationalItem(formGroup) as any;

        expect(operationalItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOperationalItem should not enable id FormControl', () => {
        const formGroup = service.createOperationalItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOperationalItem should disable id FormControl', () => {
        const formGroup = service.createOperationalItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
