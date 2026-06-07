import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { TransactionService } from '../../../services/transaction.service';
import { TransactionRequest } from '../../../models/transaction.model';
import { MemberService } from '../../../services/member.service';
import { Member } from '../../../models/member.model';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './transaction-form.component.html',
  styleUrl: './transaction-form.component.css'
})
export class TransactionFormComponent implements OnInit {
  members: Member[] = [];
  data: TransactionRequest = { memberId: 0, type: 'CREDIT', amount: 0, description: '' };
  loading = false;
  error = '';
  success = '';

  constructor(
    private transactionService: TransactionService,
    private memberService: MemberService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.memberService.getAll().subscribe(res => this.members = res);
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.transactionService.create(this.data).subscribe({
      next: () => {
        this.success = 'Transaction created successfully!';
        this.loading = false;
        this.data = { memberId: 0, type: 'CREDIT', amount: 0, description: '' };
      },
      error: (err) => {
        this.error = err.error?.message || 'Transaction failed.';
        this.loading = false;
      }
    });
  }
}
