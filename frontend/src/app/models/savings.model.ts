export interface Savings {
  id: number;
  memberId: number;
  memberName?: string;
  amount: number;
  type: string;
  description: string;
  transactionDate: string;
  balanceAfter: number;
}

export interface SavingsRequest {
  memberId: number;
  amount: number;
  description: string;
}
