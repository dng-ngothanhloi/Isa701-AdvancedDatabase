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

describe('ChiTietNhapXuat e2e test', () => {
  const chiTietNhapXuatPageUrl = '/chi-tiet-nhap-xuat';
  const chiTietNhapXuatPageUrlPattern = new RegExp('/chi-tiet-nhap-xuat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const chiTietNhapXuatSample = { soLuong: 10174 };

  let chiTietNhapXuat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/chi-tiet-nhap-xuats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/chi-tiet-nhap-xuats').as('postEntityRequest');
    cy.intercept('DELETE', '/api/chi-tiet-nhap-xuats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (chiTietNhapXuat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/chi-tiet-nhap-xuats/${chiTietNhapXuat.id}`,
      }).then(() => {
        chiTietNhapXuat = undefined;
      });
    }
  });

  it('ChiTietNhapXuats menu should load ChiTietNhapXuats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('chi-tiet-nhap-xuat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ChiTietNhapXuat').should('exist');
    cy.url().should('match', chiTietNhapXuatPageUrlPattern);
  });

  describe('ChiTietNhapXuat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(chiTietNhapXuatPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ChiTietNhapXuat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/chi-tiet-nhap-xuat/new$'));
        cy.getEntityCreateUpdateHeading('ChiTietNhapXuat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chiTietNhapXuatPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/chi-tiet-nhap-xuats',
          body: chiTietNhapXuatSample,
        }).then(({ body }) => {
          chiTietNhapXuat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/chi-tiet-nhap-xuats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/chi-tiet-nhap-xuats?page=0&size=20>; rel="last",<http://localhost/api/chi-tiet-nhap-xuats?page=0&size=20>; rel="first"',
              },
              body: [chiTietNhapXuat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(chiTietNhapXuatPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ChiTietNhapXuat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('chiTietNhapXuat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chiTietNhapXuatPageUrlPattern);
      });

      it('edit button click should load edit ChiTietNhapXuat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChiTietNhapXuat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chiTietNhapXuatPageUrlPattern);
      });

      it('edit button click should load edit ChiTietNhapXuat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ChiTietNhapXuat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chiTietNhapXuatPageUrlPattern);
      });

      it('last delete button click should delete instance of ChiTietNhapXuat', () => {
        cy.intercept('GET', '/api/chi-tiet-nhap-xuats/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('chiTietNhapXuat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', chiTietNhapXuatPageUrlPattern);

        chiTietNhapXuat = undefined;
      });
    });
  });

  describe('new ChiTietNhapXuat page', () => {
    beforeEach(() => {
      cy.visit(`${chiTietNhapXuatPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ChiTietNhapXuat');
    });

    it('should create an instance of ChiTietNhapXuat', () => {
      cy.get(`[data-cy="soLuong"]`).type('22219');
      cy.get(`[data-cy="soLuong"]`).should('have.value', '22219');

      cy.get(`[data-cy="donGia"]`).type('7851.75');
      cy.get(`[data-cy="donGia"]`).should('have.value', '7851.75');

      cy.get(`[data-cy="createdAt"]`).type('2025-04-27T17:11');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2025-04-27T17:11');

      cy.get(`[data-cy="createdBy"]`).type('hence');
      cy.get(`[data-cy="createdBy"]`).should('have.value', 'hence');

      cy.get(`[data-cy="updatedAt"]`).type('2025-04-27T11:21');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2025-04-27T11:21');

      cy.get(`[data-cy="updatedBy"]`).type('in kissingly');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', 'in kissingly');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        chiTietNhapXuat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', chiTietNhapXuatPageUrlPattern);
    });
  });
});
