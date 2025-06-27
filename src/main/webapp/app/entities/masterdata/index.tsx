import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Masterdata from './masterdata';
import MasterdataDetail from './masterdata-detail';
import MasterdataUpdate from './masterdata-update';
import MasterdataDeleteDialog from './masterdata-delete-dialog';

const MasterdataRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Masterdata />} />
    <Route path="new" element={<MasterdataUpdate />} />
    <Route path=":id">
      <Route index element={<MasterdataDetail />} />
      <Route path="edit" element={<MasterdataUpdate />} />
      <Route path="delete" element={<MasterdataDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MasterdataRoutes;
