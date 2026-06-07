import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Member } from '../models/member.model';
import { Savings } from '../models/savings.model';
import { Loan } from '../models/loan.model';
import { MemberDashboard } from '../models/member-dashboard.model';

@Injectable({ providedIn: 'root' })
export class MemberService {
  private memberApiUrl = `${environment.apiUrl}/member`;
  private adminApiUrl = `${environment.apiUrl}/members`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Member[]> {
    return this.http.get<Member[]>(this.adminApiUrl);
  }

  getById(id: number): Observable<Member> {
    return this.http.get<Member>(`${this.adminApiUrl}/${id}`);
  }

  create(member: Member): Observable<Member> {
    return this.http.post<Member>(this.adminApiUrl, member);
  }

  update(id: number, member: Member): Observable<Member> {
    return this.http.put<Member>(`${this.adminApiUrl}/${id}`, member);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.adminApiUrl}/${id}`);
  }

  getDashboard(): Observable<MemberDashboard> {
    return this.http.get<MemberDashboard>(`${this.memberApiUrl}/dashboard`);
  }

  getPassbook(): Observable<{ balance: number; transactions: Savings[] }> {
    return this.http.get<{ balance: number; transactions: Savings[] }>(`${this.memberApiUrl}/passbook`);
  }

  getMyLoans(): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${this.memberApiUrl}/loans`);
  }

  applyForLoan(data: any): Observable<Loan> {
    return this.http.post<Loan>(`${this.memberApiUrl}/loans/apply`, data);
  }

  deposit(data: any): Observable<Savings> {
    return this.http.post<Savings>(`${this.memberApiUrl}/savings/deposit`, data);
  }

  withdraw(data: any): Observable<Savings> {
    return this.http.post<Savings>(`${this.memberApiUrl}/savings/withdraw`, data);
  }
}
