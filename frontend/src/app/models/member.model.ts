export interface Member {
  id: number;
  memberId: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  dateOfBirth?: string;
  joinDate: string;
  status: string;
  userId?: number;
}
