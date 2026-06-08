import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Member } from '../models/member.model';

export interface AppUser {
  id: number;
  username: string;
  staffId?: string;
  email: string;
  role: string;
  enabled: boolean;
  createdAt: string;
}

export interface CreateUserRequest {
  username: string;
  password: string;
  email: string;
  role: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {
  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  getUsers(): Observable<AppUser[]> {
    return this.http.get<AppUser[]>(`${this.apiUrl}/users`);
  }

  createUser(data: CreateUserRequest): Observable<AppUser> {
    return this.http.post<AppUser>(`${this.apiUrl}/users`, data);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/users/${id}`);
  }

  getPendingMembers(): Observable<Member[]> {
    return this.http.get<Member[]>(`${this.apiUrl}/members/pending`);
  }

  approveMember(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/members/${id}/approve`, {});
  }

  rejectMember(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/members/${id}/reject`);
  }
}
