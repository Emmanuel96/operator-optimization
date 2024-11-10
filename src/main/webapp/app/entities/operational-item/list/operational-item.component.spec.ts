import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OperationalItemService } from '../service/operational-item.service';

import { OperationalItemComponent } from './operational-item.component';

describe('OperationalItem Management Component', () => {
  let comp: OperationalItemComponent;
  let fixture: ComponentFixture<OperationalItemComponent>;
  let service: OperationalItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'operational-item', component: OperationalItemComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [OperationalItemComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(OperationalItemComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationalItemComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OperationalItemService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.operationalItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to operationalItemService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOperationalItemIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOperationalItemIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
