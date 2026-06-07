import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { MemberService } from '../../../services/member.service';
import { Member } from '../../../models/member.model';

@Component({
  selector: 'app-member-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './member-list.component.html',
  styleUrl: './member-list.component.css'
})
export class MemberListComponent implements OnInit {
  members: Member[] = [];
  loading = true;

  constructor(private memberService: MemberService, private router: Router) {}

  ngOnInit(): void {
    this.loadMembers();
  }

  loadMembers(): void {
    this.loading = true;
    this.memberService.getAll().subscribe({
      next: (res) => {
        this.members = res;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  editMember(id: number): void {
    this.router.navigate(['/members', id, 'edit']);
  }

  deleteMember(id: number): void {
    if (confirm('Are you sure you want to delete this member? This will also remove all their savings and loan records.')) {
      this.memberService.delete(id).subscribe({
        next: () => this.loadMembers(),
        error: (err) => alert('Failed to delete member: ' + (err.error?.message || err.message))
      });
    }
  }
}
