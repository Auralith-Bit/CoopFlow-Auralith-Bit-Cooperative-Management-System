import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminService } from '../../../services/admin.service';
import { Member } from '../../../models/member.model';

@Component({
  selector: 'app-member-approval',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './member-approval.component.html',
  styleUrl: './member-approval.component.css'
})
export class MemberApprovalComponent implements OnInit {
  pendingMembers: Member[] = [];
  loading = true;
  error = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadPending();
  }

  loadPending(): void {
    this.loading = true;
    this.adminService.getPendingMembers().subscribe({
      next: (res) => { this.pendingMembers = res; this.loading = false; },
      error: () => { this.error = 'Failed to load pending members'; this.loading = false; }
    });
  }

  approve(id: number): void {
    this.adminService.approveMember(id).subscribe({
      next: () => { this.pendingMembers = this.pendingMembers.filter(m => m.id !== id); },
      error: () => this.error = 'Failed to approve member'
    });
  }

  reject(id: number): void {
    if (confirm('Reject and delete this member registration?')) {
      this.adminService.rejectMember(id).subscribe({
        next: () => { this.pendingMembers = this.pendingMembers.filter(m => m.id !== id); },
        error: () => this.error = 'Failed to reject member'
      });
    }
  }
}
