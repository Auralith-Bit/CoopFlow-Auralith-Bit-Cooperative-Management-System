import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { LoanService } from '../../../services/loan.service';
import { LoanRequest } from '../../../models/loan.model';
import { MemberService } from '../../../services/member.service';
import { Member } from '../../../models/member.model';

@Component({
  selector: 'app-loan-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './loan-form.component.html',
  styleUrl: './loan-form.component.css'
})
export class LoanFormComponent implements OnInit {
  members: Member[] = [];
  data: LoanRequest = { memberId: 0, amount: 0, interestRate: 5, tenureMonths: 12 };
  loading = false;
  error = '';
  success = '';

  constructor(
    private loanService: LoanService,
    private memberService: MemberService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.memberService.getAll().subscribe(res => this.members = res);
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.loanService.apply(this.data).subscribe({
      next: () => {
        this.success = 'Loan application submitted successfully!';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/loans']), 1500);
      },
      error: (err) => {
        this.error = err.error?.message || 'Loan application failed.';
        this.loading = false;
      }
    });
  }
}
