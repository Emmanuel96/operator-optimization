import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OperationalItemDetailComponent } from './operational-item-detail.component';

describe('OperationalItem Management Detail Component', () => {
  let comp: OperationalItemDetailComponent;
  let fixture: ComponentFixture<OperationalItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperationalItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ operationalItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OperationalItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OperationalItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load operationalItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.operationalItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
