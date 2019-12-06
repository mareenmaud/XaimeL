import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IExtendedUser, ExtendedUser } from 'app/shared/model/extended-user.model';
import { ExtendedUserService } from './extended-user.service';
import { IITProfile } from 'app/shared/model/it-profile.model';
import { ITProfileService } from 'app/entities/it-profile/it-profile.service';
import { IPsychoProfile } from 'app/shared/model/psycho-profile.model';
import { PsychoProfileService } from 'app/entities/psycho-profile/psycho-profile.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-extended-user-update',
  templateUrl: './extended-user-update.component.html'
})
export class ExtendedUserUpdateComponent implements OnInit {
  isSaving: boolean;

  itprofiles: IITProfile[];

  psychoprofiles: IPsychoProfile[];

  users: IUser[];

  extendedusers: IExtendedUser[];

  editForm = this.fb.group({
    id: [],
    birthDate: [],
    memberSince: [],
    locationLongitude: [],
    locationLatitude: [],
    gender: [],
    interest: [],
    note: [],
    hobbies: [],
    profilePhotoURL: [],
    iTProfile: [],
    psychoProfile: [],
    user: [],
    extendedUser: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected extendedUserService: ExtendedUserService,
    protected iTProfileService: ITProfileService,
    protected psychoProfileService: PsychoProfileService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ extendedUser }) => {
      this.updateForm(extendedUser);
    });
    this.iTProfileService
      .query({ filter: 'extendeduser-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IITProfile[]>) => mayBeOk.ok),
        map((response: HttpResponse<IITProfile[]>) => response.body)
      )
      .subscribe(
        (res: IITProfile[]) => {
          if (!this.editForm.get('iTProfile').value || !this.editForm.get('iTProfile').value.id) {
            this.itprofiles = res;
          } else {
            this.iTProfileService
              .find(this.editForm.get('iTProfile').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IITProfile>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IITProfile>) => subResponse.body)
              )
              .subscribe(
                (subRes: IITProfile) => (this.itprofiles = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.psychoProfileService
      .query({ filter: 'extendeduser-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IPsychoProfile[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPsychoProfile[]>) => response.body)
      )
      .subscribe(
        (res: IPsychoProfile[]) => {
          if (!this.editForm.get('psychoProfile').value || !this.editForm.get('psychoProfile').value.id) {
            this.psychoprofiles = res;
          } else {
            this.psychoProfileService
              .find(this.editForm.get('psychoProfile').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IPsychoProfile>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IPsychoProfile>) => subResponse.body)
              )
              .subscribe(
                (subRes: IPsychoProfile) => (this.psychoprofiles = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.extendedUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IExtendedUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IExtendedUser[]>) => response.body)
      )
      .subscribe((res: IExtendedUser[]) => (this.extendedusers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(extendedUser: IExtendedUser) {
    this.editForm.patchValue({
      id: extendedUser.id,
      birthDate: extendedUser.birthDate != null ? extendedUser.birthDate.format(DATE_TIME_FORMAT) : null,
      memberSince: extendedUser.memberSince != null ? extendedUser.memberSince.format(DATE_TIME_FORMAT) : null,
      locationLongitude: extendedUser.locationLongitude,
      locationLatitude: extendedUser.locationLatitude,
      gender: extendedUser.gender,
      interest: extendedUser.interest,
      note: extendedUser.note,
      hobbies: extendedUser.hobbies,
      profilePhotoURL: extendedUser.profilePhotoURL,
      iTProfile: extendedUser.iTProfile,
      psychoProfile: extendedUser.psychoProfile,
      user: extendedUser.user,
      extendedUser: extendedUser.extendedUser
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const extendedUser = this.createFromForm();
    if (extendedUser.id !== undefined) {
      this.subscribeToSaveResponse(this.extendedUserService.update(extendedUser));
    } else {
      this.subscribeToSaveResponse(this.extendedUserService.create(extendedUser));
    }
  }

  private createFromForm(): IExtendedUser {
    return {
      ...new ExtendedUser(),
      id: this.editForm.get(['id']).value,
      birthDate:
        this.editForm.get(['birthDate']).value != null ? moment(this.editForm.get(['birthDate']).value, DATE_TIME_FORMAT) : undefined,
      memberSince:
        this.editForm.get(['memberSince']).value != null ? moment(this.editForm.get(['memberSince']).value, DATE_TIME_FORMAT) : undefined,
      locationLongitude: this.editForm.get(['locationLongitude']).value,
      locationLatitude: this.editForm.get(['locationLatitude']).value,
      gender: this.editForm.get(['gender']).value,
      interest: this.editForm.get(['interest']).value,
      note: this.editForm.get(['note']).value,
      hobbies: this.editForm.get(['hobbies']).value,
      profilePhotoURL: this.editForm.get(['profilePhotoURL']).value,
      iTProfile: this.editForm.get(['iTProfile']).value,
      psychoProfile: this.editForm.get(['psychoProfile']).value,
      user: this.editForm.get(['user']).value,
      extendedUser: this.editForm.get(['extendedUser']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtendedUser>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackITProfileById(index: number, item: IITProfile) {
    return item.id;
  }

  trackPsychoProfileById(index: number, item: IPsychoProfile) {
    return item.id;
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackExtendedUserById(index: number, item: IExtendedUser) {
    return item.id;
  }
}
