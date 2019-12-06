import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IITProfile } from 'app/shared/model/it-profile.model';

type EntityResponseType = HttpResponse<IITProfile>;
type EntityArrayResponseType = HttpResponse<IITProfile[]>;

@Injectable({ providedIn: 'root' })
export class ITProfileService {
  public resourceUrl = SERVER_API_URL + 'api/it-profiles';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/it-profiles';

  constructor(protected http: HttpClient) {}

  create(iTProfile: IITProfile): Observable<EntityResponseType> {
    return this.http.post<IITProfile>(this.resourceUrl, iTProfile, { observe: 'response' });
  }

  update(iTProfile: IITProfile): Observable<EntityResponseType> {
    return this.http.put<IITProfile>(this.resourceUrl, iTProfile, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IITProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IITProfile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IITProfile[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
