import { IPad, NewPad } from './pad.model';

export const sampleWithRequiredData: IPad = {
  id: 2443,
  name: 'Cape ROI Enterprise-wide',
};

export const sampleWithPartialData: IPad = {
  id: 2916,
  name: 'Arkansas channels copying',
};

export const sampleWithFullData: IPad = {
  id: 18609,
  name: 'navigate Intelligent',
};

export const sampleWithNewData: NewPad = {
  name: 'morph Salad',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
