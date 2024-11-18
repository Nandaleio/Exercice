import { Injectable } from '@angular/core';
import { Rule } from '../models/rule-model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Game } from '../models/game-model';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  constructor(private http: HttpClient) { }


  
  getRules() {
    return this.http.get<Rule[]>(environment.apiURL+'/rules');
  }

  createGame(gameRuleId: number, name: string) {
    return this.http.post(environment.apiURL+'/new', {gameRuleId, name});
  }

  getGameList() {
    return this.http.get<Game[]>(environment.apiURL+'/games');
  }

  joinGame(gameId: number, name: string) {
    return this.http.post<Game[]>(environment.apiURL+'/join', {gameId, name});
  }
  

}
