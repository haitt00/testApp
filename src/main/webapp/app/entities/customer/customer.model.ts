export interface ICustomer {
  id: number;
  name?: string | null;
  address?: string | null;
  phone?: string | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
