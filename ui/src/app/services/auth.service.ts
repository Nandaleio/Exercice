import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private LOCAL_KEY = "telemis_token"
  private token: string | null;

  // Using localstorage to prevent logout on page reload (not super secure)
  constructor(private http: HttpClient) {
    this.token = localStorage.getItem(this.LOCAL_KEY)
  }

  login(username: string, password: string) {
    return this.http.post<string>(`${environment.apiURL}/auth/login`, {username, password}).pipe(
      tap(token => this.setToken(token))
    );
  }

  register(username: string, password: string) {
    return this.http.post<string>(`${environment.apiURL}/auth/register`, {username, password}).pipe(
      tap(token => this.setToken(token))
    );
  }

  logout() {
    localStorage.removeItem(this.LOCAL_KEY);
    this.token = null;
  }

  checkLoggedIn(): boolean {
    return !!this.token;
  }

  private setToken(token:string){
    localStorage.setItem(this.LOCAL_KEY, token);
    this.token = token;
  }

  getToken() {
    return this.token;
  }

}
