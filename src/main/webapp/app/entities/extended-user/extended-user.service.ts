import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExtendedUser } from 'app/shared/model/extended-user.model';

type EntityResponseType = HttpResponse<IExtendedUser>;
type EntityArrayResponseType = HttpResponse<IExtendedUser[]>;

@Injectable({ providedIn: 'root' })
export class ExtendedUserService {
  public resourceUrl = SERVER_API_URL + 'api/extended-users';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/extended-users';

  constructor(protected http: HttpClient) {}

  create(extendedUser: IExtendedUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extendedUser);
    return this.http
      .post<IExtendedUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(extendedUser: IExtendedUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(extendedUser);
    return this.http
      .put<IExtendedUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IExtendedUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExtendedUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExtendedUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(extendedUser: IExtendedUser): IExtendedUser {
    const copy: IExtendedUser = Object.assign({}, extendedUser, {
      birthDate: extendedUser.birthDate != null && extendedUser.birthDate.isValid() ? extendedUser.birthDate.toJSON() : null,
      memberSince: extendedUser.memberSince != null && extendedUser.memberSince.isValid() ? extendedUser.memberSince.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.birthDate = res.body.birthDate != null ? moment(res.body.birthDate) : null;
      res.body.memberSince = res.body.memberSince != null ? moment(res.body.memberSince) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((extendedUser: IExtendedUser) => {
        extendedUser.birthDate = extendedUser.birthDate != null ? moment(extendedUser.birthDate) : null;
        extendedUser.memberSince = extendedUser.memberSince != null ? moment(extendedUser.memberSince) : null;
      });
    }
    return res;
  }
}
