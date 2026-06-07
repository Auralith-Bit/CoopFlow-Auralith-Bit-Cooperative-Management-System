export interface Transaction {
  id: number;
  memberId: number;
  type: string;
  amount: number;
  transactionDate: string;
  description: string;
}

export interface TransactionRequest {
  memberId: number;
  type: string;
  amount: number;
  description?: string;
}
