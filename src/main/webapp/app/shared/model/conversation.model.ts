import { IMessage } from 'app/shared/model/message.model';

export interface IConversation {
  id?: string;
  idUser1?: string;
  idUser2?: string;
  messages?: IMessage[];
}

export class Conversation implements IConversation {
  constructor(public id?: string, public idUser1?: string, public idUser2?: string, public messages?: IMessage[]) {}
}
