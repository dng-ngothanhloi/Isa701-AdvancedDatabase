import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './masterdata.reducer';

export const MasterdataDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const masterdataEntity = useAppSelector(state => state.masterdata.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="masterdataDetailsHeading">
          <Translate contentKey="warehouseMgmApp.masterdata.detail.title">Masterdata</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{masterdataEntity.id}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="warehouseMgmApp.masterdata.category">Category</Translate>
            </span>
          </dt>
          <dd>{masterdataEntity.category}</dd>
          <dt>
            <span id="dataKey">
              <Translate contentKey="warehouseMgmApp.masterdata.dataKey">Data Key</Translate>
            </span>
          </dt>
          <dd>{masterdataEntity.dataKey}</dd>
          <dt>
            <span id="dataValue">
              <Translate contentKey="warehouseMgmApp.masterdata.dataValue">Data Value</Translate>
            </span>
          </dt>
          <dd>{masterdataEntity.dataValue}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="warehouseMgmApp.masterdata.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{masterdataEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="warehouseMgmApp.masterdata.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {masterdataEntity.createdAt ? <TextFormat value={masterdataEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="warehouseMgmApp.masterdata.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{masterdataEntity.createdBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="warehouseMgmApp.masterdata.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {masterdataEntity.updatedAt ? <TextFormat value={masterdataEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="warehouseMgmApp.masterdata.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{masterdataEntity.updatedBy}</dd>
        </dl>
        <Button tag={Link} to="/masterdata" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/masterdata/${masterdataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MasterdataDetail;
