import { IMessage } from 'app/shared/model/message.model';

export interface IConversation {
  id?: string;
  idUsers?: number;
  messages?: IMessage[];
}

export class Conversation implements IConversation {
  constructor(public id?: string, public idUsers?: number, public messages?: IMessage[]) {}
}
