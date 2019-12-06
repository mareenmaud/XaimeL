import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { XmLSharedModule } from 'app/shared/shared.module';
import { ConversationComponent } from './conversation.component';
import { ConversationDetailComponent } from './conversation-detail.component';
import { ConversationUpdateComponent } from './conversation-update.component';
import { ConversationDeletePopupComponent, ConversationDeleteDialogComponent } from './conversation-delete-dialog.component';
import { conversationRoute, conversationPopupRoute } from './conversation.route';

const ENTITY_STATES = [...conversationRoute, ...conversationPopupRoute];

@NgModule({
  imports: [XmLSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ConversationComponent,
    ConversationDetailComponent,
    ConversationUpdateComponent,
    ConversationDeleteDialogComponent,
    ConversationDeletePopupComponent
  ],
  entryComponents: [ConversationDeleteDialogComponent]
})
export class XmLConversationModule {}
