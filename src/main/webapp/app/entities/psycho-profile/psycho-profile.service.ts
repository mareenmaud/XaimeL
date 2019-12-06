import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPsychoProfile } from 'app/shared/model/psycho-profile.model';

type EntityResponseType = HttpResponse<IPsychoProfile>;
type EntityArrayResponseType = HttpResponse<IPsychoProfile[]>;

@Injectable({ providedIn: 'root' })
export class PsychoProfileService {
  public resourceUrl = SERVER_API_URL + 'api/psycho-profiles';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/psycho-profiles';

  constructor(protected http: HttpClient) {}

  create(psychoProfile: IPsychoProfile): Observable<EntityResponseType> {
    return this.http.post<IPsychoProfile>(this.resourceUrl, psychoProfile, { observe: 'response' });
  }

  update(psychoProfile: IPsychoProfile): Observable<EntityResponseType> {
    return this.http.put<IPsychoProfile>(this.resourceUrl, psychoProfile, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPsychoProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPsychoProfile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPsychoProfile[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
