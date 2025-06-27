package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.Masterdata;
import dtu.k30.msc.whm.repository.MasterdataRepository;
import dtu.k30.msc.whm.service.dto.MasterdataDTO;
import dtu.k30.msc.whm.service.mapper.MasterdataMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link dtu.k30.msc.whm.domain.Masterdata}.
 */
@Service
public class MasterdataService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterdataService.class);

    private final MasterdataRepository masterdataRepository;

    private final MasterdataMapper masterdataMapper;

    public MasterdataService(MasterdataRepository masterdataRepository, MasterdataMapper masterdataMapper) {
        this.masterdataRepository = masterdataRepository;
        this.masterdataMapper = masterdataMapper;
    }

    /**
     * Save a masterdata.
     *
     * @param masterdataDTO the entity to save.
     * @return the persisted entity.
     */
    public MasterdataDTO save(MasterdataDTO masterdataDTO) {
        LOG.debug("Request to save Masterdata : {}", masterdataDTO);
        Masterdata masterdata = masterdataMapper.toEntity(masterdataDTO);
        masterdata = masterdataRepository.save(masterdata);
        return masterdataMapper.toDto(masterdata);
    }

    /**
     * Update a masterdata.
     *
     * @param masterdataDTO the entity to save.
     * @return the persisted entity.
     */
    public MasterdataDTO update(MasterdataDTO masterdataDTO) {
        LOG.debug("Request to update Masterdata : {}", masterdataDTO);
        Masterdata masterdata = masterdataMapper.toEntity(masterdataDTO);
        masterdata = masterdataRepository.save(masterdata);
        return masterdataMapper.toDto(masterdata);
    }

    /**
     * Partially update a masterdata.
     *
     * @param masterdataDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MasterdataDTO> partialUpdate(MasterdataDTO masterdataDTO) {
        LOG.debug("Request to partially update Masterdata : {}", masterdataDTO);

        return masterdataRepository
            .findById(masterdataDTO.getId())
            .map(existingMasterdata -> {
                masterdataMapper.partialUpdate(existingMasterdata, masterdataDTO);

                return existingMasterdata;
            })
            .map(masterdataRepository::save)
            .map(masterdataMapper::toDto);
    }

    /**
     * Get all the masterdata.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<MasterdataDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Masterdata");
        return masterdataRepository.findAll(pageable).map(masterdataMapper::toDto);
    }

    /**
     * Get one masterdata by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<MasterdataDTO> findOne(String id) {
        LOG.debug("Request to get Masterdata : {}", id);
        return masterdataRepository.findById(id).map(masterdataMapper::toDto);
    }

    /**
     * Delete the masterdata by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete Masterdata : {}", id);
        masterdataRepository.deleteById(id);
    }
}
