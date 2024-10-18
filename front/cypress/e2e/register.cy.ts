describe('Register Page Spec', () => {
  beforeEach(() => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: {
        id: 1,
        username: 'newUser',
        email: 'newuser@example.com',
      },
    }).as('registerUser');

    cy.visit('/register');
  });

  it('Successful registration', () => {
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('newuser@example.com');
    cy.get('input[formControlName=password]').type('password123');

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

    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('newuser@example.com');
    cy.get('input[formControlName=password]').type('password123');

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
    cy.get('input[formControlName=firstName]').type('John');
    cy.get('input[formControlName=lastName]').type('Doe');
    cy.get('input[formControlName=email]').type('newuser@example.com');
    cy.get('input[formControlName=password]').type('password123');

    const submitButton = cy.get('button[type="submit"]');
    submitButton.should('not.be.disabled');
  });

  //   it('Should display error message if email is invalid', () => {
  //     cy.get('input[formControlName=firstName]').type('John');
  //     cy.get('input[formControlName=lastName]').type('Doe');
  //     cy.get('input[formControlName=email]').type('invalidemail');
  //     cy.get('input[formControlName=password]').type('password123');
  //     cy.get('button[type="submit"]').click();

  //     cy.get('.error')
  //       .should('be.visible')
  //       .and('contain', 'Please enter a valid email address');
  //   });
});
