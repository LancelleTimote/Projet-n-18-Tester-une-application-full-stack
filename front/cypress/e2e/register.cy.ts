describe('User Registration Functionality', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('Check register component structure', () => {
    cy.get('mat-card').should('exist');
    cy.get('mat-card-header').should('exist');
    cy.get('.mat-card-title').contains('Register').should('exist');

    cy.get('form').should('exist');
    cy.get('mat-card-content').should('exist');
    cy.get('mat-form-field').should('have.length', 4);

    cy.get('input[formControlName=firstName]').should('exist');
    cy.get('input[formControlName=lastName]').should('exist');
    cy.get('input[formControlName=email]').should('exist');
    cy.get('input[formControlName=password]').should('exist');

    cy.get('button').contains('Submit').should('exist');
    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Successful registration', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: {
        id: 1,
        username: 'Yoga',
        email: 'yoga@studio.com',
      },
    }).as('registerUser');

    const user = {
      firstName: 'Yoga',
      lastName: 'Studio',
      email: 'yoga@studio.com',
      password: 'test!1234',
    };

    cy.get('input[formControlName=firstName]').type(user.firstName);
    cy.get('input[formControlName=lastName]').type(user.lastName);
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);

    cy.get('button[type="submit"]').click();
    cy.url().should('include', '/login');
  });

  it('Displays error message on registration failure', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        error: 'Registration failed',
      },
    }).as('registerUserWithError');

    const user = {
      firstName: 'Yoga',
      lastName: 'Studio',
      email: 'yoga@studio.com',
      password: 'test!1234',
    };

    cy.get('input[formControlName=firstName]').type(user.firstName);
    cy.get('input[formControlName=lastName]').type(user.lastName);
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);

    cy.get('button[type="submit"]').click();
    cy.get('.error').should('be.visible').and('contain', 'An error occurred');
  });

  it('Should disable the submit button if the form is invalid', () => {
    cy.get('input[formControlName=firstName]').clear();
    cy.get('input[formControlName=lastName]').clear();
    cy.get('input[formControlName=email]').clear();
    cy.get('input[formControlName=password]').clear();

    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Should enable the submit button if the form is valid', () => {
    cy.get('input[formControlName=firstName]').type('Yoga');
    cy.get('input[formControlName=lastName]').type('Studio');
    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type="submit"]').should('not.be.disabled');
  });

  it('Registration not successful because email has already been used', () => {
    const user = {
      firstName: 'Yoga',
      lastName: 'Studio',
      email: 'yoga@studio.com',
      password: 'test!1234',
    };

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 409,
      body: {
        error: 'An error occurred',
      },
    }).as('register');

    cy.get('input[formControlName=firstName]').type(user.firstName);
    cy.get('input[formControlName=lastName]').type(user.lastName);
    cy.get('input[formControlName=email]').type(user.email);
    cy.get('input[formControlName=password]').type(user.password);

    cy.get('button[type="submit"]').click();
    cy.get('span').contains('An error occurred').should('exist');
    cy.url().should('not.include', '/login');
  });
});
