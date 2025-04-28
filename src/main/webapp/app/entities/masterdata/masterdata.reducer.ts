import axios from 'axios';
import {createAsyncThunk, isFulfilled, isPending} from '@reduxjs/toolkit';
import {cleanEntity} from 'app/shared/util/entity-utils';
import {EntityState, IQueryParams, createEntitySlice, serializeAxiosError} from 'app/shared/reducers/reducer.utils';
import {IMasterdata, defaultValue} from 'app/shared/model/masterdata.model';

const initialState: EntityState<IMasterdata> = {
    loading: false,
    errorMessage: null,
    entities: [],
    entity: defaultValue,
    updating: false,
    totalItems: 0,
    updateSuccess: false,
};

const apiUrl = 'api/masterdata';

// Actions

export const getEntities = createAsyncThunk(
    'masterdata/fetch_entity_list',
    async ({page, size, sort}: IQueryParams) => {
        const requestUrl = `${apiUrl}?${sort ? `page=${page}&size=${size}&sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
        return axios.get<IMasterdata[]>(requestUrl);
    },
    {serializeError: serializeAxiosError},
);

export const getEntity = createAsyncThunk(
    'masterdata/fetch_entity',
    async (id: string | number) => {
        const requestUrl = `${apiUrl}/${id}`;
        return axios.get<IMasterdata>(requestUrl);
    },
    {serializeError: serializeAxiosError},
);

export const createEntity = createAsyncThunk(
    'masterdata/create_entity',
    async (entity: IMasterdata, thunkAPI) => {
        const result = await axios.post<IMasterdata>(apiUrl, cleanEntity(entity));
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

export const updateEntity = createAsyncThunk(
    'masterdata/update_entity',
    async (entity: IMasterdata, thunkAPI) => {
        const result = await axios.put<IMasterdata>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

export const partialUpdateEntity = createAsyncThunk(
    'masterdata/partial_update_entity',
    async (entity: IMasterdata, thunkAPI) => {
        const result = await axios.patch<IMasterdata>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

export const deleteEntity = createAsyncThunk(
    'masterdata/delete_entity',
    async (id: string | number, thunkAPI) => {
        const requestUrl = `${apiUrl}/${id}`;
        const result = await axios.delete<IMasterdata>(requestUrl);
        thunkAPI.dispatch(getEntities({}));
        return result;
    },
    {serializeError: serializeAxiosError},
);

// slice

export const MasterdataSlice = createEntitySlice({
    name: 'masterdata',
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

export const {reset} = MasterdataSlice.actions;

// Reducer
export default MasterdataSlice.reducer;
