import { HttpEvent, HttpHandlerFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { Observable } from "rxjs";
import { AuthService } from "../services/auth.service";


export function authTokenInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    const token = inject(AuthService).getToken();

    if(!token) return next(req);

    req = req.clone({
        setHeaders: {Authorization: `Bearer ${token}`}
     });
    return next(req);
  }