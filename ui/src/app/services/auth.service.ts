import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private token?: string;

  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    return this.http.post<string>(`${environment.apiURL}/api/auth/login`, {username, password}).pipe(
      tap(token => this.token = token)
    );
  }

  register(username: string, password: string) {
    return this.http.post<string>(`${environment.apiURL}/api/auth/register`, {username, password}).pipe(
      tap(token => this.token = token)
    );
  }

  logout() {
    this.token = undefined;
  }

  checkLoggedIn(): boolean {
    return !!this.token;
  }

}
