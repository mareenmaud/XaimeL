import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IExtendedUser } from 'app/shared/model/extended-user.model';
import { AccountService } from 'app/core/auth/account.service';
import { ExtendedUserService } from './extended-user.service';

@Component({
  selector: 'jhi-extended-user',
  templateUrl: './extended-user.component.html'
})
export class ExtendedUserComponent implements OnInit, OnDestroy {
  extendedUsers: IExtendedUser[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected extendedUserService: ExtendedUserService,
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
      this.extendedUserService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IExtendedUser[]>) => res.ok),
          map((res: HttpResponse<IExtendedUser[]>) => res.body)
        )
        .subscribe((res: IExtendedUser[]) => (this.extendedUsers = res));
      return;
    }
    this.extendedUserService
      .query()
      .pipe(
        filter((res: HttpResponse<IExtendedUser[]>) => res.ok),
        map((res: HttpResponse<IExtendedUser[]>) => res.body)
      )
      .subscribe((res: IExtendedUser[]) => {
        this.extendedUsers = res;
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
    this.registerChangeInExtendedUsers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IExtendedUser) {
    return item.id;
  }

  registerChangeInExtendedUsers() {
    this.eventSubscriber = this.eventManager.subscribe('extendedUserListModification', response => this.loadAll());
  }
}
