import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IITProfile } from 'app/shared/model/it-profile.model';

@Component({
  selector: 'jhi-it-profile-detail',
  templateUrl: './it-profile-detail.component.html'
})
export class ITProfileDetailComponent implements OnInit {
  iTProfile: IITProfile;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ iTProfile }) => {
      this.iTProfile = iTProfile;
    });
  }

  previousState() {
    window.history.back();
  }
}
