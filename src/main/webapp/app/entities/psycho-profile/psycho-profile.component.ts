import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IPsychoProfile } from 'app/shared/model/psycho-profile.model';
import { AccountService } from 'app/core/auth/account.service';
import { PsychoProfileService } from './psycho-profile.service';

@Component({
  selector: 'jhi-psycho-profile',
  templateUrl: './psycho-profile.component.html'
})
export class PsychoProfileComponent implements OnInit, OnDestroy {
  psychoProfiles: IPsychoProfile[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected psychoProfileService: PsychoProfileService,
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
      this.psychoProfileService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IPsychoProfile[]>) => res.ok),
          map((res: HttpResponse<IPsychoProfile[]>) => res.body)
        )
        .subscribe((res: IPsychoProfile[]) => (this.psychoProfiles = res));
      return;
    }
    this.psychoProfileService
      .query()
      .pipe(
        filter((res: HttpResponse<IPsychoProfile[]>) => res.ok),
        map((res: HttpResponse<IPsychoProfile[]>) => res.body)
      )
      .subscribe((res: IPsychoProfile[]) => {
        this.psychoProfiles = res;
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
    this.registerChangeInPsychoProfiles();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IPsychoProfile) {
    return item.id;
  }

  registerChangeInPsychoProfiles() {
    this.eventSubscriber = this.eventManager.subscribe('psychoProfileListModification', response => this.loadAll());
  }
}
