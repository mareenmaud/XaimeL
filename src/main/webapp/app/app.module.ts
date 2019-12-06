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
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class XmLAppModule {}
