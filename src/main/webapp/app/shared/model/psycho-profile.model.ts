export interface IPsychoProfile {
  id?: string;
  summaryProfile?: string;
  jungValues?: number;
}

export class PsychoProfile implements IPsychoProfile {
  constructor(public id?: string, public summaryProfile?: string, public jungValues?: number) {}
}
