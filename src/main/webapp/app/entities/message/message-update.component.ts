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
import { IMessage, Message } from 'app/shared/model/message.model';
import { MessageService } from './message.service';
import { IConversation } from 'app/shared/model/conversation.model';
import { ConversationService } from 'app/entities/conversation/conversation.service';

@Component({
  selector: 'jhi-message-update',
  templateUrl: './message-update.component.html'
})
export class MessageUpdateComponent implements OnInit {
  isSaving: boolean;

  conversations: IConversation[];

  editForm = this.fb.group({
    id: [],
    idUserSender: [],
    idUserRecipient: [],
    contentMessage: [],
    dateMessage: [],
    readMessage: [],
    conversation: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected messageService: MessageService,
    protected conversationService: ConversationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ message }) => {
      this.updateForm(message);
    });
    this.conversationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IConversation[]>) => mayBeOk.ok),
        map((response: HttpResponse<IConversation[]>) => response.body)
      )
      .subscribe((res: IConversation[]) => (this.conversations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(message: IMessage) {
    this.editForm.patchValue({
      id: message.id,
      idUserSender: message.idUserSender,
      idUserRecipient: message.idUserRecipient,
      contentMessage: message.contentMessage,
      dateMessage: message.dateMessage != null ? message.dateMessage.format(DATE_TIME_FORMAT) : null,
      readMessage: message.readMessage,
      conversation: message.conversation
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const message = this.createFromForm();
    if (message.id !== undefined) {
      this.subscribeToSaveResponse(this.messageService.update(message));
    } else {
      this.subscribeToSaveResponse(this.messageService.create(message));
    }
  }

  private createFromForm(): IMessage {
    return {
      ...new Message(),
      id: this.editForm.get(['id']).value,
      idUserSender: this.editForm.get(['idUserSender']).value,
      idUserRecipient: this.editForm.get(['idUserRecipient']).value,
      contentMessage: this.editForm.get(['contentMessage']).value,
      dateMessage:
        this.editForm.get(['dateMessage']).value != null ? moment(this.editForm.get(['dateMessage']).value, DATE_TIME_FORMAT) : undefined,
      readMessage: this.editForm.get(['readMessage']).value,
      conversation: this.editForm.get(['conversation']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMessage>>) {
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

  trackConversationById(index: number, item: IConversation) {
    return item.id;
  }
}
