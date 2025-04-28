import React, {useEffect, useState} from 'react';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import {Button, Table} from 'reactstrap';
import {JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSort, faSortDown, faSortUp} from '@fortawesome/free-solid-svg-icons';
import {APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT} from 'app/config/constants';
import {ASC, DESC, ITEMS_PER_PAGE, SORT} from 'app/shared/util/pagination.constants';
import {overridePaginationStateWithQueryParams} from 'app/shared/util/entity-utils';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntities} from './khach-hang.reducer';

export const KhachHang = () => {
    const dispatch = useAppDispatch();

    const pageLocation = useLocation();
    const navigate = useNavigate();

    const [paginationState, setPaginationState] = useState(
        overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
    );

    const khachHangList = useAppSelector(state => state.khachHang.entities);
    const loading = useAppSelector(state => state.khachHang.loading);
    const totalItems = useAppSelector(state => state.khachHang.totalItems);

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
            <h2 id="khach-hang-heading" data-cy="KhachHangHeading">
                <Translate contentKey="warehousMmgmtApp.khachHang.home.title">Khach Hangs</Translate>
                <div className="d-flex justify-content-end">
                    <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                        <FontAwesomeIcon icon="sync" spin={loading}/>{' '}
                        <Translate contentKey="warehousMmgmtApp.khachHang.home.refreshListLabel">Refresh
                            List</Translate>
                    </Button>
                    <Link to="/khach-hang/new" className="btn btn-primary jh-create-entity" id="jh-create-entity"
                          data-cy="entityCreateButton">
                        <FontAwesomeIcon icon="plus"/>
                        &nbsp;
                        <Translate contentKey="warehousMmgmtApp.khachHang.home.createLabel">Create new Khach
                            Hang</Translate>
                    </Link>
                </div>
            </h2>
            <div className="table-responsive">
                {khachHangList && khachHangList.length > 0 ? (
                    <Table responsive>
                        <thead>
                        <tr>
                            <th className="hand" onClick={sort('id')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.id">ID</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('id')}/>
                            </th>
                            <th className="hand" onClick={sort('maKH')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.maKH">Ma KH</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('maKH')}/>
                            </th>
                            <th className="hand" onClick={sort('tenKH')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.tenKH">Ten KH</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('tenKH')}/>
                            </th>
                            <th className="hand" onClick={sort('goiTinh')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.goiTinh">Goi Tinh</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('goiTinh')}/>
                            </th>
                            <th className="hand" onClick={sort('dateOfBirth')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.dateOfBirth">Date Of
                                    Birth</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('dateOfBirth')}/>
                            </th>
                            <th className="hand" onClick={sort('diaChi')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.diaChi">Dia Chi</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('diaChi')}/>
                            </th>
                            <th className="hand" onClick={sort('createdAt')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.createdAt">Created At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')}/>
                            </th>
                            <th className="hand" onClick={sort('createdBy')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.createdBy">Created By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedAt')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.updatedAt">Updated At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedBy')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.updatedBy">Updated By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')}/>
                            </th>
                            <th className="hand" onClick={sort('isDeleted')}>
                                <Translate contentKey="warehousMmgmtApp.khachHang.isDeleted">Is Deleted</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')}/>
                            </th>
                            <th/>
                        </tr>
                        </thead>
                        <tbody>
                        {khachHangList.map((khachHang, i) => (
                            <tr key={`entity-${i}`} data-cy="entityTable">
                                <td>
                                    <Button tag={Link} to={`/khach-hang/${khachHang.id}`} color="link" size="sm">
                                        {khachHang.id}
                                    </Button>
                                </td>
                                <td>{khachHang.maKH}</td>
                                <td>{khachHang.tenKH}</td>
                                <td>
                                    <Translate contentKey={`warehousMmgmtApp.EmpSex.${khachHang.goiTinh}`}/>
                                </td>
                                <td>
                                    {khachHang.dateOfBirth ? <TextFormat type="date" value={khachHang.dateOfBirth}
                                                                         format={APP_LOCAL_DATE_FORMAT}/> : null}
                                </td>
                                <td>{khachHang.diaChi}</td>
                                <td>{khachHang.createdAt ? <TextFormat type="date" value={khachHang.createdAt}
                                                                       format={APP_DATE_FORMAT}/> : null}</td>
                                <td>{khachHang.createdBy}</td>
                                <td>{khachHang.updatedAt ? <TextFormat type="date" value={khachHang.updatedAt}
                                                                       format={APP_DATE_FORMAT}/> : null}</td>
                                <td>{khachHang.updatedBy}</td>
                                <td>{khachHang.isDeleted ? 'true' : 'false'}</td>
                                <td className="text-end">
                                    <div className="btn-group flex-btn-group-container">
                                        <Button tag={Link} to={`/khach-hang/${khachHang.id}`} color="info" size="sm"
                                                data-cy="entityDetailsButton">
                                            <FontAwesomeIcon icon="eye"/>{' '}
                                            <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                                        </Button>
                                        <Button
                                            tag={Link}
                                            to={`/khach-hang/${khachHang.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                                                (window.location.href = `/khach-hang/${khachHang.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
                            <Translate contentKey="warehousMmgmtApp.khachHang.home.notFound">No Khach Hangs
                                found</Translate>
                        </div>
                    )
                )}
            </div>
            {totalItems ? (
                <div className={khachHangList && khachHangList.length > 0 ? '' : 'd-none'}>
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

export default KhachHang;
