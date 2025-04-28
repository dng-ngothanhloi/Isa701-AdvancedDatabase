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

describe('Masterdata e2e test', () => {
    const masterdataPageUrl = '/masterdata';
    const masterdataPageUrlPattern = new RegExp('/masterdata(\\?.*)?$');
    const username = Cypress.env('E2E_USERNAME') ?? 'user';
    const password = Cypress.env('E2E_PASSWORD') ?? 'user';
    const masterdataSample = {category: 'convince', dataKey: 'enormously descendant likewise', dataValue: 'doubter'};

    let masterdata;

    beforeEach(() => {
        cy.login(username, password);
    });

    beforeEach(() => {
        cy.intercept('GET', '/api/masterdata+(?*|)').as('entitiesRequest');
        cy.intercept('POST', '/api/masterdata').as('postEntityRequest');
        cy.intercept('DELETE', '/api/masterdata/*').as('deleteEntityRequest');
    });

    afterEach(() => {
        if (masterdata) {
            cy.authenticatedRequest({
                method: 'DELETE',
                url: `/api/masterdata/${masterdata.id}`,
            }).then(() => {
                masterdata = undefined;
            });
        }
    });

    it('Masterdata menu should load Masterdata page', () => {
        cy.visit('/');
        cy.clickOnEntityMenuItem('masterdata');
        cy.wait('@entitiesRequest').then(({response}) => {
            if (response?.body.length === 0) {
                cy.get(entityTableSelector).should('not.exist');
            } else {
                cy.get(entityTableSelector).should('exist');
            }
        });
        cy.getEntityHeading('Masterdata').should('exist');
        cy.url().should('match', masterdataPageUrlPattern);
    });

    describe('Masterdata page', () => {
        describe('create button click', () => {
            beforeEach(() => {
                cy.visit(masterdataPageUrl);
                cy.wait('@entitiesRequest');
            });

            it('should load create Masterdata page', () => {
                cy.get(entityCreateButtonSelector).click();
                cy.url().should('match', new RegExp('/masterdata/new$'));
                cy.getEntityCreateUpdateHeading('Masterdata');
                cy.get(entityCreateSaveButtonSelector).should('exist');
                cy.get(entityCreateCancelButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', masterdataPageUrlPattern);
            });
        });

        describe('with existing value', () => {
            beforeEach(() => {
                cy.authenticatedRequest({
                    method: 'POST',
                    url: '/api/masterdata',
                    body: masterdataSample,
                }).then(({body}) => {
                    masterdata = body;

                    cy.intercept(
                        {
                            method: 'GET',
                            url: '/api/masterdata+(?*|)',
                            times: 1,
                        },
                        {
                            statusCode: 200,
                            headers: {
                                link: '<http://localhost/api/masterdata?page=0&size=20>; rel="last",<http://localhost/api/masterdata?page=0&size=20>; rel="first"',
                            },
                            body: [masterdata],
                        },
                    ).as('entitiesRequestInternal');
                });

                cy.visit(masterdataPageUrl);

                cy.wait('@entitiesRequestInternal');
            });

            it('detail button click should load details Masterdata page', () => {
                cy.get(entityDetailsButtonSelector).first().click();
                cy.getEntityDetailsHeading('masterdata');
                cy.get(entityDetailsBackButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', masterdataPageUrlPattern);
            });

            it('edit button click should load edit Masterdata page and go back', () => {
                cy.get(entityEditButtonSelector).first().click();
                cy.getEntityCreateUpdateHeading('Masterdata');
                cy.get(entityCreateSaveButtonSelector).should('exist');
                cy.get(entityCreateCancelButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', masterdataPageUrlPattern);
            });

            it('edit button click should load edit Masterdata page and save', () => {
                cy.get(entityEditButtonSelector).first().click();
                cy.getEntityCreateUpdateHeading('Masterdata');
                cy.get(entityCreateSaveButtonSelector).click();
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', masterdataPageUrlPattern);
            });

            it('last delete button click should delete instance of Masterdata', () => {
                cy.intercept('GET', '/api/masterdata/*').as('dialogDeleteRequest');
                cy.get(entityDeleteButtonSelector).last().click();
                cy.wait('@dialogDeleteRequest');
                cy.getEntityDeleteDialogHeading('masterdata').should('exist');
                cy.get(entityConfirmDeleteButtonSelector).click();
                cy.wait('@deleteEntityRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(204);
                });
                cy.wait('@entitiesRequest').then(({response}) => {
                    expect(response?.statusCode).to.equal(200);
                });
                cy.url().should('match', masterdataPageUrlPattern);

                masterdata = undefined;
            });
        });
    });

    describe('new Masterdata page', () => {
        beforeEach(() => {
            cy.visit(`${masterdataPageUrl}`);
            cy.get(entityCreateButtonSelector).click();
            cy.getEntityCreateUpdateHeading('Masterdata');
        });

        it('should create an instance of Masterdata', () => {
            cy.get(`[data-cy="category"]`).type('ack');
            cy.get(`[data-cy="category"]`).should('have.value', 'ack');

            cy.get(`[data-cy="dataKey"]`).type('joshingly basket perfectly');
            cy.get(`[data-cy="dataKey"]`).should('have.value', 'joshingly basket perfectly');

            cy.get(`[data-cy="dataValue"]`).type('quaintly ha');
            cy.get(`[data-cy="dataValue"]`).should('have.value', 'quaintly ha');

            cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
            cy.get(`[data-cy="isDeleted"]`).click();
            cy.get(`[data-cy="isDeleted"]`).should('be.checked');

            cy.get(`[data-cy="createdAt"]`).type('2025-04-27T06:50');
            cy.get(`[data-cy="createdAt"]`).blur();
            cy.get(`[data-cy="createdAt"]`).should('have.value', '2025-04-27T06:50');

            cy.get(`[data-cy="createdBy"]`).type('when ew gleefully');
            cy.get(`[data-cy="createdBy"]`).should('have.value', 'when ew gleefully');

            cy.get(`[data-cy="updatedAt"]`).type('2025-04-28T01:55');
            cy.get(`[data-cy="updatedAt"]`).blur();
            cy.get(`[data-cy="updatedAt"]`).should('have.value', '2025-04-28T01:55');

            cy.get(`[data-cy="updatedBy"]`).type('questioningly narrate');
            cy.get(`[data-cy="updatedBy"]`).should('have.value', 'questioningly narrate');

            cy.get(entityCreateSaveButtonSelector).click();

            cy.wait('@postEntityRequest').then(({response}) => {
                expect(response?.statusCode).to.equal(201);
                masterdata = response.body;
            });
            cy.wait('@entitiesRequest').then(({response}) => {
                expect(response?.statusCode).to.equal(200);
            });
            cy.url().should('match', masterdataPageUrlPattern);
        });
    });
});
