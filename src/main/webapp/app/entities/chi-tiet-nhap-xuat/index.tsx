import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ChiTietNhapXuat from './chi-tiet-nhap-xuat';
import ChiTietNhapXuatDetail from './chi-tiet-nhap-xuat-detail';
import ChiTietNhapXuatUpdate from './chi-tiet-nhap-xuat-update';
import ChiTietNhapXuatDeleteDialog from './chi-tiet-nhap-xuat-delete-dialog';

const ChiTietNhapXuatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ChiTietNhapXuat />} />
    <Route path="new" element={<ChiTietNhapXuatUpdate />} />
    <Route path=":id">
      <Route index element={<ChiTietNhapXuatDetail />} />
      <Route path="edit" element={<ChiTietNhapXuatUpdate />} />
      <Route path="delete" element={<ChiTietNhapXuatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ChiTietNhapXuatRoutes;
