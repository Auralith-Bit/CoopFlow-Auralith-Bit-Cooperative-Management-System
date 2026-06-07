import { Transaction } from './transaction.model';

export interface DashboardDTO {
  totalMembers: number;
  totalSavings: number;
  totalLoans: number;
  totalLoanAmount: number;
  recentTransactions: Transaction[];
}
