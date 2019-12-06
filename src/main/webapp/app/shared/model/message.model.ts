import { Moment } from 'moment';
import { IConversation } from 'app/shared/model/conversation.model';

export interface IMessage {
  id?: string;
  idUserSender?: number;
  idUserRecipient?: number;
  contentMessage?: string;
  dateMessage?: Moment;
  readMessage?: boolean;
  conversation?: IConversation;
}

export class Message implements IMessage {
  constructor(
    public id?: string,
    public idUserSender?: number,
    public idUserRecipient?: number,
    public contentMessage?: string,
    public dateMessage?: Moment,
    public readMessage?: boolean,
    public conversation?: IConversation
  ) {
    this.readMessage = this.readMessage || false;
  }
}
