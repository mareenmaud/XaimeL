import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IITProfile } from 'app/shared/model/it-profile.model';
import { AccountService } from 'app/core/auth/account.service';
import { ITProfileService } from './it-profile.service';

@Component({
  selector: 'jhi-it-profile',
  templateUrl: './it-profile.component.html'
})
export class ITProfileComponent implements OnInit, OnDestroy {
  iTProfiles: IITProfile[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected iTProfileService: ITProfileService,
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
      this.iTProfileService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IITProfile[]>) => res.ok),
          map((res: HttpResponse<IITProfile[]>) => res.body)
        )
        .subscribe((res: IITProfile[]) => (this.iTProfiles = res));
      return;
    }
    this.iTProfileService
      .query()
      .pipe(
        filter((res: HttpResponse<IITProfile[]>) => res.ok),
        map((res: HttpResponse<IITProfile[]>) => res.body)
      )
      .subscribe((res: IITProfile[]) => {
        this.iTProfiles = res;
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
    this.registerChangeInITProfiles();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IITProfile) {
    return item.id;
  }

  registerChangeInITProfiles() {
    this.eventSubscriber = this.eventManager.subscribe('iTProfileListModification', response => this.loadAll());
  }
}
