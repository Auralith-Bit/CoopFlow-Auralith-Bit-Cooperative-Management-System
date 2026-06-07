import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../environments/environment';
import { LoginRequest, LoginResponse, RegisterRequest } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private tokenKey = 'cms_token';
  private userKey = 'cms_user';

  constructor(private http: HttpClient) {}

  login(data: LoginRequest, loginType = 'MEMBER'): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, { ...data, loginType }).pipe(
      tap(res => {
        localStorage.setItem(this.tokenKey, res.token);
        localStorage.setItem(this.userKey, JSON.stringify({
          username: res.username, role: res.role, staffId: res.staffId
        }));
      })
    );
  }

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getRole(): string | null {
    const user = localStorage.getItem(this.userKey);
    if (user) {
      try { return JSON.parse(user).role; } catch { return null; }
    }
    return null;
  }

  getStaffId(): string | null {
    const user = localStorage.getItem(this.userKey);
    if (user) {
      try { return JSON.parse(user).staffId; } catch { return null; }
    }
    return null;
  }

  getUsername(): string | null {
    const user = localStorage.getItem(this.userKey);
    if (user) {
      try { return JSON.parse(user).username; } catch { return null; }
    }
    return null;
  }
}
