import dayjs from 'dayjs';

export interface IDanhMucHang {
  id?: string;
  maHang?: string | null;
  tenHang?: string;
  donVitinh?: string | null;
  noiSanXuat?: string;
  ngaySanXuat?: dayjs.Dayjs | null;
  hanSuDung?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  isDeleted?: boolean | null;
}

export const defaultValue: Readonly<IDanhMucHang> = {
  isDeleted: false,
};
