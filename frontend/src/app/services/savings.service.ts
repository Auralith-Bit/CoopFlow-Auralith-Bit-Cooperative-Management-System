import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Savings, SavingsRequest } from '../models/savings.model';

@Injectable({ providedIn: 'root' })
export class SavingsService {
  private apiUrl = `${environment.apiUrl}/savings`;

  constructor(private http: HttpClient) {}

  deposit(data: SavingsRequest): Observable<Savings> {
    return this.http.post<Savings>(`${this.apiUrl}/deposit`, data);
  }

  withdraw(data: SavingsRequest): Observable<Savings> {
    return this.http.post<Savings>(`${this.apiUrl}/withdraw`, data);
  }

  getHistory(memberId: number): Observable<Savings[]> {
    return this.http.get<Savings[]>(`${this.apiUrl}/member/${memberId}`);
  }

  getBalance(memberId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/member/${memberId}/balance`);
  }
}
