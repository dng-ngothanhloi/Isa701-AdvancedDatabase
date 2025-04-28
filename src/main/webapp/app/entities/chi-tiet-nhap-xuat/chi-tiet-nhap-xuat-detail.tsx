import React, {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {APP_DATE_FORMAT} from 'app/config/constants';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntity} from './chi-tiet-nhap-xuat.reducer';

export const ChiTietNhapXuatDetail = () => {
    const dispatch = useAppDispatch();

    const {id} = useParams<'id'>();

    useEffect(() => {
        dispatch(getEntity(id));
    }, []);

    const chiTietNhapXuatEntity = useAppSelector(state => state.chiTietNhapXuat.entity);
    return (
        <Row>
            <Col md="8">
                <h2 data-cy="chiTietNhapXuatDetailsHeading">
                    <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.detail.title">ChiTietNhapXuat</Translate>
                </h2>
                <dl className="jh-entity-details">
                    <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.id}</dd>
                    <dt>
            <span id="soLuong">
              <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.soLuong">So Luong</Translate>
            </span>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.soLuong}</dd>
                    <dt>
            <span id="donGia">
              <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.donGia">Don Gia</Translate>
            </span>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.donGia}</dd>
                    <dt>
            <span id="createdAt">
              <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.createdAt">Created At</Translate>
            </span>
                    </dt>
                    <dd>
                        {chiTietNhapXuatEntity.createdAt ? (
                            <TextFormat value={chiTietNhapXuatEntity.createdAt} type="date" format={APP_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="createdBy">
              <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.createdBy">Created By</Translate>
            </span>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.createdBy}</dd>
                    <dt>
            <span id="updatedAt">
              <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.updatedAt">Updated At</Translate>
            </span>
                    </dt>
                    <dd>
                        {chiTietNhapXuatEntity.updatedAt ? (
                            <TextFormat value={chiTietNhapXuatEntity.updatedAt} type="date" format={APP_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="updatedBy">
              <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.updatedBy">Updated By</Translate>
            </span>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.updatedBy}</dd>
                    <dt>
            <span id="isDeleted">
              <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.isDeleted">Is Deleted</Translate>
            </span>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.isDeleted ? 'true' : 'false'}</dd>
                    <dt>
                        <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.phieuNhapXuat">Phieu Nhap
                            Xuat</Translate>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.phieuNhapXuat ? chiTietNhapXuatEntity.phieuNhapXuat.id : ''}</dd>
                    <dt>
                        <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.maHang">Ma Hang</Translate>
                    </dt>
                    <dd>{chiTietNhapXuatEntity.maHang ? chiTietNhapXuatEntity.maHang.id : ''}</dd>
                </dl>
                <Button tag={Link} to="/chi-tiet-nhap-xuat" replace color="info" data-cy="entityDetailsBackButton">
                    <FontAwesomeIcon icon="arrow-left"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
                </Button>
                &nbsp;
                <Button tag={Link} to={`/chi-tiet-nhap-xuat/${chiTietNhapXuatEntity.id}/edit`} replace color="primary">
                    <FontAwesomeIcon icon="pencil-alt"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
                </Button>
            </Col>
        </Row>
    );
};

export default ChiTietNhapXuatDetail;
