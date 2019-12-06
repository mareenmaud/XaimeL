import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPsychoProfile } from 'app/shared/model/psycho-profile.model';
import { PsychoProfileService } from './psycho-profile.service';

@Component({
  selector: 'jhi-psycho-profile-delete-dialog',
  templateUrl: './psycho-profile-delete-dialog.component.html'
})
export class PsychoProfileDeleteDialogComponent {
  psychoProfile: IPsychoProfile;

  constructor(
    protected psychoProfileService: PsychoProfileService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.psychoProfileService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'psychoProfileListModification',
        content: 'Deleted an psychoProfile'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-psycho-profile-delete-popup',
  template: ''
})
export class PsychoProfileDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ psychoProfile }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PsychoProfileDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.psychoProfile = psychoProfile;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/psycho-profile', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/psycho-profile', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
