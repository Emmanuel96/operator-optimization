import { LocationType } from 'app/entities/enumerations/location-type.model';

import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 91847,
  name: 'parsing',
  latitude: 93501,
  longitude: 8097,
  type: LocationType['PLANT'],
};

export const sampleWithPartialData: ILocation = {
  id: 92404,
  name: 'Island',
  uwi: 'indigo Virginia Arkansas',
  latitude: 80756,
  longitude: 11070,
  type: LocationType['PLANT'],
};

export const sampleWithFullData: ILocation = {
  id: 16917,
  name: 'Internal',
  uwi: 'solutions',
  latitude: 4180,
  longitude: 31982,
  type: LocationType['PLANT'],
};

export const sampleWithNewData: NewLocation = {
  name: 'enable network Steel',
  latitude: 40165,
  longitude: 22995,
  type: LocationType['WELL'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
