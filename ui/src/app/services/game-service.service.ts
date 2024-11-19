import { Injectable } from '@angular/core';
import { Rule } from '../models/rule-model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Game } from '../models/game-model';
import { Subject, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private socket?: WebSocket;

  subject = new Subject<{type: "create" | "delete" | "update", game: Game | number}>();

  constructor(private http: HttpClient) {}

  
  getRules() {
    return this.http.get<Rule[]>(environment.apiURL+'/rules');
  }

  createGame(gameRuleId: number, name: string) {
    return this.http.post<Game>(environment.apiURL+'/new', {gameRuleId, name}).pipe(
      tap(res => this.subject.next({type: "create", game: res}))
    );
  }

  getGameList() {
    return this.http.get<Game[]>(environment.apiURL+'/games');
  }
  
  getGame(gameId: number | string) {
    return this.http.get<Game>(`${environment.apiURL}/game?gameId=${gameId}`);
  }

  deleteGame(gameId: number) {
    return this.http.get(`${environment.apiURL}/delete?gameId=${gameId}`).pipe(
      tap(res => this.subject.next({type: "delete", game: gameId}))
    );
  }

  joinGame(gameId: number) {
    return this.http.get<Game>(`${environment.apiURL}/join?gameId=${gameId}`);
  }

  connect() {
    this.socket = new WebSocket(environment.WsUrl+"/games");
  }

  listenToChange(callback: (this: WebSocket, ev: Event | CloseEvent | MessageEvent<any>) => any) {
    this.socket?.addEventListener("message", callback);
  }

  disconnect() {
    this.socket?.close();
  }

}
