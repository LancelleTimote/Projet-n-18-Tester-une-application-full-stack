describe('User Registration Functionality', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: {
        id: 1,
        username: 'toto',
        email: 'toto3@toto.com',
      },
    }).as('registerUser');

    cy.visit('/register');
  });

  it('Successful registration', () => {
    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    cy.get('input[formControlName=email]').type('toto3@toto.com');
    cy.get('input[formControlName=password]').type('test!1234');

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

    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    cy.get('input[formControlName=email]').type('toto3@toto.com');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type="submit"]').click();
    cy.get('.error').should('be.visible').and('contain', 'An error occurred');
  });

  it('Should disable the submit button if the form is invalid', () => {
    cy.get('input[formControlName=firstName]').clear();
    cy.get('input[formControlName=lastName]').clear();
    cy.get('input[formControlName=email]').clear();
    cy.get('input[formControlName=password]').clear();

    const submitButton = cy.get('button[type="submit"]');
    submitButton.should('be.disabled');
  });

  it('Should enable the submit button if the form is valid', () => {
    cy.get('input[formControlName=firstName]').type('toto');
    cy.get('input[formControlName=lastName]').type('toto');
    cy.get('input[formControlName=email]').type('toto3@toto.com');
    cy.get('input[formControlName=password]').type('test!1234');

    const submitButton = cy.get('button[type="submit"]');
    submitButton.should('not.be.disabled');
  });
});