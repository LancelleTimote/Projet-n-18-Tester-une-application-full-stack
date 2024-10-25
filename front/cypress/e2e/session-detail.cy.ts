describe('Session Detail and Update Functionality', () => {
  const user = {
    id: 1,
    email: 'yoga@studio.com',
    password: 'test!1234',
    admin: false,
  };

  beforeEach(() => {
    cy.intercept('GET', '/api/session', { fixture: 'sessions-list.json' });
    cy.intercept('GET', '/api/session/1', { fixture: 'session-empty.json' });
    cy.intercept('GET', '/api/teacher/1', { fixture: 'teacher-single.json' });

    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: user.id,
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

    cy.get('button').contains('Detail').first().click();
  });

  it('Should display the session detail page correctly', () => {
    cy.url().should('include', '/sessions/detail');

    cy.get('mat-card').should('exist');

    cy.get('mat-card-title').should('exist');
    cy.get('h1').should('exist');

    cy.get('button').contains('arrow_back').should('exist');
    cy.get('button').contains('arrow_back').click();
    cy.url().should('include', '/sessions');

    cy.get('button').contains('Detail').first().click();

    cy.get('mat-icon').contains('person_add').should('exist');
    cy.get('button').should('have.class', 'mat-primary');
    cy.get('button').contains('Participate').should('exist');

    cy.get('mat-card-subtitle').should('exist');
    cy.get('mat-icon').contains('people').should('exist');

    cy.get('img')
      .should('be.visible')
      .should('have.attr', 'src', 'assets/sessions.png');

    cy.get('mat-card-content').should('exist');
    cy.get('mat-icon').contains('group').should('exist');
    cy.get('mat-icon').contains('calendar_month').should('exist');

    cy.get('p').contains('Description:').should('exist');
    cy.get('br').should('exist');

    cy.get('i').contains('Create at:').should('exist');
    cy.get('i').contains('Last update:').should('exist');
  });

  it('Should verify session details information', () => {
    cy.fixture('session-empty.json').then((session) => {
      cy.url().should('include', `/sessions/detail/${session.id}`);

      cy.get('h1').contains(`${session.name}`).should('exist');
      cy.get('span')
        .contains(`${session.users.length} attendees`)
        .should('exist');

      const formattedDate = new Date(session.date).toLocaleDateString('en-US', {
        month: 'long',
        day: 'numeric',
        year: 'numeric',
      });
      cy.get('span').contains(`${formattedDate}`).should('exist');

      cy.get('div').contains(`${session.description}`).should('exist');

      const formattedCreatedDate = new Date(
        session.createdAt
      ).toLocaleDateString('en-US', {
        month: 'long',
        day: 'numeric',
        year: 'numeric',
      });
      const formattedUpdatedDate = new Date(
        session.updatedAt
      ).toLocaleDateString('en-US', {
        month: 'long',
        day: 'numeric',
        year: 'numeric',
      });

      cy.get('div').contains(`${formattedCreatedDate}`).should('exist');
      cy.get('div').contains(`${formattedUpdatedDate}`).should('exist');
    });

    cy.fixture('teacher-single.json').then((teacher) => {
      cy.get('span')
        .contains(`${teacher.firstName} ${teacher.lastName.toUpperCase()}`)
        .should('exist');
    });
  });

  it('Should allow participation and cancel participation for a session', () => {
    cy.fixture('session-empty.json').then((session) => {
      cy.intercept(
        'POST',
        `/api/session/${session.id}/participate/${user.id}`,
        { statusCode: 200, body: {} }
      );
      cy.intercept('GET', `/api/session/${session.id}`, {
        fixture: 'session-with-participant.json',
      });
      cy.intercept('GET', `/api/teacher/${session.teacher_id}`, {
        fixture: 'teacher-single.json',
      });

      cy.get('button').contains('Participate').click();

      cy.get('mat-icon').contains('person_remove').should('exist');
      cy.get('span').contains('Do not participate');
      cy.get('button').should('have.class', 'mat-warn');

      cy.intercept(
        'DELETE',
        `/api/session/${session.id}/participate/${user.id}`,
        { statusCode: 200, body: {} }
      );
      cy.intercept('GET', `/api/session/${session.id}`, {
        fixture: 'session-empty.json',
      });
      cy.intercept('GET', `/api/teacher/${session.teacher_id}`, {
        fixture: 'teacher-single.json',
      });

      cy.get('button').contains('Do not participate').click();

      cy.get('mat-icon').contains('person_add').should('exist');
      cy.get('span').contains('Participate');
      cy.get('button').should('have.class', 'mat-primary');
    });
  });
});
