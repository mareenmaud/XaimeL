import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ITProfile } from 'app/shared/model/it-profile.model';
import { ITProfileService } from './it-profile.service';
import { ITProfileComponent } from './it-profile.component';
import { ITProfileDetailComponent } from './it-profile-detail.component';
import { ITProfileUpdateComponent } from './it-profile-update.component';
import { ITProfileDeletePopupComponent } from './it-profile-delete-dialog.component';
import { IITProfile } from 'app/shared/model/it-profile.model';

@Injectable({ providedIn: 'root' })
export class ITProfileResolve implements Resolve<IITProfile> {
  constructor(private service: ITProfileService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IITProfile> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ITProfile>) => response.ok),
        map((iTProfile: HttpResponse<ITProfile>) => iTProfile.body)
      );
    }
    return of(new ITProfile());
  }
}

export const iTProfileRoute: Routes = [
  {
    path: '',
    component: ITProfileComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ITProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ITProfileDetailComponent,
    resolve: {
      iTProfile: ITProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ITProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ITProfileUpdateComponent,
    resolve: {
      iTProfile: ITProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ITProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ITProfileUpdateComponent,
    resolve: {
      iTProfile: ITProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ITProfiles'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const iTProfilePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ITProfileDeletePopupComponent,
    resolve: {
      iTProfile: ITProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ITProfiles'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
