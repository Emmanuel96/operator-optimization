import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pad.test-samples';

import { PadFormService } from './pad-form.service';

describe('Pad Form Service', () => {
  let service: PadFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PadFormService);
  });

  describe('Service methods', () => {
    describe('createPadFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPadFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IPad should create a new form with FormGroup', () => {
        const formGroup = service.createPadFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getPad', () => {
      it('should return NewPad for default Pad initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPadFormGroup(sampleWithNewData);

        const pad = service.getPad(formGroup) as any;

        expect(pad).toMatchObject(sampleWithNewData);
      });

      it('should return NewPad for empty Pad initial value', () => {
        const formGroup = service.createPadFormGroup();

        const pad = service.getPad(formGroup) as any;

        expect(pad).toMatchObject({});
      });

      it('should return IPad', () => {
        const formGroup = service.createPadFormGroup(sampleWithRequiredData);

        const pad = service.getPad(formGroup) as any;

        expect(pad).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPad should not enable id FormControl', () => {
        const formGroup = service.createPadFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPad should disable id FormControl', () => {
        const formGroup = service.createPadFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
