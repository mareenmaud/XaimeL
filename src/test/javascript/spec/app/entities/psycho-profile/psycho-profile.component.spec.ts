import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { XmLTestModule } from '../../../test.module';
import { PsychoProfileComponent } from 'app/entities/psycho-profile/psycho-profile.component';
import { PsychoProfileService } from 'app/entities/psycho-profile/psycho-profile.service';
import { PsychoProfile } from 'app/shared/model/psycho-profile.model';

describe('Component Tests', () => {
  describe('PsychoProfile Management Component', () => {
    let comp: PsychoProfileComponent;
    let fixture: ComponentFixture<PsychoProfileComponent>;
    let service: PsychoProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [XmLTestModule],
        declarations: [PsychoProfileComponent],
        providers: []
      })
        .overrideTemplate(PsychoProfileComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PsychoProfileComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PsychoProfileService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new PsychoProfile('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.psychoProfiles[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
