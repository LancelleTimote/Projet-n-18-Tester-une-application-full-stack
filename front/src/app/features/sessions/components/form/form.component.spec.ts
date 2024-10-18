import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let matSnackBar: { open: jest.Mock };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockSessionApiService = {
    create: jest.fn(),
    update: jest.fn(),
    detail: jest.fn(),
  };

  beforeEach(async () => {
    matSnackBar = { open: jest.fn() };
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: matSnackBar },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form correctly', () => {
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm!.controls['name']).toBeDefined();
    expect(component.sessionForm!.controls['date']).toBeDefined();
    expect(component.sessionForm!.controls['teacher_id']).toBeDefined();
    expect(component.sessionForm!.controls['description']).toBeDefined();
  });

  it('should set form controls to invalid when empty', () => {
    component.sessionForm?.controls['name'].setValue('');
    component.sessionForm?.controls['date'].setValue('');
    component.sessionForm?.controls['teacher_id'].setValue('');
    component.sessionForm?.controls['description'].setValue('');

    expect(component.sessionForm?.valid).toBe(false);
  });

  it('should call create when form is submitted and onUpdate is false', () => {
    const sessionData = {
      name: 'Yoga Class',
      date: '2024-10-12',
      teacher_id: 1,
      description: 'A relaxing yoga session',
    };
    component.sessionForm!.setValue(sessionData);

    mockSessionApiService.create.mockReturnValue(of(sessionData));

    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalledWith(sessionData);
    expect(matSnackBar.open).toHaveBeenCalledWith(
      'Session created !',
      'Close',
      { duration: 3000 }
    );
  });
});
