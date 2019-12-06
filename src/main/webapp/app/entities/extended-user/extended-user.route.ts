import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ExtendedUser } from 'app/shared/model/extended-user.model';
import { ExtendedUserService } from './extended-user.service';
import { ExtendedUserComponent } from './extended-user.component';
import { ExtendedUserDetailComponent } from './extended-user-detail.component';
import { ExtendedUserUpdateComponent } from './extended-user-update.component';
import { ExtendedUserDeletePopupComponent } from './extended-user-delete-dialog.component';
import { IExtendedUser } from 'app/shared/model/extended-user.model';

@Injectable({ providedIn: 'root' })
export class ExtendedUserResolve implements Resolve<IExtendedUser> {
  constructor(private service: ExtendedUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExtendedUser> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ExtendedUser>) => response.ok),
        map((extendedUser: HttpResponse<ExtendedUser>) => extendedUser.body)
      );
    }
    return of(new ExtendedUser());
  }
}

export const extendedUserRoute: Routes = [
  {
    path: '',
    component: ExtendedUserComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExtendedUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExtendedUserDetailComponent,
    resolve: {
      extendedUser: ExtendedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExtendedUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExtendedUserUpdateComponent,
    resolve: {
      extendedUser: ExtendedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExtendedUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExtendedUserUpdateComponent,
    resolve: {
      extendedUser: ExtendedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExtendedUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const extendedUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ExtendedUserDeletePopupComponent,
    resolve: {
      extendedUser: ExtendedUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ExtendedUsers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
