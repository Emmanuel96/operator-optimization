import dayjs from 'dayjs/esm';
import { ILocation } from 'app/entities/location/location.model';
import { OperationalItemTypes } from 'app/entities/enumerations/operational-item-types.model';

export interface IOperationalItem {
  id: number;
  type?: OperationalItemTypes | null;
  priorityScore?: number | null;
  dueDate?: dayjs.Dayjs | null;
  location?: Pick<ILocation, 'id'> | null;
}

export type NewOperationalItem = Omit<IOperationalItem, 'id'> & { id: null };
