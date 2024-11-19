import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { GameComponent } from './game/game.component';
import { AuthComponent } from './auth/auth.component';

export const routes: Routes = [
    {path: 'login', component: AuthComponent},
    {children: [
        {path: 'dashboard', canActivate:undefined, component: DashboardComponent},
        {path: 'game/:gameId', component: GameComponent},
    ]},
    { path: '**', redirectTo:'dashboard' }
];
