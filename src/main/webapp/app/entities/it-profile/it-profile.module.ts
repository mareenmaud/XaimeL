import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XmLSharedModule } from 'app/shared/shared.module';
import { ITProfileComponent } from './it-profile.component';
import { ITProfileDetailComponent } from './it-profile-detail.component';
import { ITProfileUpdateComponent } from './it-profile-update.component';
import { ITProfileDeletePopupComponent, ITProfileDeleteDialogComponent } from './it-profile-delete-dialog.component';
import { iTProfileRoute, iTProfilePopupRoute } from './it-profile.route';

const ENTITY_STATES = [...iTProfileRoute, ...iTProfilePopupRoute];

@NgModule({
  imports: [XmLSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ITProfileComponent,
    ITProfileDetailComponent,
    ITProfileUpdateComponent,
    ITProfileDeleteDialogComponent,
    ITProfileDeletePopupComponent
  ],
  entryComponents: [ITProfileDeleteDialogComponent]
})
export class XmLITProfileModule {}
