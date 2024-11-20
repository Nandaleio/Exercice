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
  private payload: any;

  constructor(private http: HttpClient) {
    this.token = localStorage.getItem(this.LOCAL_KEY)
    if(this.token) {
      this.payload = this.parseJwt();
    }
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

  private parseJwt() {
    var base64Url = this.token!.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
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
    if(this.token) {
      this.payload = this.parseJwt();
    }
  }

  getToken() {
    return this.token;
  }

  getPayload() {
    return this.payload;
  }

}
