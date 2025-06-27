import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './danh-muc-hang.reducer';

export const DanhMucHangUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const danhMucHangEntity = useAppSelector(state => state.danhMucHang.entity);
  const loading = useAppSelector(state => state.danhMucHang.loading);
  const updating = useAppSelector(state => state.danhMucHang.updating);
  const updateSuccess = useAppSelector(state => state.danhMucHang.updateSuccess);

  const handleClose = () => {
    navigate(`/danh-muc-hang${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...danhMucHangEntity,
      ...values,
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
          ...danhMucHangEntity,
          createdAt: convertDateTimeFromServer(danhMucHangEntity.createdAt),
          updatedAt: convertDateTimeFromServer(danhMucHangEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="warehouseMgmApp.danhMucHang.home.createOrEditLabel" data-cy="DanhMucHangCreateUpdateHeading">
            <Translate contentKey="warehouseMgmApp.danhMucHang.home.createOrEditLabel">Create or edit a DanhMucHang</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="danh-muc-hang-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.maHang')}
                id="danh-muc-hang-maHang"
                name="maHang"
                data-cy="maHang"
                type="text"
                validate={{
                  maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.tenHang')}
                id="danh-muc-hang-tenHang"
                name="tenHang"
                data-cy="tenHang"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.donVitinh')}
                id="danh-muc-hang-donVitinh"
                name="donVitinh"
                data-cy="donVitinh"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.noiSanXuat')}
                id="danh-muc-hang-noiSanXuat"
                name="noiSanXuat"
                data-cy="noiSanXuat"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.ngaySanXuat')}
                id="danh-muc-hang-ngaySanXuat"
                name="ngaySanXuat"
                data-cy="ngaySanXuat"
                type="date"
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.hanSuDung')}
                id="danh-muc-hang-hanSuDung"
                name="hanSuDung"
                data-cy="hanSuDung"
                type="date"
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.createdAt')}
                id="danh-muc-hang-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.createdBy')}
                id="danh-muc-hang-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.updatedAt')}
                id="danh-muc-hang-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.updatedBy')}
                id="danh-muc-hang-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('warehouseMgmApp.danhMucHang.isDeleted')}
                id="danh-muc-hang-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/danh-muc-hang" replace color="info">
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

export default DanhMucHangUpdate;
