import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XmLTestModule } from '../../../test.module';
import { PsychoProfileDetailComponent } from 'app/entities/psycho-profile/psycho-profile-detail.component';
import { PsychoProfile } from 'app/shared/model/psycho-profile.model';

describe('Component Tests', () => {
  describe('PsychoProfile Management Detail Component', () => {
    let comp: PsychoProfileDetailComponent;
    let fixture: ComponentFixture<PsychoProfileDetailComponent>;
    const route = ({ data: of({ psychoProfile: new PsychoProfile('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [XmLTestModule],
        declarations: [PsychoProfileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PsychoProfileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PsychoProfileDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.psychoProfile).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
