import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { PagePresComponent } from 'app/page-pres/page-pres.component';
import { PageProfilComponent } from 'app/page-profil/page-profil.component';
import { PageConnexionComponent } from 'app/page-connexion/page-connexion.component';
import { PageInscriptionComponent } from 'app/page-inscription/page-inscription.component';
import { PageProfilClientComponent } from 'app/page-profil-client/page-profil-client.component';
import { PageConqueteComponent } from 'app/page-conquete/page-conquete.component';
import { StatsComponent } from 'app/stats/stats.component';
import { PageMatchsEnCoursComponent } from 'app/page-matchs-en-cours/page-matchs-en-cours.component';
import { PageInvitationsComponent } from 'app/page-invitations/page-invitations.component';
import { PageParametresComponent } from 'app/page-parametres/page-parametres.component';
import { PageMessagesComponent } from 'app/page-messages/page-messages.component';
import { PageReglementComponent } from 'app/page-reglement/page-reglement.component';
import { PageMdpPerduComponent } from 'app/page-mdp-perdu/page-mdp-perdu.component';
import { PageProfilPsychoComponent } from 'app/page-profil-psycho/page-profil-psycho.component';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: 'admin',
          data: {
            authorities: ['ROLE_ADMIN']
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule)
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.XmLAccountModule)
        },
        { path: 'presentation', component: PagePresComponent },
        { path: 'equipe', component: PageProfilComponent },
        { path: 'connexion', component: PageConnexionComponent },
        { path: 'inscription', component: PageInscriptionComponent },
        { path: 'profil', component: PageProfilClientComponent },
        { path: 'conquete', component: PageConqueteComponent },
        { path: 'stats', component: StatsComponent },
        { path: 'matchsencours', component: PageMatchsEnCoursComponent },
        { path: 'invitations', component: PageInvitationsComponent },
        { path: 'parametres', component: PageParametresComponent },
        { path: 'messages', component: PageMessagesComponent },
        { path: 'mdp', component: PageMdpPerduComponent },
        { path: 'reglement', component: PageReglementComponent },
        { path: 'psy', component: PageProfilPsychoComponent },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class XmLAppRoutingModule {}
