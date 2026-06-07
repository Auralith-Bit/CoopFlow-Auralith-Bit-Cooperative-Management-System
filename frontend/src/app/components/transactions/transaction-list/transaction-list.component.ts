import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../../services/transaction.service';
import { Transaction } from '../../../models/transaction.model';

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './transaction-list.component.html',
  styleUrl: './transaction-list.component.css'
})
export class TransactionListComponent implements OnInit {
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
  loading = true;
  startDate = '';
  endDate = '';

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.loading = true;
    this.transactionService.getAll().subscribe({
      next: (res) => {
        this.transactions = res;
        this.filteredTransactions = res;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  filterByDate(): void {
    if (this.startDate && this.endDate) {
      this.transactionService.getBetween(this.startDate, this.endDate).subscribe({
        next: (res) => this.filteredTransactions = res
      });
    } else {
      this.filteredTransactions = this.transactions;
    }
  }

  clearFilter(): void {
    this.startDate = '';
    this.endDate = '';
    this.filteredTransactions = this.transactions;
  }
}
