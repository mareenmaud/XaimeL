import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XmLSharedModule } from 'app/shared/shared.module';
import { ExtendedUserComponent } from './extended-user.component';
import { ExtendedUserDetailComponent } from './extended-user-detail.component';
import { ExtendedUserUpdateComponent } from './extended-user-update.component';
import { ExtendedUserDeletePopupComponent, ExtendedUserDeleteDialogComponent } from './extended-user-delete-dialog.component';
import { extendedUserRoute, extendedUserPopupRoute } from './extended-user.route';

const ENTITY_STATES = [...extendedUserRoute, ...extendedUserPopupRoute];

@NgModule({
  imports: [XmLSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ExtendedUserComponent,
    ExtendedUserDetailComponent,
    ExtendedUserUpdateComponent,
    ExtendedUserDeleteDialogComponent,
    ExtendedUserDeletePopupComponent
  ],
  entryComponents: [ExtendedUserDeleteDialogComponent]
})
export class XmLExtendedUserModule {}
