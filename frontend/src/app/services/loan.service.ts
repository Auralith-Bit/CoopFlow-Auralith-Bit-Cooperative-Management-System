import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Loan, LoanRequest, LoanPaymentRequest } from '../models/loan.model';

@Injectable({ providedIn: 'root' })
export class LoanService {
  private apiUrl = `${environment.apiUrl}/loans`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Loan[]> {
    return this.http.get<Loan[]>(this.apiUrl);
  }

  getById(id: number): Observable<Loan> {
    return this.http.get<Loan>(`${this.apiUrl}/${id}`);
  }

  getByMember(memberId: number): Observable<Loan[]> {
    return this.http.get<Loan[]>(`${this.apiUrl}?memberId=${memberId}`);
  }

  apply(data: LoanRequest): Observable<Loan> {
    return this.http.post<Loan>(this.apiUrl, data);
  }

  approve(id: number): Observable<Loan> {
    return this.http.post<Loan>(`${this.apiUrl}/${id}/approve`, {});
  }

  reject(id: number): Observable<Loan> {
    return this.http.post<Loan>(`${this.apiUrl}/${id}/reject`, {});
  }

  makePayment(id: number, data: LoanPaymentRequest): Observable<Loan> {
    return this.http.post<Loan>(`${this.apiUrl}/${id}/payment`, data);
  }
}
