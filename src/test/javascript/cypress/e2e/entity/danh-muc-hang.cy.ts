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

describe('DanhMucHang e2e test', () => {
    const danhMucHangPageUrl = '/danh-muc-hang';
    const danhMucHangPageUrlPattern = new RegExp('/danh-muc-hang(\\?.*)?$');
    const username = Cypress.env('E2E_USERNAME') ?? 'user';
    const password = Cypress.env('E2E_PASSWORD') ?? 'user';
    const danhMucHangSample = {tenHang: 'clear dense', noiSanXuat: 'quickly putrid'};

    let danhMucHang;

    beforeEach(() => {
        cy.login(username, password);
    });

    beforeEach(() => {
        cy.intercept('GET', '/api/danh-muc-hangs+(?*|)').as('entitiesRequest');
        cy.intercept('POST', '/api/danh-muc-hangs').as('postEntityRequest');
        cy.intercept('DELETE', '/api/danh-muc-hangs/*').as('deleteEntityRequest');
    });

    afterEach(() => {
        if (danhMucHang) {
            cy.authenticatedRequest({
                method: 'DELETE',
                url: `/api/danh-muc-hangs/${danhMucHang.id}`,
            }).then(() => {
                danhMucHang = undefined;
            });
        }
    });

    it('DanhMucHangs menu should load DanhMucHangs page', () => {
        cy.visit('/');
        cy.clickOnEntityMenuItem('danh-muc-hang');
        cy.wait('@entitiesRequest').then(({response}) => {
            if (response?.body.length === 0) {
                cy.get(entityTableSelector).should('not.exist');
            } else {
                cy.get(entityTableSelector).should('exist');
            }
        });
        cy.getEntityHeading('DanhMucHang').should('exist');
        cy.url().should('match', danhMucHangPageUrlPattern);
    });

    describe('DanhMucHang page', () => {
        describe('create button click', () => {
            beforeEach(() => {
                cy.visit(danhMucHangPageUrl);
                cy.wait('@entitiesRequest');
            });

            it('should load create DanhMucHang page', () => {
                cy.get(entityCreateButtonSelector).click();
                cy.url().should('match', new RegExp('/danh-muc-hang/new$'));
                cy.getEntityCreateUpdateHeading('DanhMucHang');
                cy.get(entityCreateSaveButtonSelector).should('exist');
                cy.get(entityCreateCancelButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', danhMucHangPageUrlPattern);
            });
        });

        describe('with existing value', () => {
            beforeEach(() => {
                cy.authenticatedRequest({
                    method: 'POST',
                    url: '/api/danh-muc-hangs',
                    body: danhMucHangSample,
                }).then(({body}) => {
                    danhMucHang = body;

                    cy.intercept(
                        {
                            method: 'GET',
                            url: '/api/danh-muc-hangs+(?*|)',
                            times: 1,
                        },
                        {
                            statusCode: 200,
                            headers: {
                                link: '<http://localhost/api/danh-muc-hangs?page=0&size=20>; rel="last",<http://localhost/api/danh-muc-hangs?page=0&size=20>; rel="first"',
                            },
                            body: [danhMucHang],
                        },
                    ).as('entitiesRequestInternal');
                });

                cy.visit(danhMucHangPageUrl);

                cy.wait('@entitiesRequestInternal');
            });

            it('detail button click should load details DanhMucHang page', () => {
                cy.get(entityDetailsButtonSelector).first().click();
                cy.getEntityDetailsHeading('danhMucHang');
                cy.get(entityDetailsBackButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', danhMucHangPageUrlPattern);
            });

            it('edit button click should load edit DanhMucHang page and go back', () => {
                cy.get(entityEditButtonSelector).first().click();
                cy.getEntityCreateUpdateHeading('DanhMucHang');
                cy.get(entityCreateSaveButtonSelector).should('exist');
                cy.get(entityCreateCancelButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', danhMucHangPageUrlPattern);
            });

            it('edit button click should load edit DanhMucHang page and save', () => {
                cy.get(entityEditButtonSelector).first().click();
                cy.getEntityCreateUpdateHeading('DanhMucHang');
                cy.get(entityCreateSaveButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', danhMucHangPageUrlPattern);
            });

            it('last delete button click should delete instance of DanhMucHang', () => {
                cy.intercept('GET', '/api/danh-muc-hangs/*').as('dialogDeleteRequest');
                cy.get(entityDeleteButtonSelector).last().click();
                cy.wait('@dialogDeleteRequest');
                cy.getEntityDeleteDialogHeading('danhMucHang').should('exist');
                cy.get(entityConfirmDeleteButtonSelector).click();
                cy.wait('@deleteEntityRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(204);
                });
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', danhMucHangPageUrlPattern);

                danhMucHang = undefined;
            });
        });
    });

    describe('new DanhMucHang page', () => {
        beforeEach(() => {
            cy.visit(`${danhMucHangPageUrl}`);
            cy.get(entityCreateButtonSelector).click();
            cy.getEntityCreateUpdateHeading('DanhMucHang');
        });

        it('should create an instance of DanhMucHang', () => {
            cy.get(`[data-cy="maHang"]`).type('as reconst');
            cy.get(`[data-cy="maHang"]`).should('have.value', 'as reconst');

            cy.get(`[data-cy="tenHang"]`).type('ugh');
            cy.get(`[data-cy="tenHang"]`).should('have.value', 'ugh');

            cy.get(`[data-cy="donVitinh"]`).type('stump famously stiffen');
            cy.get(`[data-cy="donVitinh"]`).should('have.value', 'stump famously stiffen');

            cy.get(`[data-cy="noiSanXuat"]`).type('tightly extra-large lest');
            cy.get(`[data-cy="noiSanXuat"]`).should('have.value', 'tightly extra-large lest');

            cy.get(`[data-cy="ngaySanXuat"]`).type('2025-04-27');
            cy.get(`[data-cy="ngaySanXuat"]`).blur();
            cy.get(`[data-cy="ngaySanXuat"]`).should('have.value', '2025-04-27');

            cy.get(`[data-cy="hanSuDung"]`).type('2025-04-28');
            cy.get(`[data-cy="hanSuDung"]`).blur();
            cy.get(`[data-cy="hanSuDung"]`).should('have.value', '2025-04-28');

            cy.get(`[data-cy="createdAt"]`).type('2025-04-27T12:17');
            cy.get(`[data-cy="createdAt"]`).blur();
            cy.get(`[data-cy="createdAt"]`).should('have.value', '2025-04-27T12:17');

            cy.get(`[data-cy="createdBy"]`).type('covenant than');
            cy.get(`[data-cy="createdBy"]`).should('have.value', 'covenant than');

            cy.get(`[data-cy="updatedAt"]`).type('2025-04-27T10:50');
            cy.get(`[data-cy="updatedAt"]`).blur();
            cy.get(`[data-cy="updatedAt"]`).should('have.value', '2025-04-27T10:50');

            cy.get(`[data-cy="updatedBy"]`).type('often defiantly');
            cy.get(`[data-cy="updatedBy"]`).should('have.value', 'often defiantly');

            cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
            cy.get(`[data-cy="isDeleted"]`).click();
            cy.get(`[data-cy="isDeleted"]`).should('be.checked');

            cy.get(entityCreateSaveButtonSelector).click();

            cy.wait('@postEntityRequest').then(({response}) => {
                expect(response?.statusCode).to.equal(201);
                danhMucHang = response.body;
            });
            cy.wait('@entitiesRequest').then(({response}) => {
                expect(response?.statusCode).to.equal(200);
            });
            cy.url().should('match', danhMucHangPageUrlPattern);
        });
    });
});
