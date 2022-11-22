export interface ICustomerWithFilter {
  id: number;
  name?: string | null;
  address?: string | null;
  phone?: string | null;
}

export type NewCustomerWithFilter = Omit<ICustomerWithFilter, 'id'> & { id: null };
