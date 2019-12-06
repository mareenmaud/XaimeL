import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { XmLTestModule } from '../../../test.module';
import { ITProfileDetailComponent } from 'app/entities/it-profile/it-profile-detail.component';
import { ITProfile } from 'app/shared/model/it-profile.model';

describe('Component Tests', () => {
  describe('ITProfile Management Detail Component', () => {
    let comp: ITProfileDetailComponent;
    let fixture: ComponentFixture<ITProfileDetailComponent>;
    const route = ({ data: of({ iTProfile: new ITProfile('123') }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [XmLTestModule],
        declarations: [ITProfileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ITProfileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ITProfileDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.iTProfile).toEqual(jasmine.objectContaining({ id: '123' }));
      });
    });
  });
});
