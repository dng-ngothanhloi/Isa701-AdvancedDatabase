import React, {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT} from 'app/config/constants';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntity} from './khach-hang.reducer';

export const KhachHangDetail = () => {
    const dispatch = useAppDispatch();

    const {id} = useParams<'id'>();

    useEffect(() => {
        dispatch(getEntity(id));
    }, []);

    const khachHangEntity = useAppSelector(state => state.khachHang.entity);
    return (
        <Row>
            <Col md="8">
                <h2 data-cy="khachHangDetailsHeading">
                    <Translate contentKey="warehousMmgmtApp.khachHang.detail.title">KhachHang</Translate>
                </h2>
                <dl className="jh-entity-details">
                    <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.id}</dd>
                    <dt>
            <span id="maKH">
              <Translate contentKey="warehousMmgmtApp.khachHang.maKH">Ma KH</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.maKH}</dd>
                    <dt>
            <span id="tenKH">
              <Translate contentKey="warehousMmgmtApp.khachHang.tenKH">Ten KH</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.tenKH}</dd>
                    <dt>
            <span id="goiTinh">
              <Translate contentKey="warehousMmgmtApp.khachHang.goiTinh">Goi Tinh</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.goiTinh}</dd>
                    <dt>
            <span id="dateOfBirth">
              <Translate contentKey="warehousMmgmtApp.khachHang.dateOfBirth">Date Of Birth</Translate>
            </span>
                    </dt>
                    <dd>
                        {khachHangEntity.dateOfBirth ? (
                            <TextFormat value={khachHangEntity.dateOfBirth} type="date" format={APP_LOCAL_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="diaChi">
              <Translate contentKey="warehousMmgmtApp.khachHang.diaChi">Dia Chi</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.diaChi}</dd>
                    <dt>
            <span id="createdAt">
              <Translate contentKey="warehousMmgmtApp.khachHang.createdAt">Created At</Translate>
            </span>
                    </dt>
                    <dd>
                        {khachHangEntity.createdAt ?
                            <TextFormat value={khachHangEntity.createdAt} type="date" format={APP_DATE_FORMAT}/> : null}
                    </dd>
                    <dt>
            <span id="createdBy">
              <Translate contentKey="warehousMmgmtApp.khachHang.createdBy">Created By</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.createdBy}</dd>
                    <dt>
            <span id="updatedAt">
              <Translate contentKey="warehousMmgmtApp.khachHang.updatedAt">Updated At</Translate>
            </span>
                    </dt>
                    <dd>
                        {khachHangEntity.updatedAt ?
                            <TextFormat value={khachHangEntity.updatedAt} type="date" format={APP_DATE_FORMAT}/> : null}
                    </dd>
                    <dt>
            <span id="updatedBy">
              <Translate contentKey="warehousMmgmtApp.khachHang.updatedBy">Updated By</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.updatedBy}</dd>
                    <dt>
            <span id="isDeleted">
              <Translate contentKey="warehousMmgmtApp.khachHang.isDeleted">Is Deleted</Translate>
            </span>
                    </dt>
                    <dd>{khachHangEntity.isDeleted ? 'true' : 'false'}</dd>
                </dl>
                <Button tag={Link} to="/khach-hang" replace color="info" data-cy="entityDetailsBackButton">
                    <FontAwesomeIcon icon="arrow-left"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
                </Button>
                &nbsp;
                <Button tag={Link} to={`/khach-hang/${khachHangEntity.id}/edit`} replace color="primary">
                    <FontAwesomeIcon icon="pencil-alt"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
                </Button>
            </Col>
        </Row>
    );
};

export default KhachHangDetail;
