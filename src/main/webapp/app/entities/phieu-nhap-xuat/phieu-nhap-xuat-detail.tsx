import React, {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT} from 'app/config/constants';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntity} from './phieu-nhap-xuat.reducer';

export const PhieuNhapXuatDetail = () => {
    const dispatch = useAppDispatch();

    const {id} = useParams<'id'>();

    useEffect(() => {
        dispatch(getEntity(id));
    }, []);

    const phieuNhapXuatEntity = useAppSelector(state => state.phieuNhapXuat.entity);
    return (
        <Row>
            <Col md="8">
                <h2 data-cy="phieuNhapXuatDetailsHeading">
                    <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.detail.title">PhieuNhapXuat</Translate>
                </h2>
                <dl className="jh-entity-details">
                    <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
                    </dt>
                    <dd>{phieuNhapXuatEntity.id}</dd>
                    <dt>
            <span id="maPhieu">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.maPhieu">Ma Phieu</Translate>
            </span>
                    </dt>
                    <dd>{phieuNhapXuatEntity.maPhieu}</dd>
                    <dt>
            <span id="ngayLapPhieu">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.ngayLapPhieu">Ngay Lap Phieu</Translate>
            </span>
                    </dt>
                    <dd>
                        {phieuNhapXuatEntity.ngayLapPhieu ? (
                            <TextFormat value={phieuNhapXuatEntity.ngayLapPhieu} type="date"
                                        format={APP_LOCAL_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="loaiPhieu">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.loaiPhieu">Loai Phieu</Translate>
            </span>
                    </dt>
                    <dd>{phieuNhapXuatEntity.loaiPhieu}</dd>
                    <dt>
            <span id="createdAt">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.createdAt">Created At</Translate>
            </span>
                    </dt>
                    <dd>
                        {phieuNhapXuatEntity.createdAt ? (
                            <TextFormat value={phieuNhapXuatEntity.createdAt} type="date" format={APP_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="createdBy">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.createdBy">Created By</Translate>
            </span>
                    </dt>
                    <dd>{phieuNhapXuatEntity.createdBy}</dd>
                    <dt>
            <span id="updatedAt">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.updatedAt">Updated At</Translate>
            </span>
                    </dt>
                    <dd>
                        {phieuNhapXuatEntity.updatedAt ? (
                            <TextFormat value={phieuNhapXuatEntity.updatedAt} type="date" format={APP_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="updatedBy">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.updatedBy">Updated By</Translate>
            </span>
                    </dt>
                    <dd>{phieuNhapXuatEntity.updatedBy}</dd>
                    <dt>
            <span id="isDeleted">
              <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.isDeleted">Is Deleted</Translate>
            </span>
                    </dt>
                    <dd>{phieuNhapXuatEntity.isDeleted ? 'true' : 'false'}</dd>
                    <dt>
                        <Translate contentKey="warehousMmgmtApp.phieuNhapXuat.khachHang">Khach Hang</Translate>
                    </dt>
                    <dd>{phieuNhapXuatEntity.khachHang ? phieuNhapXuatEntity.khachHang.id : ''}</dd>
                </dl>
                <Button tag={Link} to="/phieu-nhap-xuat" replace color="info" data-cy="entityDetailsBackButton">
                    <FontAwesomeIcon icon="arrow-left"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
                </Button>
                &nbsp;
                <Button tag={Link} to={`/phieu-nhap-xuat/${phieuNhapXuatEntity.id}/edit`} replace color="primary">
                    <FontAwesomeIcon icon="pencil-alt"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
                </Button>
            </Col>
        </Row>
    );
};

export default PhieuNhapXuatDetail;
