export interface IPad {
  id: number;
  name?: string | null;
}

export type NewPad = Omit<IPad, 'id'> & { id: null };
