package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.domain.enumeration.VoucherType;
import dtu.k30.msc.whm.repository.PhieuNhapXuatRepository;
import dtu.k30.msc.whm.repository.KhachHangRepository;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import dtu.k30.msc.whm.service.mapper.KhachHangMapper;
import dtu.k30.msc.whm.service.mapper.PhieuNhapXuatMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link dtu.k30.msc.whm.domain.PhieuNhapXuat}.
 */
@Service
public class PhieuNhapXuatService {

    private static final Logger LOG = LoggerFactory.getLogger(PhieuNhapXuatService.class);

    private final PhieuNhapXuatRepository phieuNhapXuatRepository;

    private final PhieuNhapXuatMapper phieuNhapXuatMapper;

    private final KhachHangRepository khachHangRepository;

    private final DataSynchronizationService dataSynchronizationService;

    public PhieuNhapXuatService(PhieuNhapXuatRepository phieuNhapXuatRepository, PhieuNhapXuatMapper phieuNhapXuatMapper, KhachHangRepository khachHangRepository, DataSynchronizationService dataSynchronizationService) {
        this.phieuNhapXuatRepository = phieuNhapXuatRepository;
        this.phieuNhapXuatMapper = phieuNhapXuatMapper;
        this.khachHangRepository = khachHangRepository;
        this.dataSynchronizationService = dataSynchronizationService;
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
        
        // Get KhachHang for synchronization
        KhachHang khachHang = null;
        if (phieuNhapXuat.getKhachHang() != null && phieuNhapXuat.getKhachHang().getId() != null) {
            khachHang = khachHangRepository.findById(phieuNhapXuat.getKhachHang().getId()).orElse(null);
        }
        
        // Synchronize embedded documents
        dataSynchronizationService.updateEmbeddedPhieuNhapXuat(phieuNhapXuat, khachHang);
        
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
        
        // Get KhachHang for synchronization
        KhachHang khachHang = null;
        if (phieuNhapXuat.getKhachHang() != null && phieuNhapXuat.getKhachHang().getId() != null) {
            khachHang = khachHangRepository.findById(phieuNhapXuat.getKhachHang().getId()).orElse(null);
        }
        
        // Synchronize embedded documents
        dataSynchronizationService.updateEmbeddedPhieuNhapXuat(phieuNhapXuat, khachHang);
        
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
            .map(phieuNhapXuat -> {
                PhieuNhapXuat savedPhieuNhapXuat = phieuNhapXuatRepository.save(phieuNhapXuat);
                
                // Get KhachHang for synchronization
                KhachHang khachHang = null;
                if (savedPhieuNhapXuat.getKhachHang() != null && savedPhieuNhapXuat.getKhachHang().getId() != null) {
                    khachHang = khachHangRepository.findById(savedPhieuNhapXuat.getKhachHang().getId()).orElse(null);
                }
                
                // Synchronize embedded documents
                dataSynchronizationService.updateEmbeddedPhieuNhapXuat(savedPhieuNhapXuat, khachHang);
                
                return savedPhieuNhapXuat;
            })
            .map(phieuNhapXuatMapper::toDto);
    }

    /**
     * Get all the phieuNhapXuats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PhieuNhapXuatDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PhieuNhapXuats");
        Page<PhieuNhapXuat> page = phieuNhapXuatRepository.findAll(pageable);
        List<PhieuNhapXuatDTO> results = page.stream().map(phieu -> {
            PhieuNhapXuatDTO dto = phieuNhapXuatMapper.toDto(phieu);
            // Handle KhachHangEmbedded instead of KhachHang
            if (phieu.getKhachHang() != null) {
                // Get tenKH from embedded object
                String tenKH = phieu.getKhachHang().getTenKH();
                dto.setTenKhachHang(tenKH);
                LOG.debug("Setting tenKhachHang for phieu {}: {}", phieu.getId(), tenKH);
                
                // Create a simple KhachHang DTO from embedded data for ID reference
                if (phieu.getKhachHang().getId() != null) {
                    dto.setKhachHang(phieuNhapXuatMapper.toDtoKhachHangId(phieu.getKhachHang()));
                }
            } else {
                LOG.debug("KhachHang is null for phieu: {}", phieu.getId());
                dto.setTenKhachHang(null);
                dto.setKhachHang(null);
            }
            return dto;
        }).toList();

        return new PageImpl<>(results, pageable, page.getTotalElements());
    }


    /**
     * Get all the phieuNhapXuats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<PhieuNhapXuatDTO> findAllWithName(Pageable pageable) {
        LOG.debug("Request to get all PhieuNhapXuats");
        return phieuNhapXuatRepository.findAllWithName(pageable);
    }


    /**
     * Get one phieuNhapXuat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PhieuNhapXuatDTO> findOne(String id) {
        LOG.debug("Request to get PhieuNhapXuatService.PhieuNhapXuat : {}", id);
        Optional<PhieuNhapXuat> phieuNhapXuat = phieuNhapXuatRepository.findById(id);
        if (phieuNhapXuat.isPresent()) {
            PhieuNhapXuatDTO dto = phieuNhapXuatMapper.toDto(phieuNhapXuat.get());
            // Handle KhachHangEmbedded instead of KhachHang
            if (phieuNhapXuat.get().getKhachHang() != null) {
                String tenKH = phieuNhapXuat.get().getKhachHang().getTenKH();
                dto.setTenKhachHang(tenKH);
                LOG.debug("Setting tenKhachHang for phieu {}: {}", id, tenKH);
                
                // Create a simple KhachHang DTO from embedded data for ID reference
                if (phieuNhapXuat.get().getKhachHang().getId() != null) {
                    dto.setKhachHang(phieuNhapXuatMapper.toDtoKhachHangId(phieuNhapXuat.get().getKhachHang()));
                    LOG.debug("Request to get PhieuNhapXuatService.PhieuNhapXuat Khachang.tenKH : {}", dto.getKhachHang().getTenKH());
                }
                LOG.debug("Request to get PhieuNhapXuatService.PhieuNhapXuat.tenKhachHang : {}", dto.getTenKhachHang());
            } else {
                LOG.debug("KhachHang is null for phieu: {}", id);
                dto.setTenKhachHang(null);
                dto.setKhachHang(null);
            }
            return Optional.of(dto);
        }
        return Optional.empty();
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
