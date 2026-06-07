import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MemberService } from '../../../services/member.service';
import { MemberDashboard } from '../../../models/member-dashboard.model';

@Component({
  selector: 'app-member-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './member-dashboard.component.html',
  styleUrl: './member-dashboard.component.css'
})
export class MemberDashboardComponent implements OnInit {
  dashboard: MemberDashboard | null = null;
  loading = true;
  error = '';

  constructor(private memberService: MemberService) {}

  ngOnInit(): void {
    this.memberService.getDashboard().subscribe({
      next: (data) => {
        this.dashboard = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load dashboard';
        this.loading = false;
      }
    });
  }
}
