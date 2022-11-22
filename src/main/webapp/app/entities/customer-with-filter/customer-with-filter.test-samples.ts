import { ICustomerWithFilter, NewCustomerWithFilter } from './customer-with-filter.model';

export const sampleWithRequiredData: ICustomerWithFilter = {
  id: 59913,
};

export const sampleWithPartialData: ICustomerWithFilter = {
  id: 38598,
  phone: '456-643-5255',
};

export const sampleWithFullData: ICustomerWithFilter = {
  id: 30731,
  name: 'synthesize Alley Accountability',
  address: 'Games JSON Small',
  phone: '1-574-310-4330',
};

export const sampleWithNewData: NewCustomerWithFilter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
