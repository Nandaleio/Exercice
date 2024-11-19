import { Component, ElementRef, OnInit, viewChild } from '@angular/core';
import { GameService } from '../services/game-service.service';
import { Rule } from '../models/rule-model';
import { GameListComponent } from "./game-list/game-list.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [GameListComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit{

  rules: Rule[] = []
  selectedRule?:Rule;
  gameName:string = "";

  createGameDialog = viewChild.required<ElementRef>("createGameDialog");

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.gameService.getRules().subscribe(res => {
      this.rules = res;
    });
  }

  createGame() {
    if(!this.selectedRule || !this.gameName) return;

    this.gameService.createGame(this.selectedRule.id, this.gameName).subscribe(res => {
      this.selectedRule = undefined;
      this.gameName = "";
      this.createGameDialog().nativeElement.open = false;
    })
  }

}
