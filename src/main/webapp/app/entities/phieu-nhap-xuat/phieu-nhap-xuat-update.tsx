import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getKhachHangs } from 'app/entities/khach-hang/khach-hang.reducer';
import { VoucherType } from 'app/shared/model/enumerations/voucher-type.model';
import { createEntity, getEntity, reset, updateEntity } from './phieu-nhap-xuat.reducer';

export const PhieuNhapXuatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const khachHangs = useAppSelector(state => state.khachHang.entities);
  const phieuNhapXuatEntity = useAppSelector(state => state.phieuNhapXuat.entity);
  const loading = useAppSelector(state => state.phieuNhapXuat.loading);
  const updating = useAppSelector(state => state.phieuNhapXuat.updating);
  const updateSuccess = useAppSelector(state => state.phieuNhapXuat.updateSuccess);
  const voucherTypeValues = Object.keys(VoucherType);

  const handleClose = () => {
    navigate(`/phieu-nhap-xuat${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getKhachHangs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...phieuNhapXuatEntity,
      ...values,
      khachHang: khachHangs.find(it => it.id.toString() === values.khachHang?.toString()),
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
          loaiPhieu: 'Nhap',
          ...phieuNhapXuatEntity,
          createdAt: convertDateTimeFromServer(phieuNhapXuatEntity.createdAt),
          updatedAt: convertDateTimeFromServer(phieuNhapXuatEntity.updatedAt),
          khachHang: phieuNhapXuatEntity?.khachHang?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="warehouseMgmApp.phieuNhapXuat.home.createOrEditLabel" data-cy="PhieuNhapXuatCreateUpdateHeading">
            <Translate contentKey="warehouseMgmApp.phieuNhapXuat.home.createOrEditLabel">Create or edit a PhieuNhapXuat</Translate>
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
                label={translate('warehouseMgmApp.phieuNhapXuat.maPhieu')}
                id="phieu-nhap-xuat-maPhieu"
                name="maPhieu"
                data-cy="maPhieu"
                type="text"
                validate={{
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.phieuNhapXuat.ngayLapPhieu')}
                id="phieu-nhap-xuat-ngayLapPhieu"
                name="ngayLapPhieu"
                data-cy="ngayLapPhieu"
                type="date"
              />
              <ValidatedField
                label={translate('warehouseMgmApp.phieuNhapXuat.loaiPhieu')}
                id="phieu-nhap-xuat-loaiPhieu"
                name="loaiPhieu"
                data-cy="loaiPhieu"
                type="select"
              >
                {voucherTypeValues.map(voucherType => (
                  <option value={voucherType} key={voucherType}>
                    {translate(`warehouseMgmApp.VoucherType.${voucherType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="phieu-nhap-xuat-khachHang"
                name="khachHang"
                data-cy="khachHang"
                label={translate('warehouseMgmApp.phieuNhapXuat.khachHang')}
                type="select"
              >
                <option value="" key="0" />
                {khachHangs
                  ? khachHangs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.tenKH}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/phieu-nhap-xuat" replace color="info">
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

export default PhieuNhapXuatUpdate;
