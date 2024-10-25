describe('User Login Functionality', () => {
  it('Login successful', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [],
    }).as('session');

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions');
  });

  it('Login fails with incorrect credentials', () => {
    cy.visit('/login');

    const incorrectUser = {
      email: 'wrongemail@studio.com',
      password: 'wrongPassword!',
    };

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        error: 'Bad credentials',
      },
    });

    cy.get('input[formControlName=email]').type(incorrectUser.email);
    cy.get('input[formControlName=password]').type(incorrectUser.password);
    cy.get('button[type="submit"]').click();

    cy.url().should('not.include', '/sessions');

    cy.get('p').contains('An error occurred').should('be.visible');
  });

  it('Login fails with correct email and incorrect password', () => {
    cy.visit('/login');

    const user = {
      email: 'validemail@studio.com',
      password: 'incorrectPassword!',
    };

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        error: 'Bad credentials',
      },
    });

    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);
    cy.get('button[type="submit"]').click();

    cy.url().should('not.include', '/sessions');

    cy.get('p').contains('An error occurred').should('be.visible');
  });
});
