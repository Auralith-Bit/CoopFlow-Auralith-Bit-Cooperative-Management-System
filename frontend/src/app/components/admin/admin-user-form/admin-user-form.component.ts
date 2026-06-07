import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AdminService } from '../../../services/admin.service';

@Component({
  selector: 'app-admin-user-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin-user-form.component.html',
  styleUrl: './admin-user-form.component.css'
})
export class AdminUserFormComponent {
  data = { username: '', password: '', email: '', role: 'ACCOUNTANT' as string };
  loading = false;
  error = '';
  success = '';

  constructor(private adminService: AdminService, private router: Router) {}

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.adminService.createUser(this.data).subscribe({
      next: () => {
        this.success = 'User created successfully!';
        this.loading = false;
        setTimeout(() => this.router.navigate(['/admin/users']), 1500);
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create user';
        this.loading = false;
      }
    });
  }
}
