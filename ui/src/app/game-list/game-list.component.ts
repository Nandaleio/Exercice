import { Component, OnInit } from '@angular/core';
import { GameService } from '../services/game-service.service';
import { Game } from '../models/game-model';

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.scss'
})
export class GameListComponent implements OnInit {

  games: Game[] = [];

  currentGame?: Game;
  playerName?: string;

  constructor(private gameService: GameService) {

  }

  ngOnInit(): void {
    this.gameService.getGameList().subscribe(res => {
      this.games = res;
    })
  }

  
  joinGame() {
    if(!this.currentGame || !this.playerName) return;

    this.gameService.joinGame(this.currentGame.id, this.playerName);
  }

}
