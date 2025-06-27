import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DanhMucHang from './danh-muc-hang';
import DanhMucHangDetail from './danh-muc-hang-detail';
import DanhMucHangUpdate from './danh-muc-hang-update';
import DanhMucHangDeleteDialog from './danh-muc-hang-delete-dialog';

const DanhMucHangRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DanhMucHang />} />
    <Route path="new" element={<DanhMucHangUpdate />} />
    <Route path=":id">
      <Route index element={<DanhMucHangDetail />} />
      <Route path="edit" element={<DanhMucHangUpdate />} />
      <Route path="delete" element={<DanhMucHangDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DanhMucHangRoutes;
