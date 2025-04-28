import React from 'react';
import {Route} from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PhieuNhapXuat from './phieu-nhap-xuat';
import PhieuNhapXuatDetail from './phieu-nhap-xuat-detail';
import PhieuNhapXuatUpdate from './phieu-nhap-xuat-update';
import PhieuNhapXuatDeleteDialog from './phieu-nhap-xuat-delete-dialog';

const PhieuNhapXuatRoutes = () => (
    <ErrorBoundaryRoutes>
        <Route index element={<PhieuNhapXuat/>}/>
        <Route path="new" element={<PhieuNhapXuatUpdate/>}/>
        <Route path=":id">
            <Route index element={<PhieuNhapXuatDetail/>}/>
            <Route path="edit" element={<PhieuNhapXuatUpdate/>}/>
            <Route path="delete" element={<PhieuNhapXuatDeleteDialog/>}/>
        </Route>
    </ErrorBoundaryRoutes>
);

export default PhieuNhapXuatRoutes;
