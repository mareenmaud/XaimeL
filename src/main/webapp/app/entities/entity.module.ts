import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'it-profile',
        loadChildren: () => import('./it-profile/it-profile.module').then(m => m.XmLITProfileModule)
      },
      {
        path: 'psycho-profile',
        loadChildren: () => import('./psycho-profile/psycho-profile.module').then(m => m.XmLPsychoProfileModule)
      },
      {
        path: 'message',
        loadChildren: () => import('./message/message.module').then(m => m.XmLMessageModule)
      },
      {
        path: 'conversation',
        loadChildren: () => import('./conversation/conversation.module').then(m => m.XmLConversationModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class XmLEntityModule {}
