import React, {useEffect, useState} from 'react';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import {Button, Table} from 'reactstrap';
import {JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSort, faSortDown, faSortUp} from '@fortawesome/free-solid-svg-icons';
import {APP_DATE_FORMAT} from 'app/config/constants';
import {ASC, DESC, ITEMS_PER_PAGE, SORT} from 'app/shared/util/pagination.constants';
import {overridePaginationStateWithQueryParams} from 'app/shared/util/entity-utils';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntities} from './masterdata.reducer';

export const Masterdata = () => {
    const dispatch = useAppDispatch();

    const pageLocation = useLocation();
    const navigate = useNavigate();

    const [paginationState, setPaginationState] = useState(
        overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
    );

    const masterdataList = useAppSelector(state => state.masterdata.entities);
    const loading = useAppSelector(state => state.masterdata.loading);
    const totalItems = useAppSelector(state => state.masterdata.totalItems);

    const getAllEntities = () => {
        dispatch(
            getEntities({
                page: paginationState.activePage - 1,
                size: paginationState.itemsPerPage,
                sort: `${paginationState.sort},${paginationState.order}`,
            }),
        );
    };

    const sortEntities = () => {
        getAllEntities();
        const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
        if (pageLocation.search !== endURL) {
            navigate(`${pageLocation.pathname}${endURL}`);
        }
    };

    useEffect(() => {
        sortEntities();
    }, [paginationState.activePage, paginationState.order, paginationState.sort]);

    useEffect(() => {
        const params = new URLSearchParams(pageLocation.search);
        const page = params.get('page');
        const sort = params.get(SORT);
        if (page && sort) {
            const sortSplit = sort.split(',');
            setPaginationState({
                ...paginationState,
                activePage: +page,
                sort: sortSplit[0],
                order: sortSplit[1],
            });
        }
    }, [pageLocation.search]);

    const sort = p => () => {
        setPaginationState({
            ...paginationState,
            order: paginationState.order === ASC ? DESC : ASC,
            sort: p,
        });
    };

    const handlePagination = currentPage =>
        setPaginationState({
            ...paginationState,
            activePage: currentPage,
        });

    const handleSyncList = () => {
        sortEntities();
    };

    const getSortIconByFieldName = (fieldName: string) => {
        const sortFieldName = paginationState.sort;
        const order = paginationState.order;
        if (sortFieldName !== fieldName) {
            return faSort;
        }
        return order === ASC ? faSortUp : faSortDown;
    };

    return (
        <div>
            <h2 id="masterdata-heading" data-cy="MasterdataHeading">
                <Translate contentKey="warehousMmgmtApp.masterdata.home.title">Masterdata</Translate>
                <div className="d-flex justify-content-end">
                    <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                        <FontAwesomeIcon icon="sync" spin={loading}/>{' '}
                        <Translate contentKey="warehousMmgmtApp.masterdata.home.refreshListLabel">Refresh
                            List</Translate>
                    </Button>
                    <Link to="/masterdata/new" className="btn btn-primary jh-create-entity" id="jh-create-entity"
                          data-cy="entityCreateButton">
                        <FontAwesomeIcon icon="plus"/>
                        &nbsp;
                        <Translate contentKey="warehousMmgmtApp.masterdata.home.createLabel">Create new
                            Masterdata</Translate>
                    </Link>
                </div>
            </h2>
            <div className="table-responsive">
                {masterdataList && masterdataList.length > 0 ? (
                    <Table responsive>
                        <thead>
                        <tr>
                            <th className="hand" onClick={sort('id')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.id">ID</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('id')}/>
                            </th>
                            <th className="hand" onClick={sort('category')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.category">Category</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('category')}/>
                            </th>
                            <th className="hand" onClick={sort('dataKey')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.dataKey">Data Key</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('dataKey')}/>
                            </th>
                            <th className="hand" onClick={sort('dataValue')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.dataValue">Data
                                    Value</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('dataValue')}/>
                            </th>
                            <th className="hand" onClick={sort('isDeleted')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.isDeleted">Is
                                    Deleted</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')}/>
                            </th>
                            <th className="hand" onClick={sort('createdAt')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.createdAt">Created
                                    At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')}/>
                            </th>
                            <th className="hand" onClick={sort('createdBy')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.createdBy">Created
                                    By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedAt')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.updatedAt">Updated
                                    At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedBy')}>
                                <Translate contentKey="warehousMmgmtApp.masterdata.updatedBy">Updated
                                    By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')}/>
                            </th>
                            <th/>
                        </tr>
                        </thead>
                        <tbody>
                        {masterdataList.map((masterdata, i) => (
                            <tr key={`entity-${i}`} data-cy="entityTable">
                                <td>
                                    <Button tag={Link} to={`/masterdata/${masterdata.id}`} color="link" size="sm">
                                        {masterdata.id}
                                    </Button>
                                </td>
                                <td>{masterdata.category}</td>
                                <td>{masterdata.dataKey}</td>
                                <td>{masterdata.dataValue}</td>
                                <td>{masterdata.isDeleted ? 'true' : 'false'}</td>
                                <td>{masterdata.createdAt ? <TextFormat type="date" value={masterdata.createdAt}
                                                                        format={APP_DATE_FORMAT}/> : null}</td>
                                <td>{masterdata.createdBy}</td>
                                <td>{masterdata.updatedAt ? <TextFormat type="date" value={masterdata.updatedAt}
                                                                        format={APP_DATE_FORMAT}/> : null}</td>
                                <td>{masterdata.updatedBy}</td>
                                <td className="text-end">
                                    <div className="btn-group flex-btn-group-container">
                                        <Button tag={Link} to={`/masterdata/${masterdata.id}`} color="info" size="sm"
                                                data-cy="entityDetailsButton">
                                            <FontAwesomeIcon icon="eye"/>{' '}
                                            <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                                        </Button>
                                        <Button
                                            tag={Link}
                                            to={`/masterdata/${masterdata.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                                            color="primary"
                                            size="sm"
                                            data-cy="entityEditButton"
                                        >
                                            <FontAwesomeIcon icon="pencil-alt"/>{' '}
                                            <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                                        </Button>
                                        <Button
                                            onClick={() =>
                                                (window.location.href = `/masterdata/${masterdata.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                                            }
                                            color="danger"
                                            size="sm"
                                            data-cy="entityDeleteButton"
                                        >
                                            <FontAwesomeIcon icon="trash"/>{' '}
                                            <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                                        </Button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </Table>
                ) : (
                    !loading && (
                        <div className="alert alert-warning">
                            <Translate contentKey="warehousMmgmtApp.masterdata.home.notFound">No Masterdata
                                found</Translate>
                        </div>
                    )
                )}
            </div>
            {totalItems ? (
                <div className={masterdataList && masterdataList.length > 0 ? '' : 'd-none'}>
                    <div className="justify-content-center d-flex">
                        <JhiItemCount page={paginationState.activePage} total={totalItems}
                                      itemsPerPage={paginationState.itemsPerPage} i18nEnabled/>
                    </div>
                    <div className="justify-content-center d-flex">
                        <JhiPagination
                            activePage={paginationState.activePage}
                            onSelect={handlePagination}
                            maxButtons={5}
                            itemsPerPage={paginationState.itemsPerPage}
                            totalItems={totalItems}
                        />
                    </div>
                </div>
            ) : (
                ''
            )}
        </div>
    );
};

export default Masterdata;
