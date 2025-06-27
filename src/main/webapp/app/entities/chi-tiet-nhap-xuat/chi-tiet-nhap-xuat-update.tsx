import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPhieuNhapXuats } from 'app/entities/phieu-nhap-xuat/phieu-nhap-xuat.reducer';
import { getEntities as getDanhMucHangs } from 'app/entities/danh-muc-hang/danh-muc-hang.reducer';
import { createEntity, getEntity, reset, updateEntity } from './chi-tiet-nhap-xuat.reducer';

export const ChiTietNhapXuatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const phieuNhapXuats = useAppSelector(state => state.phieuNhapXuat.entities);
  const danhMucHangs = useAppSelector(state => state.danhMucHang.entities);
  const chiTietNhapXuatEntity = useAppSelector(state => state.chiTietNhapXuat.entity);
  const loading = useAppSelector(state => state.chiTietNhapXuat.loading);
  const updating = useAppSelector(state => state.chiTietNhapXuat.updating);
  const updateSuccess = useAppSelector(state => state.chiTietNhapXuat.updateSuccess);

  const handleClose = () => {
    navigate(`/chi-tiet-nhap-xuat${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPhieuNhapXuats({}));
    dispatch(getDanhMucHangs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.soLuong !== undefined && typeof values.soLuong !== 'number') {
      values.soLuong = Number(values.soLuong);
    }
    if (values.donGia !== undefined && typeof values.donGia !== 'number') {
      values.donGia = Number(values.donGia);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...chiTietNhapXuatEntity,
      ...values,
      phieuNhapXuat: phieuNhapXuats.find(it => it.id.toString() === values.phieuNhapXuat?.toString()),
      maHang: danhMucHangs.find(it => it.id.toString() === values.maHang?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...chiTietNhapXuatEntity,
          createdAt: convertDateTimeFromServer(chiTietNhapXuatEntity.createdAt),
          updatedAt: convertDateTimeFromServer(chiTietNhapXuatEntity.updatedAt),
          phieuNhapXuat: chiTietNhapXuatEntity?.phieuNhapXuat?.id,
          maHang: chiTietNhapXuatEntity?.maHang?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="warehouseMgmApp.chiTietNhapXuat.home.createOrEditLabel" data-cy="ChiTietNhapXuatCreateUpdateHeading">
            <Translate contentKey="warehouseMgmApp.chiTietNhapXuat.home.createOrEditLabel">Create or edit a ChiTietNhapXuat</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField
                label={translate('warehouseMgmApp.chiTietNhapXuat.soLuong')}
                id="chi-tiet-nhap-xuat-soLuong"
                name="soLuong"
                data-cy="soLuong"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.chiTietNhapXuat.donGia')}
                id="chi-tiet-nhap-xuat-donGia"
                name="donGia"
                data-cy="donGia"
                type="text"
              />
              <ValidatedField
                id="chi-tiet-nhap-xuat-phieuNhapXuat"
                name="phieuNhapXuat"
                data-cy="phieuNhapXuat"
                label={translate('warehouseMgmApp.chiTietNhapXuat.phieuNhapXuat')}
                type="select"
              >
                <option value="" key="0" />
                {phieuNhapXuats
                  ? phieuNhapXuats.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.maPhieu}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="chi-tiet-nhap-xuat-maHang"
                name="maHang"
                data-cy="maHang"
                label={translate('warehouseMgmApp.chiTietNhapXuat.maHang')}
                type="select"
              >
                <option value="" key="0" />
                {danhMucHangs
                  ? danhMucHangs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.tenHang}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/chi-tiet-nhap-xuat" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ChiTietNhapXuatUpdate;
