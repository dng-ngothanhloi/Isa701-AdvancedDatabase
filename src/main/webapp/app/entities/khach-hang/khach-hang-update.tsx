import React, {useEffect} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {Translate, ValidatedField, ValidatedForm, translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime} from 'app/shared/util/date-utils';
import {useAppDispatch, useAppSelector} from 'app/config/store';

import {EmpSex} from 'app/shared/model/enumerations/emp-sex.model';
import {createEntity, getEntity, reset, updateEntity} from './khach-hang.reducer';

export const KhachHangUpdate = () => {
    const dispatch = useAppDispatch();

    const navigate = useNavigate();

    const {id} = useParams<'id'>();
    const isNew = id === undefined;

    const khachHangEntity = useAppSelector(state => state.khachHang.entity);
    const loading = useAppSelector(state => state.khachHang.loading);
    const updating = useAppSelector(state => state.khachHang.updating);
    const updateSuccess = useAppSelector(state => state.khachHang.updateSuccess);
    const empSexValues = Object.keys(EmpSex);

    const handleClose = () => {
        navigate(`/khach-hang${location.search}`);
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
            ...khachHangEntity,
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
                goiTinh: 'Nam',
                ...khachHangEntity,
                createdAt: convertDateTimeFromServer(khachHangEntity.createdAt),
                updatedAt: convertDateTimeFromServer(khachHangEntity.updatedAt),
            };

    return (
        <div>
            <Row className="justify-content-center">
                <Col md="8">
                    <h2 id="warehousMmgmtApp.khachHang.home.createOrEditLabel" data-cy="KhachHangCreateUpdateHeading">
                        <Translate contentKey="warehousMmgmtApp.khachHang.home.createOrEditLabel">Create or edit a
                            KhachHang</Translate>
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
                                    id="khach-hang-id"
                                    label={translate('global.field.id')}
                                    validate={{required: true}}
                                />
                            ) : null}
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.maKH')}
                                id="khach-hang-maKH"
                                name="maKH"
                                data-cy="maKH"
                                type="text"
                                validate={{
                                    maxLength: {
                                        value: 10,
                                        message: translate('entity.validation.maxlength', {max: 10})
                                    },
                                }}
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.tenKH')}
                                id="khach-hang-tenKH"
                                name="tenKH"
                                data-cy="tenKH"
                                type="text"
                                validate={{
                                    required: {value: true, message: translate('entity.validation.required')},
                                    maxLength: {
                                        value: 50,
                                        message: translate('entity.validation.maxlength', {max: 50})
                                    },
                                }}
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.goiTinh')}
                                id="khach-hang-goiTinh"
                                name="goiTinh"
                                data-cy="goiTinh"
                                type="select"
                            >
                                {empSexValues.map(empSex => (
                                    <option value={empSex} key={empSex}>
                                        {translate(`warehousMmgmtApp.EmpSex.${empSex}`)}
                                    </option>
                                ))}
                            </ValidatedField>
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.dateOfBirth')}
                                id="khach-hang-dateOfBirth"
                                name="dateOfBirth"
                                data-cy="dateOfBirth"
                                type="date"
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.diaChi')}
                                id="khach-hang-diaChi"
                                name="diaChi"
                                data-cy="diaChi"
                                type="text"
                                validate={{
                                    maxLength: {
                                        value: 500,
                                        message: translate('entity.validation.maxlength', {max: 500})
                                    },
                                }}
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.createdAt')}
                                id="khach-hang-createdAt"
                                name="createdAt"
                                data-cy="createdAt"
                                type="datetime-local"
                                placeholder="YYYY-MM-DD HH:mm"
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.createdBy')}
                                id="khach-hang-createdBy"
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
                                label={translate('warehousMmgmtApp.khachHang.updatedAt')}
                                id="khach-hang-updatedAt"
                                name="updatedAt"
                                data-cy="updatedAt"
                                type="datetime-local"
                                placeholder="YYYY-MM-DD HH:mm"
                            />
                            <ValidatedField
                                label={translate('warehousMmgmtApp.khachHang.updatedBy')}
                                id="khach-hang-updatedBy"
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
                                label={translate('warehousMmgmtApp.khachHang.isDeleted')}
                                id="khach-hang-isDeleted"
                                name="isDeleted"
                                data-cy="isDeleted"
                                check
                                type="checkbox"
                            />
                            <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/khach-hang"
                                    replace color="info">
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

export default KhachHangUpdate;
