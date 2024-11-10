import dayjs from 'dayjs/esm';

import { OperationalItemTypes } from 'app/entities/enumerations/operational-item-types.model';

import { IOperationalItem, NewOperationalItem } from './operational-item.model';

export const sampleWithRequiredData: IOperationalItem = {
  id: 52836,
  type: OperationalItemTypes['OPERATIONAL_ACTION'],
  priorityScore: 70929,
};

export const sampleWithPartialData: IOperationalItem = {
  id: 82808,
  type: OperationalItemTypes['OPERATIONAL_ALARM'],
  priorityScore: 81647,
  dueDate: dayjs('2024-11-06'),
};

export const sampleWithFullData: IOperationalItem = {
  id: 85983,
  type: OperationalItemTypes['OPERATIONAL_ACTION'],
  priorityScore: 4715,
  dueDate: dayjs('2024-11-07'),
};

export const sampleWithNewData: NewOperationalItem = {
  type: OperationalItemTypes['OPERATIONAL_ACTION'],
  priorityScore: 94628,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
