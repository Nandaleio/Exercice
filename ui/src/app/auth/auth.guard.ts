import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = async (route, state) => {
  const auth = inject(AuthService)
  const router = inject(Router)
  const ret = await auth.checkLoggedIn();
  return ret ? true : router.createUrlTree(['/auth']);
};

