import React from 'react';
import {Route} from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Masterdata from './masterdata';
import KhachHang from './khach-hang';
import DanhMucHang from './danh-muc-hang';
import PhieuNhapXuat from './phieu-nhap-xuat';
import ChiTietNhapXuat from './chi-tiet-nhap-xuat';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
    return (
        <div>
            <ErrorBoundaryRoutes>
                {/* prettier-ignore */}
                <Route path="masterdata/*" element={<Masterdata/>}/>
                <Route path="khach-hang/*" element={<KhachHang/>}/>
                <Route path="danh-muc-hang/*" element={<DanhMucHang/>}/>
                <Route path="phieu-nhap-xuat/*" element={<PhieuNhapXuat/>}/>
                <Route path="chi-tiet-nhap-xuat/*" element={<ChiTietNhapXuat/>}/>
                {/* jhipster-needle-add-route-path - JHipster will add routes here */}
            </ErrorBoundaryRoutes>
        </div>
    );
};
