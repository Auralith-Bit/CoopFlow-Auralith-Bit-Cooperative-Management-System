import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { MemberService } from '../../../services/member.service';
import { Member } from '../../../models/member.model';

@Component({
  selector: 'app-member-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './member-form.component.html',
  styleUrl: './member-form.component.css'
})
export class MemberFormComponent implements OnInit {
  member: Member = {
    id: 0, memberId: '', firstName: '', lastName: '', email: '', phone: '', address: '',
    city: '', state: '', zipCode: '', joinDate: new Date().toISOString().split('T')[0],
    status: 'ACTIVE'
  };
  isEdit = false;
  loading = false;
  error = '';

  constructor(
    private memberService: MemberService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.loading = true;
      this.memberService.getById(+id).subscribe({
        next: (res) => {
          this.member = res;
          this.loading = false;
        },
        error: () => this.loading = false
      });
    }
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    if (this.isEdit) {
      this.memberService.update(this.member.id, this.member).subscribe({
        next: () => this.router.navigate(['/members']),
        error: (err) => {
          this.error = err.error?.message || 'Update failed.';
          this.loading = false;
        }
      });
    } else {
      this.memberService.create(this.member).subscribe({
        next: () => this.router.navigate(['/members']),
        error: (err) => {
          this.error = err.error?.message || 'Create failed.';
          this.loading = false;
        }
      });
    }
  }
}
