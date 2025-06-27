import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('KhachHang e2e test', () => {
  const khachHangPageUrl = '/khach-hang';
  const khachHangPageUrlPattern = new RegExp('/khach-hang(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const khachHangSample = { tenKH: 'unsteady straw' };

  let khachHang;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/khach-hangs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/khach-hangs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/khach-hangs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (khachHang) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/khach-hangs/${khachHang.id}`,
      }).then(() => {
        khachHang = undefined;
      });
    }
  });

  it('KhachHangs menu should load KhachHangs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('khach-hang');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('KhachHang').should('exist');
    cy.url().should('match', khachHangPageUrlPattern);
  });

  describe('KhachHang page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(khachHangPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create KhachHang page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/khach-hang/new$'));
        cy.getEntityCreateUpdateHeading('KhachHang');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', khachHangPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/khach-hangs',
          body: khachHangSample,
        }).then(({ body }) => {
          khachHang = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/khach-hangs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/khach-hangs?page=0&size=20>; rel="last",<http://localhost/api/khach-hangs?page=0&size=20>; rel="first"',
              },
              body: [khachHang],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(khachHangPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details KhachHang page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('khachHang');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', khachHangPageUrlPattern);
      });

      it('edit button click should load edit KhachHang page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('KhachHang');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', khachHangPageUrlPattern);
      });

      it('edit button click should load edit KhachHang page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('KhachHang');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', khachHangPageUrlPattern);
      });

      it('last delete button click should delete instance of KhachHang', () => {
        cy.intercept('GET', '/api/khach-hangs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('khachHang').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', khachHangPageUrlPattern);

        khachHang = undefined;
      });
    });
  });

  describe('new KhachHang page', () => {
    beforeEach(() => {
      cy.visit(`${khachHangPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('KhachHang');
    });

    it('should create an instance of KhachHang', () => {
      cy.get(`[data-cy="maKH"]`).type('aboard qui');
      cy.get(`[data-cy="maKH"]`).should('have.value', 'aboard qui');

      cy.get(`[data-cy="tenKH"]`).type('oof bonfire');
      cy.get(`[data-cy="tenKH"]`).should('have.value', 'oof bonfire');

      cy.get(`[data-cy="goiTinh"]`).select('KhongXacDinh');

      cy.get(`[data-cy="dateOfBirth"]`).type('2025-04-27');
      cy.get(`[data-cy="dateOfBirth"]`).blur();
      cy.get(`[data-cy="dateOfBirth"]`).should('have.value', '2025-04-27');

      cy.get(`[data-cy="diaChi"]`).type('boohoo');
      cy.get(`[data-cy="diaChi"]`).should('have.value', 'boohoo');

      cy.get(`[data-cy="createdAt"]`).type('2025-04-27T15:12');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2025-04-27T15:12');

      cy.get(`[data-cy="createdBy"]`).type('fooey');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'fooey');

      cy.get(`[data-cy="updatedAt"]`).type('2025-04-27T04:17');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2025-04-27T04:17');

      cy.get(`[data-cy="updatedBy"]`).type('who natural nautical');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', 'who natural nautical');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        khachHang = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', khachHangPageUrlPattern);
    });
  });
});
