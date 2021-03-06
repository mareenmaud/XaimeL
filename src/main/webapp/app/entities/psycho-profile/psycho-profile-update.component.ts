import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPsychoProfile, PsychoProfile } from 'app/shared/model/psycho-profile.model';
import { PsychoProfileService } from './psycho-profile.service';

@Component({
  selector: 'jhi-psycho-profile-update',
  templateUrl: './psycho-profile-update.component.html'
})
export class PsychoProfileUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    summaryProfile: [],
    jungValue1: [],
    jungValue2: [],
    jungValue3: [],
    jungValue4: []
  });

  constructor(protected psychoProfileService: PsychoProfileService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ psychoProfile }) => {
      this.updateForm(psychoProfile);
    });
  }

  updateForm(psychoProfile: IPsychoProfile) {
    this.editForm.patchValue({
      id: psychoProfile.id,
      summaryProfile: psychoProfile.summaryProfile,
      jungValue1: psychoProfile.jungValue1,
      jungValue2: psychoProfile.jungValue2,
      jungValue3: psychoProfile.jungValue3,
      jungValue4: psychoProfile.jungValue4
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const psychoProfile = this.createFromForm();
    if (psychoProfile.id !== undefined) {
      this.subscribeToSaveResponse(this.psychoProfileService.update(psychoProfile));
    } else {
      this.subscribeToSaveResponse(this.psychoProfileService.create(psychoProfile));
    }
  }

  private createFromForm(): IPsychoProfile {
    return {
      ...new PsychoProfile(),
      id: this.editForm.get(['id']).value,
      summaryProfile: this.editForm.get(['summaryProfile']).value,
      jungValue1: this.editForm.get(['jungValue1']).value,
      jungValue2: this.editForm.get(['jungValue2']).value,
      jungValue3: this.editForm.get(['jungValue3']).value,
      jungValue4: this.editForm.get(['jungValue4']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPsychoProfile>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
