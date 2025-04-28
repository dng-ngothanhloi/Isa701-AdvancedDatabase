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

import {getEntities} from './phieu-nhap-xuat.reducer';

export const PhieuNhapXuat = () => {
    const dispatch = useAppDispatch();

    const pageLocation = useLocation();
    const navigate = useNavigate();

    const [paginationState, setPaginationState] = useState(
        overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
    );

    const phieuNhapXuatList = useAppSelector(state => state.phieuNhapXuat.entities);
    const loading = useAppSelector(state => state.phieuNhapXuat.loading);
    const totalItems = useAppSelector(state => state.phieuNhapXuat.totalItems);

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
            <h2 id="phieu-nhap-xuat-heading" data-cy="PhieuNhapXuatHeading">
                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.home.title">Phieu Nhap Xuats</Translate>
                <div className="d-flex justify-content-end">
                    <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
                        <FontAwesomeIcon icon="sync" spin={loading}/>{' '}
                        <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.home.refreshListLabel">Refresh
                            List</Translate>
                    </Button>
                    <Link to="/phieu-nhap-xuat/new" className="btn btn-primary jh-create-entity" id="jh-create-entity"
                          data-cy="entityCreateButton">
                        <FontAwesomeIcon icon="plus"/>
                        &nbsp;
                        <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.home.createLabel">Create new Phieu Nhap
                            Xuat</Translate>
                    </Link>
                </div>
            </h2>
            <div className="table-responsive">
                {phieuNhapXuatList && phieuNhapXuatList.length > 0 ? (
                    <Table responsive>
                        <thead>
                        <tr>
                            <th className="hand" onClick={sort('id')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.id">ID</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('id')}/>
                            </th>
                            <th className="hand" onClick={sort('maPhieu')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.maPhieu">Ma Phieu</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('maPhieu')}/>
                            </th>
                            <th className="hand" onClick={sort('ngayLapPhieu')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.ngayLapPhieu">Ngay Lap
                                    Phieu</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('ngayLapPhieu')}/>
                            </th>
                            <th className="hand" onClick={sort('loaiPhieu')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.loaiPhieu">Loai
                                    Phieu</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('loaiPhieu')}/>
                            </th>
                            <th className="hand" onClick={sort('createdAt')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.createdAt">Created
                                    At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')}/>
                            </th>
                            <th className="hand" onClick={sort('createdBy')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.createdBy">Created
                                    By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedAt')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.updatedAt">Updated
                                    At</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')}/>
                            </th>
                            <th className="hand" onClick={sort('updatedBy')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.updatedBy">Updated
                                    By</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')}/>
                            </th>
                            <th className="hand" onClick={sort('isDeleted')}>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.isDeleted">Is
                                    Deleted</Translate>{' '}
                                <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')}/>
                            </th>
                            <th>
                                <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.khachHang">Khach Hang</Translate>
                                <FontAwesomeIcon icon="sort"/>
                            </th>
                            <th/>
                        </tr>
                        </thead>
                        <tbody>
                        {phieuNhapXuatList.map((phieuNhapXuat, i) => (
                            <tr key={`entity-${i}`} data-cy="entityTable">
                                <td>
                                    <Button tag={Link} to={`/phieu-nhap-xuat/${phieuNhapXuat.id}`} color="link"
                                            size="sm">
                                        {phieuNhapXuat.id}
                                    </Button>
                                </td>
                                <td>{phieuNhapXuat.maPhieu}</td>
                                <td>
                                    {phieuNhapXuat.ngayLapPhieu ? (
                                        <TextFormat type="date" value={phieuNhapXuat.ngayLapPhieu}
                                                    format={APP_LOCAL_DATE_FORMAT}/>
                                    ) : null}
                                </td>
                                <td>
                                    <Translate contentKey={`warehousMmgmtApp.VoucherType.${phieuNhapXuat.loaiPhieu}`}/>
                                </td>
                                <td>
                                    {phieuNhapXuat.createdAt ? <TextFormat type="date" value={phieuNhapXuat.createdAt}
                                                                           format={APP_DATE_FORMAT}/> : null}
                                </td>
                                <td>{phieuNhapXuat.createdBy}</td>
                                <td>
                                    {phieuNhapXuat.updatedAt ? <TextFormat type="date" value={phieuNhapXuat.updatedAt}
                                                                           format={APP_DATE_FORMAT}/> : null}
                                </td>
                                <td>{phieuNhapXuat.updatedBy}</td>
                                <td>{phieuNhapXuat.isDeleted ? 'true' : 'false'}</td>
                                <td>
                                    {phieuNhapXuat.khachHang ? (
                                        <Link
                                            to={`/khach-hang/${phieuNhapXuat.khachHang.id}`}>{phieuNhapXuat.khachHang.id}</Link>
                                    ) : (
                                        ''
                                    )}
                                </td>
                                <td className="text-end">
                                    <div className="btn-group flex-btn-group-container">
                                        <Button tag={Link} to={`/phieu-nhap-xuat/${phieuNhapXuat.id}`} color="info"
                                                size="sm" data-cy="entityDetailsButton">
                                            <FontAwesomeIcon icon="eye"/>{' '}
                                            <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                                        </Button>
                                        <Button
                                            tag={Link}
                                            to={`/phieu-nhap-xuat/${phieuNhapXuat.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                                                (window.location.href = `/phieu-nhap-xuat/${phieuNhapXuat.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
                            <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.home.notFound">No Phieu Nhap Xuats
                                found</Translate>
                        </div>
                    )
                )}
            </div>
            {totalItems ? (
                <div className={phieuNhapXuatList && phieuNhapXuatList.length > 0 ? '' : 'd-none'}>
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

export default PhieuNhapXuat;
