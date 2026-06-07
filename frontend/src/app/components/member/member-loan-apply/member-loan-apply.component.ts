import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { MemberService } from '../../../services/member.service';

@Component({
  selector: 'app-member-loan-apply',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './member-loan-apply.component.html',
  styleUrl: './member-loan-apply.component.css'
})
export class MemberLoanApplyComponent {
  loanData = {
    amount: null as number | null,
    interestRate: 10,
    tenureMonths: null as number | null
  };

  submitting = false;
  success = '';
  error = '';

  constructor(
    private memberService: MemberService,
    private router: Router
  ) {}

  get emi(): number {
    const { amount, interestRate, tenureMonths } = this.loanData;
    if (!amount || !interestRate || !tenureMonths || tenureMonths <= 0) return 0;
    const monthlyRate = interestRate / 12 / 100;
    const numerator = amount * monthlyRate * Math.pow(1 + monthlyRate, tenureMonths);
    const denominator = Math.pow(1 + monthlyRate, tenureMonths) - 1;
    return denominator === 0 ? 0 : numerator / denominator;
  }

  get totalPayable(): number {
    return this.emi * (this.loanData.tenureMonths || 0);
  }

  onSubmit(): void {
    if (!this.loanData.amount || !this.loanData.tenureMonths) return;
    this.submitting = true;
    this.success = '';
    this.error = '';
    this.memberService.applyForLoan(this.loanData).subscribe({
      next: () => {
        this.submitting = false;
        this.router.navigate(['/member/loans']);
      },
      error: (err) => {
        this.submitting = false;
        this.error = err.error?.message || 'Failed to submit loan application';
      }
    });
  }
}
