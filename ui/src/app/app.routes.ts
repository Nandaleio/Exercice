import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SignInComponent } from './auth/signin.component';
import { authGuard } from './auth/auth.guard';
import { RegisterComponent } from './auth/register.component';

export const routes: Routes = [
    {path: 'register', component: RegisterComponent},
    {path: 'login', component: SignInComponent},
    {
        path: '',
        canActivate: [authGuard],
        children: [
            {path: 'dashboard',  component: DashboardComponent},
            {path: 'game/:gameId', loadComponent: () => import('./game/game.component').then(m => m.GameComponent) },
            
            { path: '**', redirectTo:'dashboard', pathMatch: 'full' }
        ]
    },
];
