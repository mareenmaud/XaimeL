import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IITProfile } from 'app/shared/model/it-profile.model';
import { ITProfileService } from './it-profile.service';

@Component({
  selector: 'jhi-it-profile-delete-dialog',
  templateUrl: './it-profile-delete-dialog.component.html'
})
export class ITProfileDeleteDialogComponent {
  iTProfile: IITProfile;

  constructor(protected iTProfileService: ITProfileService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: string) {
    this.iTProfileService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'iTProfileListModification',
        content: 'Deleted an iTProfile'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-it-profile-delete-popup',
  template: ''
})
export class ITProfileDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ iTProfile }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ITProfileDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.iTProfile = iTProfile;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/it-profile', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/it-profile', { outlets: { popup: null } }]);
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
