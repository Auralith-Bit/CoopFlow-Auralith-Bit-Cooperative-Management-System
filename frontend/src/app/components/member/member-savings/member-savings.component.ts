import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MemberService } from '../../../services/member.service';

@Component({
  selector: 'app-member-savings',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './member-savings.component.html',
  styleUrl: './member-savings.component.css'
})
export class MemberSavingsComponent implements OnInit {
  balance = 0;
  loading = true;
  error = '';

  depositData = { amount: null as number | null, description: '' };
  withdrawData = { amount: null as number | null, description: '' };
  submittingDeposit = false;
  submittingWithdraw = false;
  depositSuccess = '';
  withdrawSuccess = '';
  depositError = '';
  withdrawError = '';

  constructor(private memberService: MemberService) {}

  ngOnInit(): void {
    this.loadBalance();
  }

  loadBalance(): void {
    this.loading = true;
    this.memberService.getPassbook().subscribe({
      next: (data) => {
        this.balance = data.balance;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load balance';
        this.loading = false;
      }
    });
  }

  onDeposit(): void {
    if (!this.depositData.amount || this.depositData.amount <= 0) return;
    this.submittingDeposit = true;
    this.depositSuccess = '';
    this.depositError = '';
    this.memberService.deposit(this.depositData).subscribe({
      next: () => {
        this.submittingDeposit = false;
        this.depositSuccess = 'Deposit successful!';
        this.depositData = { amount: null, description: '' };
        this.loadBalance();
      },
      error: (err) => {
        this.submittingDeposit = false;
        this.depositError = err.error?.message || 'Deposit failed';
      }
    });
  }

  onWithdraw(): void {
    if (!this.withdrawData.amount || this.withdrawData.amount <= 0) return;
    this.submittingWithdraw = true;
    this.withdrawSuccess = '';
    this.withdrawError = '';
    this.memberService.withdraw(this.withdrawData).subscribe({
      next: () => {
        this.submittingWithdraw = false;
        this.withdrawSuccess = 'Withdrawal successful!';
        this.withdrawData = { amount: null, description: '' };
        this.loadBalance();
      },
      error: (err) => {
        this.submittingWithdraw = false;
        this.withdrawError = err.error?.message || 'Withdrawal failed';
      }
    });
  }
}
