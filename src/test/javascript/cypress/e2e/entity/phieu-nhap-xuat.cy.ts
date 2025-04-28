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

describe('PhieuNhapXuat e2e test', () => {
    const phieuNhapXuatPageUrl = '/phieu-nhap-xuat';
    const phieuNhapXuatPageUrlPattern = new RegExp('/phieu-nhap-xuat(\\?.*)?$');
    const username = Cypress.env('E2E_USERNAME') ?? 'user';
    const password = Cypress.env('E2E_PASSWORD') ?? 'user';
    const phieuNhapXuatSample = {};

    let phieuNhapXuat;

    beforeEach(() => {
        cy.login(username, password);
    });

    beforeEach(() => {
        cy.intercept('GET', '/api/phieu-nhap-xuats+(?*|)').as('entitiesRequest');
        cy.intercept('POST', '/api/phieu-nhap-xuats').as('postEntityRequest');
        cy.intercept('DELETE', '/api/phieu-nhap-xuats/*').as('deleteEntityRequest');
    });

    afterEach(() => {
        if (phieuNhapXuat) {
            cy.authenticatedRequest({
                method: 'DELETE',
                url: `/api/phieu-nhap-xuats/${phieuNhapXuat.id}`,
            }).then(() => {
                phieuNhapXuat = undefined;
            });
        }
    });

    it('PhieuNhapXuats menu should load PhieuNhapXuats page', () => {
        cy.visit('/');
        cy.clickOnEntityMenuItem('phieu-nhap-xuat');
        cy.wait('@entitiesRequest').then(({response}) => {
            if (response?.body.length === 0) {
                cy.get(entityTableSelector).should('not.exist');
            } else {
                cy.get(entityTableSelector).should('exist');
            }
        });
        cy.getEntityHeading('PhieuNhapXuat').should('exist');
        cy.url().should('match', phieuNhapXuatPageUrlPattern);
    });

    describe('PhieuNhapXuat page', () => {
        describe('create button click', () => {
            beforeEach(() => {
                cy.visit(phieuNhapXuatPageUrl);
                cy.wait('@entitiesRequest');
            });

            it('should load create PhieuNhapXuat page', () => {
                cy.get(entityCreateButtonSelector).click();
                cy.url().should('match', new RegExp('/phieu-nhap-xuat/new$'));
                cy.getEntityCreateUpdateHeading('PhieuNhapXuat');
                cy.get(entityCreateSaveButtonSelector).should('exist');
                cy.get(entityCreateCancelButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', phieuNhapXuatPageUrlPattern);
            });
        });

        describe('with existing value', () => {
            beforeEach(() => {
                cy.authenticatedRequest({
                    method: 'POST',
                    url: '/api/phieu-nhap-xuats',
                    body: phieuNhapXuatSample,
                }).then(({body}) => {
                    phieuNhapXuat = body;

                    cy.intercept(
                        {
                            method: 'GET',
                            url: '/api/phieu-nhap-xuats+(?*|)',
                            times: 1,
                        },
                        {
                            statusCode: 200,
                            headers: {
                                link: '<http://localhost/api/phieu-nhap-xuats?page=0&size=20>; rel="last",<http://localhost/api/phieu-nhap-xuats?page=0&size=20>; rel="first"',
                            },
                            body: [phieuNhapXuat],
                        },
                    ).as('entitiesRequestInternal');
                });

                cy.visit(phieuNhapXuatPageUrl);

                cy.wait('@entitiesRequestInternal');
            });

            it('detail button click should load details PhieuNhapXuat page', () => {
                cy.get(entityDetailsButtonSelector).first().click();
                cy.getEntityDetailsHeading('phieuNhapXuat');
                cy.get(entityDetailsBackButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', phieuNhapXuatPageUrlPattern);
            });

            it('edit button click should load edit PhieuNhapXuat page and go back', () => {
                cy.get(entityEditButtonSelector).first().click();
                cy.getEntityCreateUpdateHeading('PhieuNhapXuat');
                cy.get(entityCreateSaveButtonSelector).should('exist');
                cy.get(entityCreateCancelButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', phieuNhapXuatPageUrlPattern);
            });

            it('edit button click should load edit PhieuNhapXuat page and save', () => {
                cy.get(entityEditButtonSelector).first().click();
                cy.getEntityCreateUpdateHeading('PhieuNhapXuat');
                cy.get(entityCreateSaveButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', phieuNhapXuatPageUrlPattern);
            });

            it('last delete button click should delete instance of PhieuNhapXuat', () => {
                cy.intercept('GET', '/api/phieu-nhap-xuats/*').as('dialogDeleteRequest');
                cy.get(entityDeleteButtonSelector).last().click();
                cy.wait('@dialogDeleteRequest');
                cy.getEntityDeleteDialogHeading('phieuNhapXuat').should('exist');
                cy.get(entityConfirmDeleteButtonSelector).click();
                cy.wait('@deleteEntityRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(204);
                });
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', phieuNhapXuatPageUrlPattern);

                phieuNhapXuat = undefined;
            });
        });
    });

    describe('new PhieuNhapXuat page', () => {
        beforeEach(() => {
            cy.visit(`${phieuNhapXuatPageUrl}`);
            cy.get(entityCreateButtonSelector).click();
            cy.getEntityCreateUpdateHeading('PhieuNhapXuat');
        });

        it('should create an instance of PhieuNhapXuat', () => {
            cy.get(`[data-cy="maPhieu"]`).type('uncomforta');
            cy.get(`[data-cy="maPhieu"]`).should('have.value', 'uncomforta');

            cy.get(`[data-cy="ngayLapPhieu"]`).type('2025-04-27');
            cy.get(`[data-cy="ngayLapPhieu"]`).blur();
            cy.get(`[data-cy="ngayLapPhieu"]`).should('have.value', '2025-04-27');

            cy.get(`[data-cy="loaiPhieu"]`).select('Nhap');

            cy.get(`[data-cy="createdAt"]`).type('2025-04-27T05:08');
            cy.get(`[data-cy="createdAt"]`).blur();
            cy.get(`[data-cy="createdAt"]`).should('have.value', '2025-04-27T05:08');

            cy.get(`[data-cy="createdBy"]`).type('bootleg curiously');
            cy.get(`[data-cy="createdBy"]`).should('have.value', 'bootleg curiously');

            cy.get(`[data-cy="updatedAt"]`).type('2025-04-27T13:25');
            cy.get(`[data-cy="updatedAt"]`).blur();
            cy.get(`[data-cy="updatedAt"]`).should('have.value', '2025-04-27T13:25');

            cy.get(`[data-cy="updatedBy"]`).type('indeed');
            cy.get(`[data-cy="updatedBy"]`).should('have.value', 'indeed');

            cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
            cy.get(`[data-cy="isDeleted"]`).click();
            cy.get(`[data-cy="isDeleted"]`).should('be.checked');

            cy.get(entityCreateSaveButtonSelector).click();

            cy.wait('@postEntityRequest').then(({response}) => {
                expect(response?.statusCode).to.equal(201);
                phieuNhapXuat = response.body;
            });
            cy.wait('@entitiesRequest').then(({response}) => {
                expect(response?.statusCode).to.equal(200);
            });
            cy.url().should('match', phieuNhapXuatPageUrlPattern);
        });
    });
});
