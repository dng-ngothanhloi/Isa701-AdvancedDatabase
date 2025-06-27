import dayjs from 'dayjs';
import { EmpSex } from 'app/shared/model/enumerations/emp-sex.model';

export interface IKhachHang {
  id?: string;
  maKH?: string | null;
  tenKH?: string;
  goiTinh?: keyof typeof EmpSex | null;
  dateOfBirth?: dayjs.Dayjs | null;
  diaChi?: string | null;
  createdAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  isDeleted?: boolean | null;
}

export const defaultValue: Readonly<IKhachHang> = {
  isDeleted: false,
};
