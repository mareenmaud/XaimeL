export interface IPsychoProfile {
  id?: string;
  summaryProfile?: string;
  jungValue1?: number;
  jungValue2?: number;
  jungValue3?: number;
  jungValue4?: number;
}

export class PsychoProfile implements IPsychoProfile {
  constructor(
    public id?: string,
    public summaryProfile?: string,
    public jungValue1?: number,
    public jungValue2?: number,
    public jungValue3?: number,
    public jungValue4?: number
  ) {}
}
