import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './chi-tiet-nhap-xuat.reducer';

export const ChiTietNhapXuat = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const chiTietNhapXuatList = useAppSelector(state => state.chiTietNhapXuat.entities);
  const loading = useAppSelector(state => state.chiTietNhapXuat.loading);
  const totalItems = useAppSelector(state => state.chiTietNhapXuat.totalItems);

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
      <h2 id="chi-tiet-nhap-xuat-heading" data-cy="ChiTietNhapXuatHeading">
        <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.home.title">Chi Tiet Nhap Xuats</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/chi-tiet-nhap-xuat/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.home.createLabel">Create new Chi Tiet Nhap Xuat</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {chiTietNhapXuatList && chiTietNhapXuatList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.phieuNhapXuat">Phieu Nhap Xuat</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.maHang">Ma Hang</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('soLuong')}>
                  <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.soLuong">So Luong</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('soLuong')} />
                </th>
                <th className="hand" onClick={sort('donGia')}>
                  <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.donGia">Don Gia</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('donGia')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {chiTietNhapXuatList.map((chiTietNhapXuat, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    {chiTietNhapXuat.phieuNhapXuat ? (
                      <Link to={`/phieu-nhap-xuat/${chiTietNhapXuat.phieuNhapXuat.id}`}>{chiTietNhapXuat.phieuNhapXuat.maPhieu}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {chiTietNhapXuat.maHang ? (
                      <Link to={`/danh-muc-hang/${chiTietNhapXuat.maHang.id}`}>{chiTietNhapXuat.maHang.maHang}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{chiTietNhapXuat.soLuong}</td>
                  <td>{chiTietNhapXuat.donGia}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/chi-tiet-nhap-xuat/${chiTietNhapXuat.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/chi-tiet-nhap-xuat/${chiTietNhapXuat.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/chi-tiet-nhap-xuat/${chiTietNhapXuat.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
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
              <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.home.notFound">No Chi Tiet Nhap Xuats found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={chiTietNhapXuatList && chiTietNhapXuatList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
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

export default ChiTietNhapXuat;
