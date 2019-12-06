import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPsychoProfile } from 'app/shared/model/psycho-profile.model';

@Component({
  selector: 'jhi-psycho-profile-detail',
  templateUrl: './psycho-profile-detail.component.html'
})
export class PsychoProfileDetailComponent implements OnInit {
  psychoProfile: IPsychoProfile;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ psychoProfile }) => {
      this.psychoProfile = psychoProfile;
    });
  }

  previousState() {
    window.history.back();
  }
}
