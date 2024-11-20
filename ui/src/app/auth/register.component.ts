import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class RegisterComponent {

  error?: string;
  isSubmit = false;
  isRegister = true;

  form = {
    username: '',
    password: '',
    confirm: '',
  }

  constructor(private authService: AuthService, 
              private router: Router,
  ) {}


  register() {
    this.isSubmit = true;
    if(this.form.password != this.form.confirm) {
      this.error = "Password and confirm password must be identical";
      return;
    }

    this.authService.register(this.form.username, this.form.password).subscribe(_res => {
      this.router.navigate(['dashboard']);
    })
  }
}
