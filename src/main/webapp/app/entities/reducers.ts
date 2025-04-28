import masterdata from 'app/entities/masterdata/masterdata.reducer';
import khachHang from 'app/entities/khach-hang/khach-hang.reducer';
import danhMucHang from 'app/entities/danh-muc-hang/danh-muc-hang.reducer';
import phieuNhapXuat from 'app/entities/phieu-nhap-xuat/phieu-nhap-xuat.reducer';
import chiTietNhapXuat from 'app/entities/chi-tiet-nhap-xuat/chi-tiet-nhap-xuat.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
    masterdata,
    khachHang,
    danhMucHang,
    phieuNhapXuat,
    chiTietNhapXuat,
    /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
