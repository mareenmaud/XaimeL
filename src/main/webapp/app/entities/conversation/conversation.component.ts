import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IConversation } from 'app/shared/model/conversation.model';
import { AccountService } from 'app/core/auth/account.service';
import { ConversationService } from './conversation.service';

@Component({
  selector: 'jhi-conversation',
  templateUrl: './conversation.component.html'
})
export class ConversationComponent implements OnInit, OnDestroy {
  conversations: IConversation[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected conversationService: ConversationService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.conversationService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IConversation[]>) => res.ok),
          map((res: HttpResponse<IConversation[]>) => res.body)
        )
        .subscribe((res: IConversation[]) => (this.conversations = res));
      return;
    }
    this.conversationService
      .query()
      .pipe(
        filter((res: HttpResponse<IConversation[]>) => res.ok),
        map((res: HttpResponse<IConversation[]>) => res.body)
      )
      .subscribe((res: IConversation[]) => {
        this.conversations = res;
        this.currentSearch = '';
      });
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInConversations();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IConversation) {
    return item.id;
  }

  registerChangeInConversations() {
    this.eventSubscriber = this.eventManager.subscribe('conversationListModification', response => this.loadAll());
  }
}
