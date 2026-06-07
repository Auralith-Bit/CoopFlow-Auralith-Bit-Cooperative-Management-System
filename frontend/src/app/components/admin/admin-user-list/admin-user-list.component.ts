import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AdminService, AppUser } from '../../../services/admin.service';

@Component({
  selector: 'app-admin-user-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-user-list.component.html',
  styleUrl: './admin-user-list.component.css'
})
export class AdminUserListComponent implements OnInit {
  users: AppUser[] = [];
  loading = true;

  constructor(private adminService: AdminService, private router: Router) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.adminService.getUsers().subscribe({
      next: (res) => { this.users = res; this.loading = false; },
      error: () => this.loading = false
    });
  }

  createUser(): void {
    this.router.navigate(['/admin/users/new']);
  }

  deleteUser(id: number): void {
    if (confirm('Delete this user?')) {
      this.adminService.deleteUser(id).subscribe(() => this.loadUsers());
    }
  }
}
