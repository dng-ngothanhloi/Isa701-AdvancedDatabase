import React, {useEffect, useState} from 'react';
import {useLocation, useNavigate, useParams} from 'react-router-dom';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {useAppDispatch, useAppSelector} from 'app/config/store';
import {deleteEntity, getEntity} from './danh-muc-hang.reducer';

export const DanhMucHangDeleteDialog = () => {
    const dispatch = useAppDispatch();

    const pageLocation = useLocation();
    const navigate = useNavigate();
    const {id} = useParams<'id'>();

    const [loadModal, setLoadModal] = useState(false);

    useEffect(() => {
        dispatch(getEntity(id));
        setLoadModal(true);
    }, []);

    const danhMucHangEntity = useAppSelector(state => state.danhMucHang.entity);
    const updateSuccess = useAppSelector(state => state.danhMucHang.updateSuccess);

    const handleClose = () => {
        navigate(`/danh-muc-hang${pageLocation.search}`);
    };

    useEffect(() => {
        if (updateSuccess && loadModal) {
            handleClose();
            setLoadModal(false);
        }
    }, [updateSuccess]);

    const confirmDelete = () => {
        dispatch(deleteEntity(danhMucHangEntity.id));
    };

    return (
        <Modal isOpen toggle={handleClose}>
            <ModalHeader toggle={handleClose} data-cy="danhMucHangDeleteDialogHeading">
                <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
            </ModalHeader>
            <ModalBody id="warehousMmgmtApp.danhMucHang.delete.question">
                <Translate contentKey="warehousMmgmtApp.danhMucHang.delete.question"
                           interpolate={{id: danhMucHangEntity.id}}>
                    Are you sure you want to delete this DanhMucHang?
                </Translate>
            </ModalBody>
            <ModalFooter>
                <Button color="secondary" onClick={handleClose}>
                    <FontAwesomeIcon icon="ban"/>
                    &nbsp;
                    <Translate contentKey="entity.action.cancel">Cancel</Translate>
                </Button>
                <Button id="jhi-confirm-delete-danhMucHang" data-cy="entityConfirmDeleteButton" color="danger"
                        onClick={confirmDelete}>
                    <FontAwesomeIcon icon="trash"/>
                    &nbsp;
                    <Translate contentKey="entity.action.delete">Delete</Translate>
                </Button>
            </ModalFooter>
        </Modal>
    );
};

export default DanhMucHangDeleteDialog;
