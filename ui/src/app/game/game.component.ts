import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Frame, Game, Player } from '../models/game-model';
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

  constructor(private route: ActivatedRoute,
              private gameService: GameService,
              private authService: AuthService,
  ) {}

  ngOnInit() {
    const currentGameId = this.route.snapshot.paramMap.get('gameId') || "";
    this.gameService.getGame(currentGameId).subscribe(res => {
      this.game = res;
    });

  }

  getPlayerGame(player: Player) {
    return this.game?.frames.filter(f => f.player.id === player.id).sort((f1,f2) => f1.frameNumber - f2.frameNumber);
  }

  roll(){
    this.gameService.roll(this.game?.id || -1, -1).subscribe(res => {
      this.game = res;
    });
  }

  rollInput(pins?: number) {
    if(!pins) pins = +prompt('How many pins ?')!;

    this.gameService.roll(this.game?.id || -1, pins).subscribe(res => {
      this.game = res;
    });
  }
  
  getRollSum(frame: Frame) {
    return frame.rolls.reduce((partialSum, a) => partialSum + a, 0)
  }
  
  getRoll(frame: Frame, roll: number | null, index: number): number|string|null {
    if(index === 0 && roll === this.game?.rule.maxPins) return 'X';
    const previousPins = frame.rolls.filter((v,i) => i<=index).reduce((totale,v) => totale+=v, 0)
    if(index !== 0 &&  previousPins === this.game?.rule.maxPins) {
      return (roll === null) ? '' : '/';
    }
    return roll;
  }

}
