package dtu.k30.msc.whm.service;

import dtu.k30.msc.whm.domain.ChiTietNhapXuat;
import dtu.k30.msc.whm.domain.DanhMucHang;
import dtu.k30.msc.whm.domain.KhachHang;
import dtu.k30.msc.whm.domain.PhieuNhapXuat;
import dtu.k30.msc.whm.repository.ChiTietNhapXuatRepository;
import dtu.k30.msc.whm.repository.DanhMucHangRepository;
import dtu.k30.msc.whm.repository.KhachHangRepository;
import dtu.k30.msc.whm.repository.PhieuNhapXuatRepository;
import dtu.k30.msc.whm.service.dto.ChiTietNhapXuatDTO;
import dtu.k30.msc.whm.service.mapper.ChiTietNhapXuatMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dtu.k30.msc.whm.service.mapper.DanhMucHangMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link dtu.k30.msc.whm.domain.ChiTietNhapXuat}.
 * Selective embedding: Uses embedded DTOs for performance optimization.
 */
@Service
public class ChiTietNhapXuatService {

    private static final Logger LOG = LoggerFactory.getLogger(ChiTietNhapXuatService.class);

    private final ChiTietNhapXuatRepository chiTietNhapXuatRepository;

    private final ChiTietNhapXuatMapper chiTietNhapXuatMapper;
    private final DanhMucHangMapper danhMucHangMapper;

    private final DanhMucHangRepository danhMucHangRepository;
    private final PhieuNhapXuatRepository phieuNhapXuatRepository;
    private final KhachHangRepository khachHangRepository;
    private final DataSynchronizationService dataSynchronizationService;

    public ChiTietNhapXuatService(ChiTietNhapXuatRepository chiTietNhapXuatRepository, 
                                 ChiTietNhapXuatMapper chiTietNhapXuatMapper, 
                                 DanhMucHangMapper danhMucHangMapper,
                                 DanhMucHangRepository danhMucHangRepository,
                                 PhieuNhapXuatRepository phieuNhapXuatRepository,
                                 KhachHangRepository khachHangRepository,
                                 DataSynchronizationService dataSynchronizationService) {
        this.chiTietNhapXuatRepository = chiTietNhapXuatRepository;
        this.chiTietNhapXuatMapper = chiTietNhapXuatMapper;
        this.danhMucHangMapper = danhMucHangMapper;
        this.danhMucHangRepository = danhMucHangRepository;
        this.phieuNhapXuatRepository = phieuNhapXuatRepository;
        this.khachHangRepository = khachHangRepository;
        this.dataSynchronizationService = dataSynchronizationService;
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
        
        // Get related entities for embedded documents
        PhieuNhapXuat phieuNhapXuat = null;
        DanhMucHang danhMucHang = null;
        KhachHang khachHang = null;
        
        if (chiTietNhapXuatDTO.getPhieuNhapXuat() != null && chiTietNhapXuatDTO.getPhieuNhapXuat().getId() != null) {
            phieuNhapXuat = phieuNhapXuatRepository.findById(chiTietNhapXuatDTO.getPhieuNhapXuat().getId()).orElse(null);
            if (phieuNhapXuat != null && phieuNhapXuat.getKhachHang() != null && phieuNhapXuat.getKhachHang().getId() != null) {
                khachHang = khachHangRepository.findById(phieuNhapXuat.getKhachHang().getId()).orElse(null);
            }
        }
        
        if (chiTietNhapXuatDTO.getMaHang() != null && chiTietNhapXuatDTO.getMaHang().getId() != null) {
            danhMucHang = danhMucHangRepository.findById(chiTietNhapXuatDTO.getMaHang().getId()).orElse(null);
        }
        
        // Create embedded documents
        dataSynchronizationService.createEmbeddedDocuments(chiTietNhapXuat, phieuNhapXuat, danhMucHang, khachHang);
        
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
        Page<ChiTietNhapXuat> page = chiTietNhapXuatRepository.findAll(pageable);
        List<ChiTietNhapXuatDTO> results= page.stream().map(detailXuatNhap -> {
            ChiTietNhapXuatDTO dto=chiTietNhapXuatMapper.toDto(detailXuatNhap);
            // Handle embedded objects
            if (detailXuatNhap.getPhieuNhapXuat() != null) {
                dto.getPhieuNhapXuat().setMaPhieu(detailXuatNhap.getPhieuNhapXuat().getMaPhieu());
            }
            if (detailXuatNhap.getMaHang() != null) {
                // Create embedded DTO from embedded data
                dto.setMaHang(danhMucHangMapper.toDto(detailXuatNhap.getMaHang()));
            }
            return dto;
        }).toList();
        //return chiTietNhapXuatRepository.findAll(pageable).map(chiTietNhapXuatMapper::toDto);
        return new PageImpl<>(results, pageable, page.getTotalElements());
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

    /**
     *
     * @param pnxId
     * @return List  ChiTietNhapXuat
     */
    public List<ChiTietNhapXuatDTO> findByPhieuNhapXuat(String pnxId ){
        LOG.debug("Request to get ChiTietNhapXuatService.findByPhieuNhapXuat with PhieuNhapXuat.id: {}", pnxId);
        List<ChiTietNhapXuat> chiTietNhapXuatList= chiTietNhapXuatRepository.findByPhieuNhapXuatCustom(pnxId);
        LOG.debug("Request to get ChiTietNhapXuatService.chiTietNhapXuatRepository Result chiTietNhapXuatList.Size: {}", chiTietNhapXuatList.size());
        if (chiTietNhapXuatList.size()==0)
        {
            // Use String ID instead of ObjectId
            chiTietNhapXuatList= chiTietNhapXuatRepository.findByPhieuNhapXuatId(pnxId);
            LOG.debug("Request to get chiTietNhapXuatRepository.findByPhieuNhapXuatId Result chiTietNhapXuatList.Size: {}", chiTietNhapXuatList.size());
        }
        List<ChiTietNhapXuatDTO> results = new ArrayList<ChiTietNhapXuatDTO>();
        for (ChiTietNhapXuat detailEntity : chiTietNhapXuatList) {
            ChiTietNhapXuatDTO chiTietNhapXuatDTO = chiTietNhapXuatMapper.toDto(detailEntity);
            // Handle embedded objects
            if (detailEntity.getPhieuNhapXuat() != null) {
                chiTietNhapXuatDTO.getPhieuNhapXuat().setMaPhieu(detailEntity.getPhieuNhapXuat().getMaPhieu());
            }
            if (detailEntity.getMaHang() != null) {
                // Create embedded DTO from embedded data
                chiTietNhapXuatDTO.setMaHang(danhMucHangMapper.toDto(detailEntity.getMaHang()));
            }
            results.add(chiTietNhapXuatDTO);
        }
    return results;
    }
}
