import React from 'react';
import {Route} from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import KhachHang from './khach-hang';
import KhachHangDetail from './khach-hang-detail';
import KhachHangUpdate from './khach-hang-update';
import KhachHangDeleteDialog from './khach-hang-delete-dialog';

const KhachHangRoutes = () => (
    <ErrorBoundaryRoutes>
        <Route index element={<KhachHang/>}/>
        <Route path="new" element={<KhachHangUpdate/>}/>
        <Route path=":id">
            <Route index element={<KhachHangDetail/>}/>
            <Route path="edit" element={<KhachHangUpdate/>}/>
            <Route path="delete" element={<KhachHangDeleteDialog/>}/>
        </Route>
    </ErrorBoundaryRoutes>
);

export default KhachHangRoutes;
