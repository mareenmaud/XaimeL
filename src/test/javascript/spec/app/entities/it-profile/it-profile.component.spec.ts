import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { XmLTestModule } from '../../../test.module';
import { ITProfileComponent } from 'app/entities/it-profile/it-profile.component';
import { ITProfileService } from 'app/entities/it-profile/it-profile.service';
import { ITProfile } from 'app/shared/model/it-profile.model';

describe('Component Tests', () => {
  describe('ITProfile Management Component', () => {
    let comp: ITProfileComponent;
    let fixture: ComponentFixture<ITProfileComponent>;
    let service: ITProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [XmLTestModule],
        declarations: [ITProfileComponent],
        providers: []
      })
        .overrideTemplate(ITProfileComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ITProfileComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ITProfileService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ITProfile('123')],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.iTProfiles[0]).toEqual(jasmine.objectContaining({ id: '123' }));
    });
  });
});
