import axios from 'axios';
import {createAsyncThunk, isFulfilled, isPending} from '@reduxjs/toolkit';
import {cleanEntity} from 'app/shared/util/entity-utils';
import {EntityState, IQueryParams, createEntitySlice, serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {IChiTietNhapXuat, defaultValue} from 'app/shared/model/chi-tiet-nhap-xuat.model';

const initialState: EntityState<IChiTietNhapXuat> = {
    loading: false,
    errorMessage: null,
    entities: [],
    entity: defaultValue,
    updating: false,
    totalItems: 0,
    updateSuccess: false,
};

const apiUrl = 'api/chi-tiet-nhap-xuats';

// Actions

export const getEntities = createAsyncThunk(
    'chiTietNhapXuat/fetch_entity_list',
    async ({page, size, sort}: IQueryParams) => {
        const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
        return axios.get<IChiTietNhapXuat[]>(requestUrl);
    },
    {serializeError: serializeAxiosError},
);

export const getEntity = createAsyncThunk(
    'chiTietNhapXuat/fetch_entity',
    async (id: string | number) => {
        const requestUrl = `${apiUrl}/${id}`;
        return axios.get<IChiTietNhapXuat>(requestUrl);
    },
    {serializeError: serializeAxiosError},
);

export const createEntity = createAsyncThunk(
    'chiTietNhapXuat/create_entity',
    async (entity: IChiTietNhapXuat, thunkAPI) => {
        const result = await axios.post<IChiTietNhapXuat>(apiUrl, cleanEntity(entity));
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

export const updateEntity = createAsyncThunk(
    'chiTietNhapXuat/update_entity',
    async (entity: IChiTietNhapXuat, thunkAPI) => {
        const result = await axios.put<IChiTietNhapXuat>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

export const partialUpdateEntity = createAsyncThunk(
    'chiTietNhapXuat/partial_update_entity',
    async (entity: IChiTietNhapXuat, thunkAPI) => {
        const result = await axios.patch<IChiTietNhapXuat>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

export const deleteEntity = createAsyncThunk(
    'chiTietNhapXuat/delete_entity',
    async (id: string | number, thunkAPI) => {
        const requestUrl = `${apiUrl}/${id}`;
        const result = await axios.delete<IChiTietNhapXuat>(requestUrl);
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

// slice

export const ChiTietNhapXuatSlice = createEntitySlice({
    name: 'chiTietNhapXuat',
    initialState,
    extraReducers(builder) {
        builder
            .addCase(getEntity.fulfilled, (state, action) => {
                state.loading = false;
                state.entity = action.payload.data;
            })
            .addCase(deleteEntity.fulfilled, state => {
                state.updating = false;
                state.updateSuccess = true;
                state.entity = {};
            })
            .addMatcher(isFulfilled(getEntities), (state, action) => {
                const {data, headers} = action.payload;

                return {
                    ...state,
                    loading: false,
                    entities: data,
                    totalItems: parseInt(headers['x-total-count'], 10),
                };
            })
            .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
                state.updating = false;
                state.loading = false;
                state.updateSuccess = true;
                state.entity = action.payload.data;
            })
            .addMatcher(isPending(getEntities, getEntity), state => {
                state.errorMessage = null;
                state.updateSuccess = false;
                state.loading = true;
            })
            .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
                state.errorMessage = null;
                state.updateSuccess = false;
                state.updating = true;
            });
    },
});

export const {reset} = ChiTietNhapXuatSlice.actions;

// Reducer
export default ChiTietNhapXuatSlice.reducer;
