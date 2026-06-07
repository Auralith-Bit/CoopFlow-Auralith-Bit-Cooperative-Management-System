export interface Loan {
  id: number;
  memberId: number;
  memberName?: string;
  amount: number;
  interestRate: number;
  tenureMonths: number;
  emiAmount?: number;
  totalPayable?: number;
  amountPaid?: number;
  status: string;
  appliedDate: string;
  approvedDate?: string;
  approvedBy?: string;
  notes?: string;
}

export interface LoanRequest {
  memberId: number;
  amount: number;
  interestRate: number;
  tenureMonths: number;
}

export interface LoanPaymentRequest {
  amount: number;
}
