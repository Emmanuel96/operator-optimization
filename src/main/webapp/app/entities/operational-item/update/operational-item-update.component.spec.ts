import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperationalItemFormService } from './operational-item-form.service';
import { OperationalItemService } from '../service/operational-item.service';
import { IOperationalItem } from '../operational-item.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

import { OperationalItemUpdateComponent } from './operational-item-update.component';

describe('OperationalItem Management Update Component', () => {
  let comp: OperationalItemUpdateComponent;
  let fixture: ComponentFixture<OperationalItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operationalItemFormService: OperationalItemFormService;
  let operationalItemService: OperationalItemService;
  let locationService: LocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperationalItemUpdateComponent],
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
      .overrideTemplate(OperationalItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationalItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operationalItemFormService = TestBed.inject(OperationalItemFormService);
    operationalItemService = TestBed.inject(OperationalItemService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Location query and add missing value', () => {
      const operationalItem: IOperationalItem = { id: 456 };
      const location: ILocation = { id: 1325 };
      operationalItem.location = location;

      const locationCollection: ILocation[] = [{ id: 79002 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [location];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operationalItem });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(
        locationCollection,
        ...additionalLocations.map(expect.objectContaining)
      );
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const operationalItem: IOperationalItem = { id: 456 };
      const location: ILocation = { id: 20493 };
      operationalItem.location = location;

      activatedRoute.data = of({ operationalItem });
      comp.ngOnInit();

      expect(comp.locationsSharedCollection).toContain(location);
      expect(comp.operationalItem).toEqual(operationalItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperationalItem>>();
      const operationalItem = { id: 123 };
      jest.spyOn(operationalItemFormService, 'getOperationalItem').mockReturnValue(operationalItem);
      jest.spyOn(operationalItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operationalItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operationalItem }));
      saveSubject.complete();

      // THEN
      expect(operationalItemFormService.getOperationalItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(operationalItemService.update).toHaveBeenCalledWith(expect.objectContaining(operationalItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperationalItem>>();
      const operationalItem = { id: 123 };
      jest.spyOn(operationalItemFormService, 'getOperationalItem').mockReturnValue({ id: null });
      jest.spyOn(operationalItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operationalItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operationalItem }));
      saveSubject.complete();

      // THEN
      expect(operationalItemFormService.getOperationalItem).toHaveBeenCalled();
      expect(operationalItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperationalItem>>();
      const operationalItem = { id: 123 };
      jest.spyOn(operationalItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operationalItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operationalItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
