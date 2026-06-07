import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  data = { username: '', password: '' };
  error = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/member/dashboard']);
    }
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.authService.login(this.data, 'MEMBER').subscribe({
      next: () => this.router.navigate(['/member/dashboard']),
      error: (err) => {
        this.error = err.error?.message || 'Invalid username or password.';
        this.loading = false;
      }
    });
  }
}
