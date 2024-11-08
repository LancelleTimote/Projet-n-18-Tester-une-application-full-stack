describe('Session Update Page', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/teacher', { fixture: 'teacher-list.json' });
    cy.intercept('GET', '/api/session', { fixture: 'sessions-list.json' });
    cy.intercept('GET', '/api/session/1', { fixture: 'session-empty.json' });

    cy.visit('/login');

    const user = {
      email: 'yoga@studio.com',
      password: 'test!1234',
      admin: true,
    };

    cy.intercept('POST', '/api/auth/login', {
      body: {
        username: user.email,
        password: user.password,
        admin: user.admin,
      },
    });

    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(
      `${user.password}{enter}{enter}`
    );

    cy.fixture('user-list.json').then((users) => {
      const foundUser = users.find((u) => u.email === user.email);
      expect(foundUser).to.exist;
      expect(foundUser.email).to.equal(user.email);
    });

    cy.url().should('include', '/sessions');

    cy.get('span').contains('Edit').click();
  });

  it('Verifies elements on update session page', () => {
    cy.url().should('include', '/sessions/update/1');

    cy.get('mat-card').should('exist');

    cy.get('mat-card-title').should('exist');
    cy.get('h1').contains('Update session').should('exist');

    cy.get('button').contains('arrow_back').should('exist');
    cy.get('button').contains('arrow_back').click();
    cy.url().should('include', '/sessions');

    cy.get('span').contains('Edit').click();

    cy.get('form').should('exist');
    cy.get('mat-form-field').should('have.length', 4);

    cy.get('input[formControlName=name]').should('exist');
    cy.get('input[formControlName=date]').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').should('exist');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').should('have.length', 2);
    cy.get('mat-option').contains('Margot DELAHAYE').should('exist');
    cy.get('mat-option').contains('Hélène THIERCELIN').should('exist');
    cy.get('textarea[formControlName=description]').should('exist');

    cy.get('span').contains('Save').should('exist');
    cy.get('button[type="submit"]').should('not.be.disabled');

    cy.get('body').click();
  });

  it('Displays pre-filled session information', () => {
    cy.get('input[formControlName=name]')
      .should('have.value', 'Session 1')
      .should('exist');
    cy.get('input[formControlName=date]')
      .should('have.value', '2024-05-01')
      .should('exist');
    cy.get('mat-select[formControlName="teacher_id"]')
      .should('contain', 'Margot DELAHAYE')
      .should('exist');
    cy.get('textarea[formControlName=description]')
      .should('have.value', 'My description')
      .should('exist');
  });

  it('Saves updated session information', () => {
    const session = {
      id: 3,
      name: 'Yoga',
      date: '2024-05-09',
      teacher_id: 2,
      description: 'Yoga session',
      users: [],
      createdAt: '2024-05-09',
      updatedAt: '2024-05-09',
    };

    cy.get('input[formControlName=date]').type(session.date);
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Hélène THIERCELIN').click();

    cy.get('button[type="submit"]').should('not.be.disabled');

    cy.intercept('PUT', '/api/session/1', {
      body: session,
    });

    cy.get('span').contains('Save').click();

    cy.url().should('include', '/sessions');

    cy.get('.mat-simple-snackbar').should('exist');
    cy.get('span').contains('Session updated !').should('exist');
    cy.get('button').contains('Close').should('exist');
  });
});
