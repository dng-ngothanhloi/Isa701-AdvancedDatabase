import dayjs from 'dayjs';
import { IPhieuNhapXuat } from 'app/shared/model/phieu-nhap-xuat.model';
import { IDanhMucHang } from 'app/shared/model/danh-muc-hang.model';

export interface IChiTietNhapXuat {
  id?: string;
  soLuong?: number;
  donGia?: number | null;
  createdAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  isDeleted?: boolean | null;
  phieuNhapXuat?: IPhieuNhapXuat | null;
  maHang?: IDanhMucHang | null;
}

export const defaultValue: Readonly<IChiTietNhapXuat> = {
  isDeleted: false,
};
