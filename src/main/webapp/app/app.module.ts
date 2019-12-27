import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { XmLSharedModule } from 'app/shared/shared.module';
import { XmLCoreModule } from 'app/core/core.module';
import { XmLAppRoutingModule } from './app-routing.module';
import { XmLHomeModule } from './home/home.module';
import { XmLEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { PageConnexionComponent } from './page-connexion/page-connexion.component';
import { PageConqueteComponent } from './page-conquete/page-conquete.component';
import { PageInscriptionComponent } from './page-inscription/page-inscription.component';
import { PageInvitationsComponent } from './page-invitations/page-invitations.component';
import { PageMatchsEnCoursComponent } from './page-matchs-en-cours/page-matchs-en-cours.component';
import { PageMessagesComponent } from './page-messages/page-messages.component';
import { PageParametresComponent } from './page-parametres/page-parametres.component';
import { PagePresComponent } from './page-pres/page-pres.component';
import { PageProfilComponent } from './page-profil/page-profil.component';
import { PageProfilClientComponent } from './page-profil-client/page-profil-client.component';
import { PageReglementComponent } from './page-reglement/page-reglement.component';
import { StatsComponent } from './stats/stats.component';
import { ServiceStatsService } from './service-stats.service';

@NgModule({
  imports: [
    BrowserModule,
    XmLSharedModule,
    XmLCoreModule,
    XmLHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    XmLEntityModule,
    XmLAppRoutingModule
  ],
  providers: [ServiceStatsService],

  declarations: [
    JhiMainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    FooterComponent,

    PageConnexionComponent,
    PageConqueteComponent,
    PageInscriptionComponent,
    PageInvitationsComponent,
    PageMatchsEnCoursComponent,
    PageMessagesComponent,
    PageParametresComponent,
    PagePresComponent,
    PageProfilComponent,
    PageProfilClientComponent,
    PageReglementComponent,
    StatsComponent
  ],
  bootstrap: [JhiMainComponent]
})
export class XmLAppModule {}
