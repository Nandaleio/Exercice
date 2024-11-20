import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs';
import { Game, Player } from '../models/game-model';
import { GameService } from '../services/game-service.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-game',
  standalone: true,
  imports: [],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss'
})
export class GameComponent implements OnInit {
  
  game?: Game | null;
  userPayload: any;

  constructor(private route: ActivatedRoute,
              private gameService: GameService,
              private authService: AuthService,
  ) {}

  ngOnInit() {
    const currentGameId = this.route.snapshot.paramMap.get('gameId') || "";
    this.gameService.getGame(currentGameId).subscribe(res => {
      this.game = res;
    })
    this.userPayload = this.authService.getPayload();

  }

  getPlayerGame(player: Player) {
    return this.game?.frames.filter(f => f.player.id === player.id).sort((f1,f2) => f1.frameNumber - f2.frameNumber);
  }

  roll(){
    this.gameService.roll(this.game?.id || -1).subscribe(res => {
      this.game?.frames.find(f => f.player.id === this.userPayload.id)
    })
  }

}
