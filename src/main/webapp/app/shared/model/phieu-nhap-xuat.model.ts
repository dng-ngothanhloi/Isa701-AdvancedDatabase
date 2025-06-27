import dayjs from 'dayjs';
import { IKhachHang } from 'app/shared/model/khach-hang.model';
import { VoucherType } from 'app/shared/model/enumerations/voucher-type.model';

export interface IPhieuNhapXuat {
  id?: string;
  maPhieu?: string | null;
  ngayLapPhieu?: dayjs.Dayjs | null;
  loaiPhieu?: keyof typeof VoucherType | null;
  createdAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  isDeleted?: boolean | null;
  tenKhachHang?: string | null;
  khachHang?: IKhachHang | null;
}

export const defaultValue: Readonly<IPhieuNhapXuat> = {
  isDeleted: false,
};
