import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 24379,
};

export const sampleWithPartialData: ICustomer = {
  id: 25384,
  address: 'Communications Club',
  phone: '855-695-8530 x06993',
};

export const sampleWithFullData: ICustomer = {
  id: 12349,
  name: 'THX Estonia',
  address: 'calculate front-end Towels',
  phone: '457-405-6335 x3342',
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
