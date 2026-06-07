import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { SavingsService } from '../../../services/savings.service';
import { SavingsRequest } from '../../../models/savings.model';
import { MemberService } from '../../../services/member.service';
import { Member } from '../../../models/member.model';

@Component({
  selector: 'app-savings-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './savings-form.component.html',
  styleUrl: './savings-form.component.css'
})
export class SavingsFormComponent implements OnInit {
  members: Member[] = [];
  data: SavingsRequest = { memberId: 0, amount: 0, description: '' };
  transactionType: 'DEPOSIT' | 'WITHDRAWAL' = 'DEPOSIT';
  loading = false;
  error = '';
  success = '';

  constructor(
    private savingsService: SavingsService,
    private memberService: MemberService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.memberService.getAll().subscribe(res => this.members = res);
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.success = '';
    const observable = this.transactionType === 'DEPOSIT'
      ? this.savingsService.deposit(this.data)
      : this.savingsService.withdraw(this.data);
    observable.subscribe({
      next: () => {
        this.success = `${this.transactionType} successful!`;
        this.loading = false;
        this.data = { memberId: 0, amount: 0, description: '' };
      },
      error: (err) => {
        this.error = err.error?.message || 'Transaction failed.';
        this.loading = false;
      }
    });
  }
}
