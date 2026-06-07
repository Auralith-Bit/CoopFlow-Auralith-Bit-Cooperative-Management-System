import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SavingsService } from '../../../services/savings.service';
import { Savings } from '../../../models/savings.model';
import { MemberService } from '../../../services/member.service';
import { Member } from '../../../models/member.model';

@Component({
  selector: 'app-savings-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './savings-list.component.html',
  styleUrl: './savings-list.component.css'
})
export class SavingsListComponent implements OnInit {
  members: Member[] = [];
  selectedMemberId: number | null = null;
  transactions: Savings[] = [];
  balance: number | null = null;
  loading = false;

  constructor(
    private savingsService: SavingsService,
    private memberService: MemberService
  ) {}

  ngOnInit(): void {
    this.memberService.getAll().subscribe(res => this.members = res);
  }

  loadData(): void {
    if (!this.selectedMemberId) return;
    this.loading = true;
    this.savingsService.getHistory(this.selectedMemberId).subscribe({
      next: (res) => {
        this.transactions = res;
        this.loading = false;
      },
      error: () => this.loading = false
    });
    this.savingsService.getBalance(this.selectedMemberId).subscribe({
      next: (res) => this.balance = res
    });
  }
}
