import { Moment } from 'moment';
import { IITProfile } from 'app/shared/model/it-profile.model';
import { IPsychoProfile } from 'app/shared/model/psycho-profile.model';
import { IUser } from 'app/core/user/user.model';
import { IExtendedUser } from 'app/shared/model/extended-user.model';

export interface IExtendedUser {
  id?: string;
  birthDate?: Moment;
  memberSince?: Moment;
  locationLongitude?: number;
  locationLatitude?: number;
  gender?: string;
  interest?: string;
  note?: number;
  hobbies?: string;
  profilePhotoURL?: string;
  iTProfile?: IITProfile;
  psychoProfile?: IPsychoProfile;
  user?: IUser;
  extendedUser?: IExtendedUser;
  matches?: IExtendedUser[];
}

export class ExtendedUser implements IExtendedUser {
  constructor(
    public id?: string,
    public birthDate?: Moment,
    public memberSince?: Moment,
    public locationLongitude?: number,
    public locationLatitude?: number,
    public gender?: string,
    public interest?: string,
    public note?: number,
    public hobbies?: string,
    public profilePhotoURL?: string,
    public iTProfile?: IITProfile,
    public psychoProfile?: IPsychoProfile,
    public user?: IUser,
    public extendedUser?: IExtendedUser,
    public matches?: IExtendedUser[]
  ) {}
}
