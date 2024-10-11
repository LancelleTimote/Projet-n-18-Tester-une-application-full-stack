import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;
  let mockSessionInfo: SessionInformation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
    mockSessionInfo = {
      token: 'testToken',
      type: 'Bearer',
      id: 1,
      username: 'testUser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in the user', () => {
    service.logIn(mockSessionInfo);
    expect(service.isLogged).toBeTruthy();
    expect(service.sessionInformation).toEqual(mockSessionInfo);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeTruthy();
    });
  });

  it('should log out the user', () => {
    service.logOut();
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalsy();
    });
  });
});
