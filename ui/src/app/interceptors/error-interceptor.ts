import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse } from "@angular/common/http";
import { Observable, tap } from "rxjs";

import Toastify from 'toastify-js'

export function errorInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    return next(req).pipe(
        tap((event: HttpEvent<any>) => {
            if (event instanceof HttpErrorResponse) {
                if(!event.ok) {
                    Toastify({
                        text: event.message,
                        duration: 4000
                    }).showToast();
                }
            }
            return event;
        }
    ));
  }