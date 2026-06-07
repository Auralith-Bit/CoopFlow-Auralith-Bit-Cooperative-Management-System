import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { LoanService } from '../../../services/loan.service';
import { Loan, LoanPaymentRequest } from '../../../models/loan.model';

@Component({
  selector: 'app-loan-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './loan-detail.component.html',
  styleUrl: './loan-detail.component.css'
})
export class LoanDetailComponent implements OnInit {
  loan: Loan | null = null;
  paymentAmount = 0;
  loading = true;
  error = '';
  success = '';

  constructor(
    private loanService: LoanService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loanService.getById(+id).subscribe({
        next: (res) => {
          this.loan = res;
          this.loading = false;
        },
        error: () => this.loading = false
      });
    }
  }

  makePayment(): void {
    if (!this.loan || this.paymentAmount <= 0) return;
    this.loading = true;
    this.error = '';
    this.success = '';
    this.loanService.makePayment(this.loan.id, { amount: this.paymentAmount }).subscribe({
      next: (res) => {
        this.loan = res;
        this.success = 'Payment recorded successfully!';
        this.paymentAmount = 0;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'Payment failed.';
        this.loading = false;
      }
    });
  }

  approveLoan(): void {
    if (!this.loan) return;
    this.loanService.approve(this.loan.id).subscribe({
      next: (res) => {
        this.loan = res;
        this.success = 'Loan approved!';
      },
      error: (err) => {
        this.error = err.error?.message || 'Approval failed.';
      }
    });
  }
}
