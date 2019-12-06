import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ExtendedUserService } from 'app/entities/extended-user/extended-user.service';
import { IExtendedUser, ExtendedUser } from 'app/shared/model/extended-user.model';

describe('Service Tests', () => {
  describe('ExtendedUser Service', () => {
    let injector: TestBed;
    let service: ExtendedUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IExtendedUser;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ExtendedUserService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ExtendedUser('ID', currentDate, currentDate, 0, 0, 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            birthDate: currentDate.format(DATE_TIME_FORMAT),
            memberSince: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find('123')
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a ExtendedUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            birthDate: currentDate.format(DATE_TIME_FORMAT),
            memberSince: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            birthDate: currentDate,
            memberSince: currentDate
          },
          returnedFromService
        );
        service
          .create(new ExtendedUser(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a ExtendedUser', () => {
        const returnedFromService = Object.assign(
          {
            birthDate: currentDate.format(DATE_TIME_FORMAT),
            memberSince: currentDate.format(DATE_TIME_FORMAT),
            locationLongitude: 1,
            locationLatitude: 1,
            gender: 'BBBBBB',
            interest: 'BBBBBB',
            note: 1,
            hobbies: 'BBBBBB',
            profilePhotoURL: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            birthDate: currentDate,
            memberSince: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of ExtendedUser', () => {
        const returnedFromService = Object.assign(
          {
            birthDate: currentDate.format(DATE_TIME_FORMAT),
            memberSince: currentDate.format(DATE_TIME_FORMAT),
            locationLongitude: 1,
            locationLatitude: 1,
            gender: 'BBBBBB',
            interest: 'BBBBBB',
            note: 1,
            hobbies: 'BBBBBB',
            profilePhotoURL: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            birthDate: currentDate,
            memberSince: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ExtendedUser', () => {
        service.delete('123').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
