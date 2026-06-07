import { Member } from './member.model';
import { Savings } from './savings.model';
import { Loan } from './loan.model';

export interface MemberDashboard {
  member: Member;
  savingsBalance: number;
  recentTransactions: Savings[];
  activeLoans: Loan[];
  totalLoans: number;
  pendingLoans: number;
}
