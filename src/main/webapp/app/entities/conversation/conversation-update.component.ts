import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IConversation, Conversation } from 'app/shared/model/conversation.model';
import { ConversationService } from './conversation.service';

@Component({
  selector: 'jhi-conversation-update',
  templateUrl: './conversation-update.component.html'
})
export class ConversationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    idUser1: [],
    idUser2: []
  });

  constructor(protected conversationService: ConversationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ conversation }) => {
      this.updateForm(conversation);
    });
  }

  updateForm(conversation: IConversation) {
    this.editForm.patchValue({
      id: conversation.id,
      idUser1: conversation.idUser1,
      idUser2: conversation.idUser2
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const conversation = this.createFromForm();
    if (conversation.id !== undefined) {
      this.subscribeToSaveResponse(this.conversationService.update(conversation));
    } else {
      this.subscribeToSaveResponse(this.conversationService.create(conversation));
    }
  }

  private createFromForm(): IConversation {
    return {
      ...new Conversation(),
      id: this.editForm.get(['id']).value,
      idUser1: this.editForm.get(['idUser1']).value,
      idUser2: this.editForm.get(['idUser2']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversation>>) {
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
