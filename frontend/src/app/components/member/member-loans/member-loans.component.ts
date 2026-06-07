import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MemberService } from '../../../services/member.service';
import { Loan } from '../../../models/loan.model';

@Component({
  selector: 'app-member-loans',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './member-loans.component.html',
  styleUrl: './member-loans.component.css'
})
export class MemberLoansComponent implements OnInit {
  loans: Loan[] = [];
  loading = true;
  error = '';

  constructor(private memberService: MemberService) {}

  ngOnInit(): void {
    this.memberService.getMyLoans().subscribe({
      next: (data) => {
        this.loans = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load loans';
        this.loading = false;
      }
    });
  }
}
