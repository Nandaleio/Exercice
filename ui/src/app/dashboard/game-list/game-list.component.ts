import { Component, OnDestroy, OnInit } from '@angular/core';
import { GameService } from '../../services/game-service.service';
import { Game } from '../../models/game-model';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import Toastify from 'toastify-js';

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.scss'
})
export class GameListComponent implements OnInit, OnDestroy {

  games: Game[] = [];

  currentGame?: Game;
  playerName?: string;

  subscription?: Subscription;

  constructor(private gameService: GameService,
              private router: Router
  ) {}


  ngOnInit(): void {
    this.gameService.getGameList().subscribe(res => {
      this.games = res;
    })
    this.subscription = this.gameService.subject.subscribe(data => {
      switch (data.type) {
        case 'create':
          this.games.push(data.game as Game);
          break;
        case 'delete':
          const index = this.games.findIndex(g => g.id === data.game as number);
          if (index > -1) {
            this.games.splice(index, 1);
          }
          break;
      }
    });
  }

  joinGame() {
    if(!this.currentGame || !this.playerName) return;
    this.gameService.joinGame(this.currentGame.id).subscribe(res => {
      this.router.navigate(["game", res.id]);
    });
  }

  deleteGame(game: Game) {
    if(confirm("Are you sure you want to delete this game ?")) {
      this.gameService.deleteGame(game.id).subscribe();
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

}
