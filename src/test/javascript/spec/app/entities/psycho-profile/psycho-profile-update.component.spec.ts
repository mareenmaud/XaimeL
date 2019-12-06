import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { XmLTestModule } from '../../../test.module';
import { PsychoProfileUpdateComponent } from 'app/entities/psycho-profile/psycho-profile-update.component';
import { PsychoProfileService } from 'app/entities/psycho-profile/psycho-profile.service';
import { PsychoProfile } from 'app/shared/model/psycho-profile.model';

describe('Component Tests', () => {
  describe('PsychoProfile Management Update Component', () => {
    let comp: PsychoProfileUpdateComponent;
    let fixture: ComponentFixture<PsychoProfileUpdateComponent>;
    let service: PsychoProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [XmLTestModule],
        declarations: [PsychoProfileUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PsychoProfileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PsychoProfileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PsychoProfileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PsychoProfile('123');
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new PsychoProfile();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
