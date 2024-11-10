import { IPad } from 'app/entities/pad/pad.model';
import { LocationType } from 'app/entities/enumerations/location-type.model';

export interface ILocation {
  id: number;
  name?: string | null;
  uwi?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  type?: LocationType | null;
  pad?: Pick<IPad, 'id'> | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
