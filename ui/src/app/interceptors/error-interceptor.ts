import { HttpEvent, HttpHandlerFn, HttpRequest, HttpResponse } from "@angular/common/http";
import { map, Observable } from "rxjs";

import Toastify from 'toastify-js'

export function errorInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {
    return next(req).pipe(
        map((event: HttpEvent<any>) => {
            if (event instanceof HttpResponse) {
                if(!event.ok) {
                    Toastify({
                        text: event.body.error,
                        duration: 4000
                    }).showToast();
                }
            }
            return event;
        }
    ));
  }