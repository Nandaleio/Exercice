import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';
import { Game } from '../models/game-model';
import { GameService } from '../services/game-service.service';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss'
})
export class GameComponent implements OnInit {

  game?: Game | null;



  constructor(private route: ActivatedRoute,
              private gameService: GameService,
  ) {}

  ngOnInit() {
    const currentGameId = this.route.snapshot.paramMap.get('gameId') || "";
    this.gameService.getGame(currentGameId).subscribe(res => {
      this.game = res;
    })
  }

  

}
