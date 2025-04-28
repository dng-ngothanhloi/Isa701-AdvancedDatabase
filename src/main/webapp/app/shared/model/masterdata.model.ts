import dayjs from 'dayjs';

export interface IMasterdata {
    id?: string;
    category?: string;
    dataKey?: string;
    dataValue?: string;
    isDeleted?: boolean | null;
    createdAt?: dayjs.Dayjs | null;
    createdBy?: string | null;
    updatedAt?: dayjs.Dayjs | null;
    updatedBy?: string | null;
}

export const defaultValue: Readonly<IMasterdata> = {
    isDeleted: false,
};
