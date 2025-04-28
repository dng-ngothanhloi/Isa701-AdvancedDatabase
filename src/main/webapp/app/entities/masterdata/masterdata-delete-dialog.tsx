import React, {useEffect, useState} from 'react';
import {useLocation, useNavigate, useParams} from 'react-router-dom';
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap';
import {Translate} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

import {useAppDispatch, useAppSelector} from 'app/config/store';
import {deleteEntity, getEntity} from './masterdata.reducer';

export const MasterdataDeleteDialog = () => {
    const dispatch = useAppDispatch();

    const pageLocation = useLocation();
    const navigate = useNavigate();
    const {id} = useParams<'id'>();

    const [loadModal, setLoadModal] = useState(false);

    useEffect(() => {
        dispatch(getEntity(id));
        setLoadModal(true);
    }, []);

    const masterdataEntity = useAppSelector(state => state.masterdata.entity);
    const updateSuccess = useAppSelector(state => state.masterdata.updateSuccess);

    const handleClose = () => {
        navigate(`/masterdata${pageLocation.search}`);
    };

    useEffect(() => {
        if (updateSuccess && loadModal) {
            handleClose();
            setLoadModal(false);
        }
    }, [updateSuccess]);

    const confirmDelete = () => {
        dispatch(deleteEntity(masterdataEntity.id));
    };

    return (
        <Modal isOpen toggle={handleClose}>
            <ModalHeader toggle={handleClose} data-cy="masterdataDeleteDialogHeading">
                <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
            </ModalHeader>
            <ModalBody id="warehousMmgmtApp.masterdata.delete.question">
                <Translate contentKey="warehousMmgmtApp.masterdata.delete.question"
                           interpolate={{id: masterdataEntity.id}}>
                    Are you sure you want to delete this Masterdata?
                </Translate>
            </ModalBody>
            <ModalFooter>
                <Button color="secondary" onClick={handleClose}>
                    <FontAwesomeIcon icon="ban"/>
                    &nbsp;
                    <Translate contentKey="entity.action.cancel">Cancel</Translate>
                </Button>
                <Button id="jhi-confirm-delete-masterdata" data-cy="entityConfirmDeleteButton" color="danger"
                        onClick={confirmDelete}>
                    <FontAwesomeIcon icon="trash"/>
                    &nbsp;
                    <Translate contentKey="entity.action.delete">Delete</Translate>
                </Button>
            </ModalFooter>
        </Modal>
    );
};

export default MasterdataDeleteDialog;
