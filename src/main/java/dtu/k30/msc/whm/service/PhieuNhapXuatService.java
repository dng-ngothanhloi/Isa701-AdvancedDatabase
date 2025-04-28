package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.repository.PhieuNhapXuatRepository;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import dtu.k30.msc.whm.service.mapper.PhieuNhapXuatMapper;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link dtu.k30.msc.whm.domain.PhieuNhapXuat}.
 */
@Service
public class PhieuNhapXuatService {

    private static final Logger LOG = LoggerFactory.getLogger(PhieuNhapXuatService.class);

    private final PhieuNhapXuatRepository phieuNhapXuatRepository;

    private final PhieuNhapXuatMapper phieuNhapXuatMapper;

    public PhieuNhapXuatService(PhieuNhapXuatRepository phieuNhapXuatRepository, PhieuNhapXuatMapper phieuNhapXuatMapper) {
        this.phieuNhapXuatRepository = phieuNhapXuatRepository;
        this.phieuNhapXuatMapper = phieuNhapXuatMapper;
    }

    /**
     * Save a phieuNhapXuat.
     *
     * @param phieuNhapXuatDTO the entity to save.
     * @return the persisted entity.
     */
    public PhieuNhapXuatDTO save(PhieuNhapXuatDTO phieuNhapXuatDTO) {
        LOG.debug("Request to save PhieuNhapXuat : {}", phieuNhapXuatDTO);
        PhieuNhapXuat phieuNhapXuat = phieuNhapXuatMapper.toEntity(phieuNhapXuatDTO);
        phieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);
        return phieuNhapXuatMapper.toDto(phieuNhapXuat);
    }

    /**
     * Update a phieuNhapXuat.
     *
     * @param phieuNhapXuatDTO the entity to save.
     * @return the persisted entity.
     */
    public PhieuNhapXuatDTO update(PhieuNhapXuatDTO phieuNhapXuatDTO) {
        LOG.debug("Request to update PhieuNhapXuat : {}", phieuNhapXuatDTO);
        PhieuNhapXuat phieuNhapXuat = phieuNhapXuatMapper.toEntity(phieuNhapXuatDTO);
        phieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);
        return phieuNhapXuatMapper.toDto(phieuNhapXuat);
    }

    /**
     * Partially update a phieuNhapXuat.
     *
     * @param phieuNhapXuatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PhieuNhapXuatDTO> partialUpdate(PhieuNhapXuatDTO phieuNhapXuatDTO) {
        LOG.debug("Request to partially update PhieuNhapXuat : {}", phieuNhapXuatDTO);

        return phieuNhapXuatRepository
                .findById(phieuNhapXuatDTO.getId())
                .map(existingPhieuNhapXuat -> {
                    phieuNhapXuatMapper.partialUpdate(existingPhieuNhapXuat, phieuNhapXuatDTO);

                    return existingPhieuNhapXuat;
                })
                .map(phieuNhapXuatRepository::save)
                .map(phieuNhapXuatMapper::toDto);
    }

    /**
     * Get all the phieuNhapXuats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<PhieuNhapXuatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PhieuNhapXuats");
        return phieuNhapXuatRepository.findAll(pageable).map(phieuNhapXuatMapper::toDto);
    }

    /**
     * Get one phieuNhapXuat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PhieuNhapXuatDTO> findOne(String id) {
        LOG.debug("Request to get PhieuNhapXuat : {}", id);
        return phieuNhapXuatRepository.findById(id).map(phieuNhapXuatMapper::toDto);
    }

    /**
     * Delete the phieuNhapXuat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete PhieuNhapXuat : {}", id);
        phieuNhapXuatRepository.deleteById(id);
    }
}
