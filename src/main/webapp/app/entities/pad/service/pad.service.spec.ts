import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPad } from '../pad.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pad.test-samples';

import { PadService } from './pad.service';

const requireRestSample: IPad = {
  ...sampleWithRequiredData,
};

describe('Pad Service', () => {
  let service: PadService;
  let httpMock: HttpTestingController;
  let expectedResult: IPad | IPad[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PadService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Pad', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pad = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pad', () => {
      const pad = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pad).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pad', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pad', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pad', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPadToCollectionIfMissing', () => {
      it('should add a Pad to an empty array', () => {
        const pad: IPad = sampleWithRequiredData;
        expectedResult = service.addPadToCollectionIfMissing([], pad);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pad);
      });

      it('should not add a Pad to an array that contains it', () => {
        const pad: IPad = sampleWithRequiredData;
        const padCollection: IPad[] = [
          {
            ...pad,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPadToCollectionIfMissing(padCollection, pad);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pad to an array that doesn't contain it", () => {
        const pad: IPad = sampleWithRequiredData;
        const padCollection: IPad[] = [sampleWithPartialData];
        expectedResult = service.addPadToCollectionIfMissing(padCollection, pad);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pad);
      });

      it('should add only unique Pad to an array', () => {
        const padArray: IPad[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const padCollection: IPad[] = [sampleWithRequiredData];
        expectedResult = service.addPadToCollectionIfMissing(padCollection, ...padArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pad: IPad = sampleWithRequiredData;
        const pad2: IPad = sampleWithPartialData;
        expectedResult = service.addPadToCollectionIfMissing([], pad, pad2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pad);
        expect(expectedResult).toContain(pad2);
      });

      it('should accept null and undefined values', () => {
        const pad: IPad = sampleWithRequiredData;
        expectedResult = service.addPadToCollectionIfMissing([], null, pad, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pad);
      });

      it('should return initial array if no Pad is added', () => {
        const padCollection: IPad[] = [sampleWithRequiredData];
        expectedResult = service.addPadToCollectionIfMissing(padCollection, undefined, null);
        expect(expectedResult).toEqual(padCollection);
      });
    });

    describe('comparePad', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePad(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePad(entity1, entity2);
        const compareResult2 = service.comparePad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePad(entity1, entity2);
        const compareResult2 = service.comparePad(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePad(entity1, entity2);
        const compareResult2 = service.comparePad(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
