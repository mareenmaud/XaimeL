import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { PsychoProfile } from 'app/shared/model/psycho-profile.model';
import { PsychoProfileService } from './psycho-profile.service';
import { PsychoProfileComponent } from './psycho-profile.component';
import { PsychoProfileDetailComponent } from './psycho-profile-detail.component';
import { PsychoProfileUpdateComponent } from './psycho-profile-update.component';
import { PsychoProfileDeletePopupComponent } from './psycho-profile-delete-dialog.component';
import { IPsychoProfile } from 'app/shared/model/psycho-profile.model';

@Injectable({ providedIn: 'root' })
export class PsychoProfileResolve implements Resolve<IPsychoProfile> {
  constructor(private service: PsychoProfileService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPsychoProfile> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<PsychoProfile>) => response.ok),
        map((psychoProfile: HttpResponse<PsychoProfile>) => psychoProfile.body)
      );
    }
    return of(new PsychoProfile());
  }
}

export const psychoProfileRoute: Routes = [
  {
    path: '',
    component: PsychoProfileComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PsychoProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PsychoProfileDetailComponent,
    resolve: {
      psychoProfile: PsychoProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PsychoProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PsychoProfileUpdateComponent,
    resolve: {
      psychoProfile: PsychoProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PsychoProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PsychoProfileUpdateComponent,
    resolve: {
      psychoProfile: PsychoProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PsychoProfiles'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const psychoProfilePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: PsychoProfileDeletePopupComponent,
    resolve: {
      psychoProfile: PsychoProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'PsychoProfiles'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
