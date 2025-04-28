import React, {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {TextFormat, Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT} from 'app/config/constants';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntity} from './danh-muc-hang.reducer';

export const DanhMucHangDetail = () => {
    const dispatch = useAppDispatch();

    const {id} = useParams<'id'>();

    useEffect(() => {
        dispatch(getEntity(id));
    }, []);

    const danhMucHangEntity = useAppSelector(state => state.danhMucHang.entity);
    return (
        <Row>
            <Col md="8">
                <h2 data-cy="danhMucHangDetailsHeading">
                    <Translate contentKey="warehousMmgmtApp.danhMucHang.detail.title">DanhMucHang</Translate>
                </h2>
                <dl className="jh-entity-details">
                    <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.id}</dd>
                    <dt>
            <span id="maHang">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.maHang">Ma Hang</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.maHang}</dd>
                    <dt>
            <span id="tenHang">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.tenHang">Ten Hang</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.tenHang}</dd>
                    <dt>
            <span id="donVitinh">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.donVitinh">Don Vitinh</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.donVitinh}</dd>
                    <dt>
            <span id="noiSanXuat">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.noiSanXuat">Noi San Xuat</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.noiSanXuat}</dd>
                    <dt>
            <span id="ngaySanXuat">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.ngaySanXuat">Ngay San Xuat</Translate>
            </span>
                    </dt>
                    <dd>
                        {danhMucHangEntity.ngaySanXuat ? (
                            <TextFormat value={danhMucHangEntity.ngaySanXuat} type="date"
                                        format={APP_LOCAL_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="hanSuDung">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.hanSuDung">Han Su Dung</Translate>
            </span>
                    </dt>
                    <dd>
                        {danhMucHangEntity.hanSuDung ? (
                            <TextFormat value={danhMucHangEntity.hanSuDung} type="date" format={APP_LOCAL_DATE_FORMAT}/>
                        ) : null}
                    </dd>
                    <dt>
            <span id="createdAt">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.createdAt">Created At</Translate>
            </span>
                    </dt>
                    <dd>
                        {danhMucHangEntity.createdAt ? <TextFormat value={danhMucHangEntity.createdAt} type="date"
                                                                   format={APP_DATE_FORMAT}/> : null}
                    </dd>
                    <dt>
            <span id="createdBy">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.createdBy">Created By</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.createdBy}</dd>
                    <dt>
            <span id="updatedAt">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.updatedAt">Updated At</Translate>
            </span>
                    </dt>
                    <dd>
                        {danhMucHangEntity.updatedAt ? <TextFormat value={danhMucHangEntity.updatedAt} type="date"
                                                                   format={APP_DATE_FORMAT}/> : null}
                    </dd>
                    <dt>
            <span id="updatedBy">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.updatedBy">Updated By</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.updatedBy}</dd>
                    <dt>
            <span id="isDeleted">
              <Translate contentKey="warehousMmgmtApp.danhMucHang.isDeleted">Is Deleted</Translate>
            </span>
                    </dt>
                    <dd>{danhMucHangEntity.isDeleted ? 'true' : 'false'}</dd>
                </dl>
                <Button tag={Link} to="/danh-muc-hang" replace color="info" data-cy="entityDetailsBackButton">
                    <FontAwesomeIcon icon="arrow-left"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
                </Button>
                &nbsp;
                <Button tag={Link} to={`/danh-muc-hang/${danhMucHangEntity.id}/edit`} replace color="primary">
                    <FontAwesomeIcon icon="pencil-alt"/>{' '}
                    <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
                </Button>
            </Col>
        </Row>
    );
};

export default DanhMucHangDetail;
