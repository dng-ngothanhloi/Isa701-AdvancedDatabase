import React, {useEffect} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {Translate, ValidatedField, ValidatedForm, isNumber, translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime} from 'app/shared/util/date-utils';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {getEntities as getPhieuNhapXuats} from 'app/entities/phieu-nhap-xuat/phieu-nhap-xuat.reducer';
import {getEntities as getDanhMucHangs} from 'app/entities/danh-muc-hang/danh-muc-hang.reducer';
import {createEntity, getEntity, reset, updateEntity} from './chi-tiet-nhap-xuat.reducer';

export const ChiTietNhapXuatUpdate = () => {
    const dispatch = useAppDispatch();

    const navigate = useNavigate();

    const {id} = useParams<'id'>();
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
                    <h2 id="warehousMmgmtApp.chiTietNhapXuat.home.createOrEditLabel"
                        data-cy="ChiTietNhapXuatCreateUpdateHeading">
                        <Translate contentKey="warehousMmgmtApp.chiTietNhapXuat.home.createOrEditLabel">Create or edit a
                            ChiTietNhapXuat</Translate>
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
                                    id="chi-tiet-nhap-xuat-id"
                                    label={translate('global.field.id')}
                                    validate={{required: true}}
                                />
                            ) : null}
                            <ValidatedField
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.soLuong')}
                                id="chi-tiet-nhap-xuat-soLuong"
                                name="soLuong"
                                data-cy="soLuong"
                                type="text"
                                validate={{
                                    required: {value: true, message: translate('entity.validation.required')},
                                    validate: v => isNumber(v) || translate('entity.validation.number'),
                                }}
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.donGia')}
                                id="chi-tiet-nhap-xuat-donGia"
                                name="donGia"
                                data-cy="donGia"
                                type="text"
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.createdAt')}
                                id="chi-tiet-nhap-xuat-createdAt"
                                name="createdAt"
                                data-cy="createdAt"
                                type="datetime-local"
                                placeholder="YYYY-MM-DD HH:mm"
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.createdBy')}
                                id="chi-tiet-nhap-xuat-createdBy"
                                name="createdBy"
                                data-cy="createdBy"
                                type="text"
                                validate={{
                                    maxLength: {
                                        value: 50,
                                        message: translate('entity.validation.maxlength', {max: 50})
                                    },
                                }}
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.updatedAt')}
                                id="chi-tiet-nhap-xuat-updatedAt"
                                name="updatedAt"
                                data-cy="updatedAt"
                                type="datetime-local"
                                placeholder="YYYY-MM-DD HH:mm"
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.updatedBy')}
                                id="chi-tiet-nhap-xuat-updatedBy"
                                name="updatedBy"
                                data-cy="updatedBy"
                                type="text"
                                validate={{
                                    maxLength: {
                                        value: 50,
                                        message: translate('entity.validation.maxlength', {max: 50})
                                    },
                                }}
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.isDeleted')}
                                id="chi-tiet-nhap-xuat-isDeleted"
                                name="isDeleted"
                                data-cy="isDeleted"
                                check
                                type="checkbox"
                            />
                            <ValidatedField
                                id="chi-tiet-nhap-xuat-phieuNhapXuat"
                                name="phieuNhapXuat"
                                data-cy="phieuNhapXuat"
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.phieuNhapXuat')}
                                type="select"
                            >
                                <option value="" key="0"/>
                                {phieuNhapXuats
                                    ? phieuNhapXuats.map(otherEntity => (
                                        <option value={otherEntity.id} key={otherEntity.id}>
                                            {otherEntity.id}
                                        </option>
                                    ))
                                    : null}
                            </ValidatedField>
                            <ValidatedField
                                id="chi-tiet-nhap-xuat-maHang"
                                name="maHang"
                                data-cy="maHang"
                                label={translate('warehousMmgmtApp.chiTietNhapXuat.maHang')}
                                type="select"
                            >
                                <option value="" key="0"/>
                                {danhMucHangs
                                    ? danhMucHangs.map(otherEntity => (
                                        <option value={otherEntity.id} key={otherEntity.id}>
                                            {otherEntity.id}
                                        </option>
                                    ))
                                    : null}
                            </ValidatedField>
                            <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton"
                                    to="/chi-tiet-nhap-xuat" replace color="info">
                                <FontAwesomeIcon icon="arrow-left"/>
                                &nbsp;
                                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
                            </Button>
                            &nbsp;
                            <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit"
                                    disabled={updating}>
                                <FontAwesomeIcon icon="save"/>
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
