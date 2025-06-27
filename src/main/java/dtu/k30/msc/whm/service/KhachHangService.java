package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.repository.KhachHangRepository;
import dtu.k30.msc.whm.service.dto.KhachHangDTO;
import dtu.k30.msc.whm.service.mapper.KhachHangMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link dtu.k30.msc.whm.domain.KhachHang}.
 */
@Service
public class KhachHangService {

    private static final Logger LOG = LoggerFactory.getLogger(KhachHangService.class);

    private final KhachHangRepository khachHangRepository;

    private final KhachHangMapper khachHangMapper;

    private final DataSynchronizationService dataSynchronizationService;

    public KhachHangService(KhachHangRepository khachHangRepository, KhachHangMapper khachHangMapper, DataSynchronizationService dataSynchronizationService) {
        this.khachHangRepository = khachHangRepository;
        this.khachHangMapper = khachHangMapper;
        this.dataSynchronizationService = dataSynchronizationService;
    }

    /**
     * Save a khachHang.
     *
     * @param khachHangDTO the entity to save.
     * @return the persisted entity.
     */
    public KhachHangDTO save(KhachHangDTO khachHangDTO) {
        LOG.debug("Request to save KhachHang : {}", khachHangDTO);
        KhachHang khachHang = khachHangMapper.toEntity(khachHangDTO);
        khachHang = khachHangRepository.save(khachHang);
        
        // Synchronize embedded documents
        dataSynchronizationService.updateEmbeddedKhachHang(khachHang);
        
        return khachHangMapper.toDto(khachHang);
    }

    /**
     * Update a khachHang.
     *
     * @param khachHangDTO the entity to save.
     * @return the persisted entity.
     */
    public KhachHangDTO update(KhachHangDTO khachHangDTO) {
        LOG.debug("Request to update KhachHang : {}", khachHangDTO);
        KhachHang khachHang = khachHangMapper.toEntity(khachHangDTO);
        khachHang = khachHangRepository.save(khachHang);
        
        // Synchronize embedded documents
        dataSynchronizationService.updateEmbeddedKhachHang(khachHang);
        
        return khachHangMapper.toDto(khachHang);
    }

    /**
     * Partially update a khachHang.
     *
     * @param khachHangDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KhachHangDTO> partialUpdate(KhachHangDTO khachHangDTO) {
        LOG.debug("Request to partially update KhachHang : {}", khachHangDTO);

        return khachHangRepository
            .findById(khachHangDTO.getId())
            .map(existingKhachHang -> {
                khachHangMapper.partialUpdate(existingKhachHang, khachHangDTO);

                return existingKhachHang;
            })
            .map(khachHang -> {
                KhachHang savedKhachHang = khachHangRepository.save(khachHang);
                
                // Synchronize embedded documents
                dataSynchronizationService.updateEmbeddedKhachHang(savedKhachHang);
                
                return savedKhachHang;
            })
            .map(khachHangMapper::toDto);
    }

    /**
     * Get all the khachHangs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<KhachHangDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all KhachHangs");
        return khachHangRepository.findAll(pageable).map(khachHangMapper::toDto);
    }

    /**
     * Get one khachHang by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<KhachHangDTO> findOne(String id) {
        LOG.debug("Request to get KhachHang : {}", id);
        return khachHangRepository.findById(id).map(khachHangMapper::toDto);
    }

    /**
     * Delete the khachHang by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        LOG.debug("Request to delete KhachHang : {}", id);
        khachHangRepository.deleteById(id);
    }
}
