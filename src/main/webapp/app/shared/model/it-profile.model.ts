export interface IITProfile {
  id?: string;
  job?: string;
  favLanguages?: string;
  favOS?: string;
  gamer?: boolean;
  geek?: boolean;
  otaku?: boolean;
}

export class ITProfile implements IITProfile {
  constructor(
    public id?: string,
    public job?: string,
    public favLanguages?: string,
    public favOS?: string,
    public gamer?: boolean,
    public geek?: boolean,
    public otaku?: boolean
  ) {
    this.gamer = this.gamer || false;
    this.geek = this.geek || false;
    this.otaku = this.otaku || false;
  }
}
