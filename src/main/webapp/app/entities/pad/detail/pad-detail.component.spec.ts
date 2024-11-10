import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PadDetailComponent } from './pad-detail.component';

describe('Pad Management Detail Component', () => {
  let comp: PadDetailComponent;
  let fixture: ComponentFixture<PadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pad: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pad on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
