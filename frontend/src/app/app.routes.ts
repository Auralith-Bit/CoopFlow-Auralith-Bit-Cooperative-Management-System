import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./components/auth/login/login.component').then(c => c.LoginComponent)
  },
  {
    path: 'staff',
    loadComponent: () => import('./components/auth/staff-login/staff-login.component').then(c => c.StaffLoginComponent)
  },
  {
    path: 'admin',
    pathMatch: 'full',
    loadComponent: () => import('./components/auth/admin-login/admin-login.component').then(c => c.AdminLoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./components/auth/register/register.component').then(c => c.RegisterComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./components/dashboard/dashboard.component').then(c => c.DashboardComponent)
  },
  {
    path: 'members',
    canActivate: [authGuard],
    loadComponent: () => import('./components/members/member-list/member-list.component').then(c => c.MemberListComponent)
  },
  {
    path: 'members/new',
    canActivate: [authGuard],
    loadComponent: () => import('./components/members/member-form/member-form.component').then(c => c.MemberFormComponent)
  },
  {
    path: 'members/:id/edit',
    canActivate: [authGuard],
    loadComponent: () => import('./components/members/member-form/member-form.component').then(c => c.MemberFormComponent)
  },
  {
    path: 'savings',
    canActivate: [authGuard],
    loadComponent: () => import('./components/savings/savings-list/savings-list.component').then(c => c.SavingsListComponent)
  },
  {
    path: 'savings/new',
    canActivate: [authGuard],
    loadComponent: () => import('./components/savings/savings-form/savings-form.component').then(c => c.SavingsFormComponent)
  },
  {
    path: 'loans',
    canActivate: [authGuard],
    loadComponent: () => import('./components/loans/loan-list/loan-list.component').then(c => c.LoanListComponent)
  },
  {
    path: 'loans/new',
    canActivate: [authGuard],
    loadComponent: () => import('./components/loans/loan-form/loan-form.component').then(c => c.LoanFormComponent)
  },
  {
    path: 'loans/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./components/loans/loan-detail/loan-detail.component').then(c => c.LoanDetailComponent)
  },
  {
    path: 'transactions',
    canActivate: [authGuard],
    loadComponent: () => import('./components/transactions/transaction-list/transaction-list.component').then(c => c.TransactionListComponent)
  },
  {
    path: 'transactions/new',
    canActivate: [authGuard],
    loadComponent: () => import('./components/transactions/transaction-form/transaction-form.component').then(c => c.TransactionFormComponent)
  },
  {
    path: 'admin',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'users', pathMatch: 'full' },
      { path: 'users', loadComponent: () => import('./components/admin/admin-user-list/admin-user-list.component').then(c => c.AdminUserListComponent) },
      { path: 'users/new', loadComponent: () => import('./components/admin/admin-user-form/admin-user-form.component').then(c => c.AdminUserFormComponent) },
      { path: 'member-approval', loadComponent: () => import('./components/admin/member-approval/member-approval.component').then(c => c.MemberApprovalComponent) }
    ]
  },
  {
    path: 'member',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', loadComponent: () => import('./components/member/member-dashboard/member-dashboard.component').then(c => c.MemberDashboardComponent) },
      { path: 'passbook', loadComponent: () => import('./components/member/member-passbook/member-passbook.component').then(c => c.MemberPassbookComponent) },
      { path: 'loans', loadComponent: () => import('./components/member/member-loans/member-loans.component').then(c => c.MemberLoansComponent) },
      { path: 'loans/apply', loadComponent: () => import('./components/member/member-loan-apply/member-loan-apply.component').then(c => c.MemberLoanApplyComponent) },
      { path: 'savings', loadComponent: () => import('./components/member/member-savings/member-savings.component').then(c => c.MemberSavingsComponent) }
    ]
  },
  { path: '**', redirectTo: '/dashboard' }
];
