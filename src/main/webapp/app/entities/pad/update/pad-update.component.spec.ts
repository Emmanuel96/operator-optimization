import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PadFormService } from './pad-form.service';
import { PadService } from '../service/pad.service';
import { IPad } from '../pad.model';

import { PadUpdateComponent } from './pad-update.component';

describe('Pad Management Update Component', () => {
  let comp: PadUpdateComponent;
  let fixture: ComponentFixture<PadUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let padFormService: PadFormService;
  let padService: PadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PadUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PadUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PadUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    padFormService = TestBed.inject(PadFormService);
    padService = TestBed.inject(PadService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pad: IPad = { id: 456 };

      activatedRoute.data = of({ pad });
      comp.ngOnInit();

      expect(comp.pad).toEqual(pad);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPad>>();
      const pad = { id: 123 };
      jest.spyOn(padFormService, 'getPad').mockReturnValue(pad);
      jest.spyOn(padService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pad }));
      saveSubject.complete();

      // THEN
      expect(padFormService.getPad).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(padService.update).toHaveBeenCalledWith(expect.objectContaining(pad));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPad>>();
      const pad = { id: 123 };
      jest.spyOn(padFormService, 'getPad').mockReturnValue({ id: null });
      jest.spyOn(padService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pad: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pad }));
      saveSubject.complete();

      // THEN
      expect(padFormService.getPad).toHaveBeenCalled();
      expect(padService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPad>>();
      const pad = { id: 123 };
      jest.spyOn(padService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pad });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(padService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
