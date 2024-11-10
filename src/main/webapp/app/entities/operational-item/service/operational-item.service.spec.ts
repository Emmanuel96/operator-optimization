import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IOperationalItem } from '../operational-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../operational-item.test-samples';

import { OperationalItemService, RestOperationalItem } from './operational-item.service';

const requireRestSample: RestOperationalItem = {
  ...sampleWithRequiredData,
  dueDate: sampleWithRequiredData.dueDate?.format(DATE_FORMAT),
};

describe('OperationalItem Service', () => {
  let service: OperationalItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IOperationalItem | IOperationalItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperationalItemService);
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

    it('should create a OperationalItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const operationalItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(operationalItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OperationalItem', () => {
      const operationalItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(operationalItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OperationalItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OperationalItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OperationalItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOperationalItemToCollectionIfMissing', () => {
      it('should add a OperationalItem to an empty array', () => {
        const operationalItem: IOperationalItem = sampleWithRequiredData;
        expectedResult = service.addOperationalItemToCollectionIfMissing([], operationalItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operationalItem);
      });

      it('should not add a OperationalItem to an array that contains it', () => {
        const operationalItem: IOperationalItem = sampleWithRequiredData;
        const operationalItemCollection: IOperationalItem[] = [
          {
            ...operationalItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOperationalItemToCollectionIfMissing(operationalItemCollection, operationalItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OperationalItem to an array that doesn't contain it", () => {
        const operationalItem: IOperationalItem = sampleWithRequiredData;
        const operationalItemCollection: IOperationalItem[] = [sampleWithPartialData];
        expectedResult = service.addOperationalItemToCollectionIfMissing(operationalItemCollection, operationalItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operationalItem);
      });

      it('should add only unique OperationalItem to an array', () => {
        const operationalItemArray: IOperationalItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const operationalItemCollection: IOperationalItem[] = [sampleWithRequiredData];
        expectedResult = service.addOperationalItemToCollectionIfMissing(operationalItemCollection, ...operationalItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operationalItem: IOperationalItem = sampleWithRequiredData;
        const operationalItem2: IOperationalItem = sampleWithPartialData;
        expectedResult = service.addOperationalItemToCollectionIfMissing([], operationalItem, operationalItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operationalItem);
        expect(expectedResult).toContain(operationalItem2);
      });

      it('should accept null and undefined values', () => {
        const operationalItem: IOperationalItem = sampleWithRequiredData;
        expectedResult = service.addOperationalItemToCollectionIfMissing([], null, operationalItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operationalItem);
      });

      it('should return initial array if no OperationalItem is added', () => {
        const operationalItemCollection: IOperationalItem[] = [sampleWithRequiredData];
        expectedResult = service.addOperationalItemToCollectionIfMissing(operationalItemCollection, undefined, null);
        expect(expectedResult).toEqual(operationalItemCollection);
      });
    });

    describe('compareOperationalItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOperationalItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOperationalItem(entity1, entity2);
        const compareResult2 = service.compareOperationalItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOperationalItem(entity1, entity2);
        const compareResult2 = service.compareOperationalItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOperationalItem(entity1, entity2);
        const compareResult2 = service.compareOperationalItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
