package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.repository.ChiTietNhapXuatRepository;
import dtu.k30.msc.whm.service.dto.ChiTietNhapXuatDTO;
import dtu.k30.msc.whm.service.mapper.ChiTietNhapXuatMapper;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link dtu.k30.msc.whm.domain.ChiTietNhapXuat}.
 */
@Service
public class ChiTietNhapXuatService {

    private static final Logger LOG = LoggerFactory.getLogger(ChiTietNhapXuatService.class);

    private final ChiTietNhapXuatRepository chiTietNhapXuatRepository;

    private final ChiTietNhapXuatMapper chiTietNhapXuatMapper;

    public ChiTietNhapXuatService(ChiTietNhapXuatRepository chiTietNhapXuatRepository, ChiTietNhapXuatMapper chiTietNhapXuatMapper) {
        this.chiTietNhapXuatRepository = chiTietNhapXuatRepository;
        this.chiTietNhapXuatMapper = chiTietNhapXuatMapper;
    }

    /**
     * Save a chiTietNhapXuat.
     *
     * @param chiTietNhapXuatDTO the entity to save.
     * @return the persisted entity.
     */
    public ChiTietNhapXuatDTO save(ChiTietNhapXuatDTO chiTietNhapXuatDTO) {
        LOG.debug("Request to save ChiTietNhapXuat : {}", chiTietNhapXuatDTO);
        ChiTietNhapXuat chiTietNhapXuat = chiTietNhapXuatMapper.toEntity(chiTietNhapXuatDTO);
        chiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);
        return chiTietNhapXuatMapper.toDto(chiTietNhapXuat);
    }

    /**
     * Update a chiTietNhapXuat.
     *
     * @param chiTietNhapXuatDTO the entity to save.
     * @return the persisted entity.
     */
    public ChiTietNhapXuatDTO update(ChiTietNhapXuatDTO chiTietNhapXuatDTO) {
        LOG.debug("Request to update ChiTietNhapXuat : {}", chiTietNhapXuatDTO);
        ChiTietNhapXuat chiTietNhapXuat = chiTietNhapXuatMapper.toEntity(chiTietNhapXuatDTO);
        chiTietNhapXuat = chiTietNhapXuatRepository.save(chiTietNhapXuat);
        return chiTietNhapXuatMapper.toDto(chiTietNhapXuat);
    }

    /**
     * Partially update a chiTietNhapXuat.
     *
     * @param chiTietNhapXuatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChiTietNhapXuatDTO> partialUpdate(ChiTietNhapXuatDTO chiTietNhapXuatDTO) {
        LOG.debug("Request to partially update ChiTietNhapXuat : {}", chiTietNhapXuatDTO);

        return chiTietNhapXuatRepository
                .findById(chiTietNhapXuatDTO.getId())
                .map(existingChiTietNhapXuat -> {
                    chiTietNhapXuatMapper.partialUpdate(existingChiTietNhapXuat, chiTietNhapXuatDTO);

                    return existingChiTietNhapXuat;
                })
                .map(chiTietNhapXuatRepository::save)
                .map(chiTietNhapXuatMapper::toDto);
    }

    /**
     * Get all the chiTietNhapXuats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<ChiTietNhapXuatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ChiTietNhapXuats");
        return chiTietNhapXuatRepository.findAll(pageable).map(chiTietNhapXuatMapper::toDto);
    }

    /**
     * Get one chiTietNhapXuat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ChiTietNhapXuatDTO> findOne(String id) {
        LOG.debug("Request to get ChiTietNhapXuat : {}", id);
        return chiTietNhapXuatRepository.findById(id).map(chiTietNhapXuatMapper::toDto);
    }

    /**
     * Delete the chiTietNhapXuat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete ChiTietNhapXuat : {}", id);
        chiTietNhapXuatRepository.deleteById(id);
    }
}
