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

import {getEntities} from './danh-muc-hang.reducer';

export const DanhMucHang = () => {
    const dispatch = useAppDispatch();

    const pageLocation = useLocation();
    const navigate = useNavigate();

    const [paginationState, setPaginationState] = useState(
        overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
    );

    const danhMucHangList = useAppSelector(state => state.danhMucHang.entities);
    const loading = useAppSelector(state => state.danhMucHang.loading);
    const totalItems = useAppSelector(state => state.danhMucHang.totalItems);

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
            <h2 id="danh-muc-hang-heading" data-cy="DanhMucHangHeading">
                <Translate contentKey="warehousMmgmtApp.danhMucHang.home.title">Danh Muc Hangs</Translate>
                <div className="d-flex justify-content-end">
                    <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                        <FontAwesomeIcon icon="sync" spin={loading}/>{' '}
                        <Translate contentKey="warehousMmgmtApp.danhMucHang.home.refreshListLabel">Refresh
                            List</Translate>
                    </Button>
                    <Link to="/danh-muc-hang/new" className="btn btn-primary jh-create-entity" id="jh-create-entity"
                          data-cy="entityCreateButton">
                        <FontAwesomeIcon icon="plus"/>
                        &nbsp;
                        <Translate contentKey="warehousMmgmtApp.danhMucHang.home.createLabel">Create new Danh Muc
                            Hang</Translate>
                    </Link>
                </div>
            </h2>
            <div className="table-responsive">
                {danhMucHangList && danhMucHangList.length > 0 ? (
                    <Table responsive>
                        <thead>
                        <tr>
                            <th className="hand" onClick={sort('id')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.id">ID</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('id')}/>
                            </th>
                            <th className="hand" onClick={sort('maHang')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.maHang">Ma Hang</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('maHang')}/>
                            </th>
                            <th className="hand" onClick={sort('tenHang')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.tenHang">Ten Hang</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('tenHang')}/>
                            </th>
                            <th className="hand" onClick={sort('donVitinh')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.donVitinh">Don
                                    Vitinh</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('donVitinh')}/>
                            </th>
                            <th className="hand" onClick={sort('noiSanXuat')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.noiSanXuat">Noi San
                                    Xuat</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('noiSanXuat')}/>
                            </th>
                            <th className="hand" onClick={sort('ngaySanXuat')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.ngaySanXuat">Ngay San
                                    Xuat</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('ngaySanXuat')}/>
                            </th>
                            <th className="hand" onClick={sort('hanSuDung')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.hanSuDung">Han Su
                                    Dung</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('hanSuDung')}/>
                            </th>
                            <th className="hand" onClick={sort('createdAt')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.createdAt">Created
                                    At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')}/>
                            </th>
                            <th className="hand" onClick={sort('createdBy')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.createdBy">Created
                                    By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedAt')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.updatedAt">Updated
                                    At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedBy')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.updatedBy">Updated
                                    By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')}/>
                            </th>
                            <th className="hand" onClick={sort('isDeleted')}>
                                <Translate contentKey="warehousMmgmtApp.danhMucHang.isDeleted">Is
                                    Deleted</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')}/>
                            </th>
                            <th/>
                        </tr>
                        </thead>
                        <tbody>
                        {danhMucHangList.map((danhMucHang, i) => (
                            <tr key={`entity-${i}`} data-cy="entityTable">
                                <td>
                                    <Button tag={Link} to={`/danh-muc-hang/${danhMucHang.id}`} color="link" size="sm">
                                        {danhMucHang.id}
                                    </Button>
                                </td>
                                <td>{danhMucHang.maHang}</td>
                                <td>{danhMucHang.tenHang}</td>
                                <td>{danhMucHang.donVitinh}</td>
                                <td>{danhMucHang.noiSanXuat}</td>
                                <td>
                                    {danhMucHang.ngaySanXuat ? (
                                        <TextFormat type="date" value={danhMucHang.ngaySanXuat}
                                                    format={APP_LOCAL_DATE_FORMAT}/>
                                    ) : null}
                                </td>
                                <td>
                                    {danhMucHang.hanSuDung ? <TextFormat type="date" value={danhMucHang.hanSuDung}
                                                                         format={APP_LOCAL_DATE_FORMAT}/> : null}
                                </td>
                                <td>
                                    {danhMucHang.createdAt ? <TextFormat type="date" value={danhMucHang.createdAt}
                                                                         format={APP_DATE_FORMAT}/> : null}
                                </td>
                                <td>{danhMucHang.createdBy}</td>
                                <td>
                                    {danhMucHang.updatedAt ? <TextFormat type="date" value={danhMucHang.updatedAt}
                                                                         format={APP_DATE_FORMAT}/> : null}
                                </td>
                                <td>{danhMucHang.updatedBy}</td>
                                <td>{danhMucHang.isDeleted ? 'true' : 'false'}</td>
                                <td className="text-end">
                                    <div className="btn-group flex-btn-group-container">
                                        <Button tag={Link} to={`/danh-muc-hang/${danhMucHang.id}`} color="info"
                                                size="sm" data-cy="entityDetailsButton">
                                            <FontAwesomeIcon icon="eye"/>{' '}
                                            <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                                        </Button>
                                        <Button
                                            tag={Link}
                                            to={`/danh-muc-hang/${danhMucHang.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                                                (window.location.href = `/danh-muc-hang/${danhMucHang.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
                            <Translate contentKey="warehousMmgmtApp.danhMucHang.home.notFound">No Danh Muc Hangs
                                found</Translate>
                        </div>
                    )
                )}
            </div>
            {totalItems ? (
                <div className={danhMucHangList && danhMucHangList.length > 0 ? '' : 'd-none'}>
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

export default DanhMucHang;
