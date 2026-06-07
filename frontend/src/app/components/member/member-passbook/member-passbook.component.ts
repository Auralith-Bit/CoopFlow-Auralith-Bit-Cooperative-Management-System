import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MemberService } from '../../../services/member.service';
import { Savings } from '../../../models/savings.model';

@Component({
  selector: 'app-member-passbook',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './member-passbook.component.html',
  styleUrl: './member-passbook.component.css'
})
export class MemberPassbookComponent implements OnInit {
  balance = 0;
  transactions: Savings[] = [];
  filteredTransactions: Savings[] = [];
  loading = true;
  error = '';

  filterType = '';
  filterDateFrom = '';
  filterDateTo = '';

  constructor(private memberService: MemberService) {}

  ngOnInit(): void {
    this.loadPassbook();
  }

  loadPassbook(): void {
    this.loading = true;
    this.memberService.getPassbook().subscribe({
      next: (data) => {
        this.balance = data.balance;
        this.transactions = data.transactions;
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load passbook';
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    this.filteredTransactions = this.transactions.filter(t => {
      if (this.filterType && t.type !== this.filterType) return false;
      if (this.filterDateFrom && t.transactionDate < this.filterDateFrom) return false;
      if (this.filterDateTo && t.transactionDate > this.filterDateTo) return false;
      return true;
    });
  }

  clearFilters(): void {
    this.filterType = '';
    this.filterDateFrom = '';
    this.filterDateTo = '';
    this.applyFilter();
  }
}
