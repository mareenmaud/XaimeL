import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IConversation } from 'app/shared/model/conversation.model';
import { IMessage } from 'app/shared/model/message.model';

type EntityResponseType = HttpResponse<IConversation>;
type EntityArrayResponseType = HttpResponse<IConversation[]>;

@Injectable({ providedIn: 'root' })
export class ConversationService {
  public resourceUrl = SERVER_API_URL + 'api/conversations';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/conversations';
  public resourceUrlCreateConv = SERVER_API_URL + 'api/createconversation';
  public resourceUrlFindConv = SERVER_API_URL + 'api/conversationuser';
  public resourceUrlAddMessage = SERVER_API_URL + ' api/ajoutermessage';

  constructor(protected http: HttpClient) {}

  create(conversation: IConversation): Observable<EntityResponseType> {
    return this.http.post<IConversation>(this.resourceUrl, conversation, { observe: 'response' });
  }

  update(conversation: IConversation): Observable<EntityResponseType> {
    return this.http.put<IConversation>(this.resourceUrl, conversation, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IConversation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConversation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConversation[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  //  mes services:
  //  créer conversation avec id des 2 users
  createConv(idUser1: string, idUser2: string): Observable<EntityResponseType> {
    return this.http.post<any>(`${this.resourceUrlCreateConv}?id_user1=${idUser1}id_user2=${idUser2}`, { observe: 'response' });
  }

  //   trouver une conversation a partir des id des deux utilisateurs
  findConv(idUser1: string, idUser2: string): Observable<EntityResponseType> {
    return this.http.get<IConversation>(`${this.resourceUrlFindConv}?id_user1=${idUser1}id_user2=${idUser2}`, { observe: 'response' });
  }

  // ********* ajouter un message ou le mettre à jour (pour les notifs)
  addMessage(message: IMessage): Observable<EntityResponseType> {
    return this.http.put<IConversation>(this.resourceUrlAddMessage, message, { observe: 'response' });
  }
}
