import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XmLSharedModule } from 'app/shared/shared.module';
import { PsychoProfileComponent } from './psycho-profile.component';
import { PsychoProfileDetailComponent } from './psycho-profile-detail.component';
import { PsychoProfileUpdateComponent } from './psycho-profile-update.component';
import { PsychoProfileDeletePopupComponent, PsychoProfileDeleteDialogComponent } from './psycho-profile-delete-dialog.component';
import { psychoProfileRoute, psychoProfilePopupRoute } from './psycho-profile.route';

const ENTITY_STATES = [...psychoProfileRoute, ...psychoProfilePopupRoute];

@NgModule({
  imports: [XmLSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PsychoProfileComponent,
    PsychoProfileDetailComponent,
    PsychoProfileUpdateComponent,
    PsychoProfileDeleteDialogComponent,
    PsychoProfileDeletePopupComponent
  ],
  entryComponents: [PsychoProfileDeleteDialogComponent]
})
export class XmLPsychoProfileModule {}
