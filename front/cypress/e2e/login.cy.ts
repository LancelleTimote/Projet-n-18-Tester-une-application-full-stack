describe('User Login Functionality', () => {
  beforeEach(() => {
    cy.intercept('GET', '/api/auth', { fixture: 'user-list.json' });
  });

  it('Check login component structure', () => {
    cy.visit('/login');

    cy.get('mat-card').should('exist');
    cy.get('mat-card-header').should('exist');
    cy.get('.mat-card-title').contains('Login').should('exist');

    cy.get('form').should('exist');
    cy.get('mat-card-content').should('exist');
    cy.get('mat-form-field').should('have.length', 2);
    cy.get('input[formControlName=email]').should('exist');
    cy.get('input[formControlName=password]').should('exist');

    cy.get('button[mat-icon-button]')
      .contains('visibility_off')
      .should('exist');
    cy.get('button[mat-icon-button]').contains('visibility').click();
    cy.get('button[mat-icon-button]').contains('visibility').should('exist');

    cy.get('button').contains('Submit').should('exist');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Login unsuccessful with incorrect email', () => {
    cy.visit('/login');
    const user = { email: 'yoga@studio.com', password: 'test!1234' };

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { error: 'Bad credentials' },
    });

    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);
    cy.get('button').contains('Submit').click();

    cy.fixture('user-list.json').then((users) => {
      const foundUser = users.find((u) => u.email === user.email);
      expect(foundUser).to.not.exist;
    });

    cy.url().should('not.include', '/sessions');
    cy.get('p').contains('An error occurred').should('exist');
  });

  it('Login unsuccessful with correct email but incorrect password', () => {
    cy.visit('/login');
    const user = { email: 'yoga@studio.com', password: 'wrongPassword!' };

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { error: 'Bad credentials' },
    });

    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);
    cy.get('button').contains('Submit').click();

    cy.fixture('user-list.json').then((users) => {
      const foundUser = users.find((u) => u.email === user.email);
      expect(foundUser).to.exist;
      expect(foundUser.email).to.equal(user.email);
    });

    cy.url().should('not.include', '/sessions');
    cy.get('p').contains('An error occurred').should('exist');
  });

  it('Login unsuccessful with incorrect email and password', () => {
    cy.visit('/login');
    const user = { email: 'invalid@test.com', password: 'wrongPassword!' };

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { error: 'Bad credentials' },
    });

    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);
    cy.get('button').contains('Submit').click();

    cy.fixture('user-list.json').then((users) => {
      const foundUser = users.find((u) => u.email === user.email);
      expect(foundUser).to.not.exist;
    });

    cy.url().should('not.include', '/sessions');
    cy.get('p').contains('An error occurred').should('exist');
  });

  it('Login successful', () => {
    cy.visit('/login');
    const user = { email: 'yoga@studio.com', password: 'test!1234' };

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: user.email,
        firstName: 'Yoga',
        lastName: 'Studio',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [],
    }).as('session');

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
  });
});
