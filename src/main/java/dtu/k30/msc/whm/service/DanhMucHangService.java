package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.repository.DanhMucHangRepository;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
import dtu.k30.msc.whm.service.mapper.DanhMucHangMapper;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link dtu.k30.msc.whm.domain.DanhMucHang}.
 */
@Service
public class DanhMucHangService {

    private static final Logger LOG = LoggerFactory.getLogger(DanhMucHangService.class);

    private final DanhMucHangRepository danhMucHangRepository;

    private final DanhMucHangMapper danhMucHangMapper;

    public DanhMucHangService(DanhMucHangRepository danhMucHangRepository, DanhMucHangMapper danhMucHangMapper) {
        this.danhMucHangRepository = danhMucHangRepository;
        this.danhMucHangMapper = danhMucHangMapper;
    }

    /**
     * Save a danhMucHang.
     *
     * @param danhMucHangDTO the entity to save.
     * @return the persisted entity.
     */
    public DanhMucHangDTO save(DanhMucHangDTO danhMucHangDTO) {
        LOG.debug("Request to save DanhMucHang : {}", danhMucHangDTO);
        DanhMucHang danhMucHang = danhMucHangMapper.toEntity(danhMucHangDTO);
        danhMucHang = danhMucHangRepository.save(danhMucHang);
        return danhMucHangMapper.toDto(danhMucHang);
    }

    /**
     * Update a danhMucHang.
     *
     * @param danhMucHangDTO the entity to save.
     * @return the persisted entity.
     */
    public DanhMucHangDTO update(DanhMucHangDTO danhMucHangDTO) {
        LOG.debug("Request to update DanhMucHang : {}", danhMucHangDTO);
        DanhMucHang danhMucHang = danhMucHangMapper.toEntity(danhMucHangDTO);
        danhMucHang = danhMucHangRepository.save(danhMucHang);
        return danhMucHangMapper.toDto(danhMucHang);
    }

    /**
     * Partially update a danhMucHang.
     *
     * @param danhMucHangDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DanhMucHangDTO> partialUpdate(DanhMucHangDTO danhMucHangDTO) {
        LOG.debug("Request to partially update DanhMucHang : {}", danhMucHangDTO);

        return danhMucHangRepository
                .findById(danhMucHangDTO.getId())
                .map(existingDanhMucHang -> {
                    danhMucHangMapper.partialUpdate(existingDanhMucHang, danhMucHangDTO);

                    return existingDanhMucHang;
                })
                .map(danhMucHangRepository::save)
                .map(danhMucHangMapper::toDto);
    }

    /**
     * Get all the danhMucHangs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<DanhMucHangDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DanhMucHangs");
        return danhMucHangRepository.findAll(pageable).map(danhMucHangMapper::toDto);
    }

    /**
     * Get one danhMucHang by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DanhMucHangDTO> findOne(String id) {
        LOG.debug("Request to get DanhMucHang : {}", id);
        return danhMucHangRepository.findById(id).map(danhMucHangMapper::toDto);
    }

    /**
     * Delete the danhMucHang by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete DanhMucHang : {}", id);
        danhMucHangRepository.deleteById(id);
    }
}
