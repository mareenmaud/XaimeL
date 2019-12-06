import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { XmLTestModule } from '../../../test.module';
import { ITProfileUpdateComponent } from 'app/entities/it-profile/it-profile-update.component';
import { ITProfileService } from 'app/entities/it-profile/it-profile.service';
import { ITProfile } from 'app/shared/model/it-profile.model';

describe('Component Tests', () => {
  describe('ITProfile Management Update Component', () => {
    let comp: ITProfileUpdateComponent;
    let fixture: ComponentFixture<ITProfileUpdateComponent>;
    let service: ITProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [XmLTestModule],
        declarations: [ITProfileUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ITProfileUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ITProfileUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ITProfileService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ITProfile('123');
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
        const entity = new ITProfile();
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
