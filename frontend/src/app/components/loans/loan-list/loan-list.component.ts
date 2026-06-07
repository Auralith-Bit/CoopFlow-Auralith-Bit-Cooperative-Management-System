import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { LoanService } from '../../../services/loan.service';
import { Loan } from '../../../models/loan.model';

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './loan-list.component.html',
  styleUrl: './loan-list.component.css'
})
export class LoanListComponent implements OnInit {
  loans: Loan[] = [];
  loading = true;

  constructor(private loanService: LoanService, private router: Router) {}

  ngOnInit(): void {
    this.loadLoans();
  }

  loadLoans(): void {
    this.loading = true;
    this.loanService.getAll().subscribe({
      next: (res) => {
        this.loans = res;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  viewLoan(id: number): void {
    this.router.navigate(['/loans', id]);
  }

  approveLoan(id: number): void {
    this.loanService.approve(id).subscribe(() => this.loadLoans());
  }
}
