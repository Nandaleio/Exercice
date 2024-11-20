import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss'
})
export class SignInComponent  {

  error?: string;
  isSubmit = false;
  isRegister = false;

  form = {
    username: '',
    password: '',
  }

  constructor(private authService: AuthService, 
              private router: Router,
  ) {}

  signIn() {
    this.isSubmit = true;
    if(!this.form.username || !this.form.password) return
    
    this.authService.login(this.form.username, this.form.password).subscribe(_res => {
      this.router.navigate(['dashboard']);
    })
  }


}
