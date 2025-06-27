import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './phieu-nhap-xuat.reducer';

export const PhieuNhapXuatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const phieuNhapXuatEntity = useAppSelector(state => state.phieuNhapXuat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phieuNhapXuatDetailsHeading">
          <Translate contentKey="warehouseMgmApp.phieuNhapXuat.detail.title">PhieuNhapXuat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="maPhieu">
              <Translate contentKey="warehouseMgmApp.phieuNhapXuat.maPhieu">Ma Phieu</Translate>
            </span>
          </dt>
          <dd>{phieuNhapXuatEntity.maPhieu}</dd>
          <dt>
            <span id="ngayLapPhieu">
              <Translate contentKey="warehouseMgmApp.phieuNhapXuat.ngayLapPhieu">Ngay Lap Phieu</Translate>
            </span>
          </dt>
          <dd>{phieuNhapXuatEntity.ngayLapPhieu}</dd>
          <dt>
            <span id="loaiPhieu">
              <Translate contentKey="warehouseMgmApp.phieuNhapXuat.loaiPhieu">Loai Phieu</Translate>
            </span>
          </dt>
          <dd>{phieuNhapXuatEntity.loaiPhieu}</dd>
          <dt>
            <span id="khachHang">
              <Translate contentKey="warehouseMgmApp.phieuNhapXuat.khachHang">khach Hang</Translate>
            </span>
          </dt>
          <dd>{phieuNhapXuatEntity.khachHang ? phieuNhapXuatEntity.tenKhachHang : ''}</dd>
          <dt>
            <span id="detailNhapXuat">
              {phieuNhapXuatEntity.chiTietNhapXuatDTOList && phieuNhapXuatEntity.chiTietNhapXuatDTOList.length > 0 ? (
                <Table responsive>
                  <thead>
                    <tr>
                      <th>
                        <Translate contentKey="warehouseMgmApp.danhMucHang.maHang">Ma Hang</Translate>
                      </th>
                      <th>
                        <Translate contentKey="warehouseMgmApp.danhMucHang.tenHang">Ten Hang</Translate>
                      </th>
                      <th className="hand">
                        <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.soLuong">So Luong</Translate>{' '}
                      </th>
                      <th className="hand">
                        <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.donGia">Don Gia</Translate>{' '}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {phieuNhapXuatEntity.chiTietNhapXuatDTOList.map((chiTietNhapXuat, i) => (
                      <tr key={`entity-${i}`} data-cy="entityTable">
                        <td>
                          {chiTietNhapXuat.maHang ? (
                            <Link to={`/danh-muc-hang/${chiTietNhapXuat.maHang.id}`}>{chiTietNhapXuat.maHang.maHang}</Link>
                          ) : (
                            ''
                          )}
                        </td>
                        <td>{chiTietNhapXuat.maHang.tenHang}</td>
                        <td>{chiTietNhapXuat.soLuong}</td>
                        <td>{chiTietNhapXuat.donGia}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              ) : null}
            </span>
          </dt>
        </dl>
        <Button tag={Link} to="/phieu-nhap-xuat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phieu-nhap-xuat/${phieuNhapXuatEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhieuNhapXuatDetail;
