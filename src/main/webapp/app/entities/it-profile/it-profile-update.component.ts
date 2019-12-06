import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IITProfile, ITProfile } from 'app/shared/model/it-profile.model';
import { ITProfileService } from './it-profile.service';

@Component({
  selector: 'jhi-it-profile-update',
  templateUrl: './it-profile-update.component.html'
})
export class ITProfileUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    job: [],
    favLanguages: [],
    favOS: [],
    gamer: [],
    geek: [],
    otaku: []
  });

  constructor(protected iTProfileService: ITProfileService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ iTProfile }) => {
      this.updateForm(iTProfile);
    });
  }

  updateForm(iTProfile: IITProfile) {
    this.editForm.patchValue({
      id: iTProfile.id,
      job: iTProfile.job,
      favLanguages: iTProfile.favLanguages,
      favOS: iTProfile.favOS,
      gamer: iTProfile.gamer,
      geek: iTProfile.geek,
      otaku: iTProfile.otaku
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const iTProfile = this.createFromForm();
    if (iTProfile.id !== undefined) {
      this.subscribeToSaveResponse(this.iTProfileService.update(iTProfile));
    } else {
      this.subscribeToSaveResponse(this.iTProfileService.create(iTProfile));
    }
  }

  private createFromForm(): IITProfile {
    return {
      ...new ITProfile(),
      id: this.editForm.get(['id']).value,
      job: this.editForm.get(['job']).value,
      favLanguages: this.editForm.get(['favLanguages']).value,
      favOS: this.editForm.get(['favOS']).value,
      gamer: this.editForm.get(['gamer']).value,
      geek: this.editForm.get(['geek']).value,
      otaku: this.editForm.get(['otaku']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IITProfile>>) {
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
