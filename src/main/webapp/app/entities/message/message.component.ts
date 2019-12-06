import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IMessage } from 'app/shared/model/message.model';
import { AccountService } from 'app/core/auth/account.service';
import { MessageService } from './message.service';

@Component({
  selector: 'jhi-message',
  templateUrl: './message.component.html'
})
export class MessageComponent implements OnInit, OnDestroy {
  messages: IMessage[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected messageService: MessageService,
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
      this.messageService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IMessage[]>) => res.ok),
          map((res: HttpResponse<IMessage[]>) => res.body)
        )
        .subscribe((res: IMessage[]) => (this.messages = res));
      return;
    }
    this.messageService
      .query()
      .pipe(
        filter((res: HttpResponse<IMessage[]>) => res.ok),
        map((res: HttpResponse<IMessage[]>) => res.body)
      )
      .subscribe((res: IMessage[]) => {
        this.messages = res;
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
    this.registerChangeInMessages();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMessage) {
    return item.id;
  }

  registerChangeInMessages() {
    this.eventSubscriber = this.eventManager.subscribe('messageListModification', response => this.loadAll());
  }
}
