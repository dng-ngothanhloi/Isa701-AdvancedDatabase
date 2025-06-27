package dtu.k30.msc.whm.web.rest;

import dtu.k30.msc.whm.repository.PhieuNhapXuatRepository;
import dtu.k30.msc.whm.service.ChiTietNhapXuatService;
import dtu.k30.msc.whm.service.PhieuNhapXuatService;
import dtu.k30.msc.whm.service.dto.ChiTietNhapXuatDTO;
import dtu.k30.msc.whm.service.dto.PhieuNhapXuatDTO;
import dtu.k30.msc.whm.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dtu.k30.msc.whm.domain.PhieuNhapXuat}.
 */
@RestController
@RequestMapping("/api/phieu-nhap-xuats")
public class PhieuNhapXuatResource {

    private static final Logger LOG = LoggerFactory.getLogger(PhieuNhapXuatResource.class);

    private static final String ENTITY_NAME = "phieuNhapXuat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhieuNhapXuatService phieuNhapXuatService;
    private final ChiTietNhapXuatService chiTietNhapXuatService;

    private final PhieuNhapXuatRepository phieuNhapXuatRepository;

    public PhieuNhapXuatResource(PhieuNhapXuatService phieuNhapXuatService, PhieuNhapXuatRepository phieuNhapXuatRepository, ChiTietNhapXuatService chiTietNhapXuatService) {
        this.phieuNhapXuatService = phieuNhapXuatService;
        this.chiTietNhapXuatService = chiTietNhapXuatService;
        this.phieuNhapXuatRepository = phieuNhapXuatRepository;
    }

    /**
     * {@code POST  /phieu-nhap-xuats} : Create a new phieuNhapXuat.
     *
     * @param phieuNhapXuatDTO the phieuNhapXuatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phieuNhapXuatDTO, or with status {@code 400 (Bad Request)} if the phieuNhapXuat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PhieuNhapXuatDTO> createPhieuNhapXuat(@Valid @RequestBody PhieuNhapXuatDTO phieuNhapXuatDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PhieuNhapXuat : {}", phieuNhapXuatDTO);
        if (phieuNhapXuatDTO.getId() != null) {
            throw new BadRequestAlertException("A new phieuNhapXuat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        phieuNhapXuatDTO = phieuNhapXuatService.save(phieuNhapXuatDTO);
        return ResponseEntity.created(new URI("/api/phieu-nhap-xuats/" + phieuNhapXuatDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, phieuNhapXuatDTO.getId()))
            .body(phieuNhapXuatDTO);
    }

    /**
     * {@code PUT  /phieu-nhap-xuats/:id} : Updates an existing phieuNhapXuat.
     *
     * @param id the id of the phieuNhapXuatDTO to save.
     * @param phieuNhapXuatDTO the phieuNhapXuatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phieuNhapXuatDTO,
     * or with status {@code 400 (Bad Request)} if the phieuNhapXuatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phieuNhapXuatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PhieuNhapXuatDTO> updatePhieuNhapXuat(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody PhieuNhapXuatDTO phieuNhapXuatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PhieuNhapXuat : {}, {}", id, phieuNhapXuatDTO);
        if (phieuNhapXuatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phieuNhapXuatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phieuNhapXuatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        phieuNhapXuatDTO = phieuNhapXuatService.update(phieuNhapXuatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phieuNhapXuatDTO.getId()))
            .body(phieuNhapXuatDTO);
    }

    /**
     * {@code PATCH  /phieu-nhap-xuats/:id} : Partial updates given fields of an existing phieuNhapXuat, field will ignore if it is null
     *
     * @param id the id of the phieuNhapXuatDTO to save.
     * @param phieuNhapXuatDTO the phieuNhapXuatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phieuNhapXuatDTO,
     * or with status {@code 400 (Bad Request)} if the phieuNhapXuatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the phieuNhapXuatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the phieuNhapXuatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhieuNhapXuatDTO> partialUpdatePhieuNhapXuat(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody PhieuNhapXuatDTO phieuNhapXuatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PhieuNhapXuat partially : {}, {}", id, phieuNhapXuatDTO);
        if (phieuNhapXuatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phieuNhapXuatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phieuNhapXuatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhieuNhapXuatDTO> result = phieuNhapXuatService.partialUpdate(phieuNhapXuatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phieuNhapXuatDTO.getId())
        );
    }

    /**
     * {@code GET  /phieu-nhap-xuats} : get all the phieuNhapXuats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phieuNhapXuats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PhieuNhapXuatDTO>> getAllPhieuNhapXuats(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PhieuNhapXuats");
        Page<PhieuNhapXuatDTO> page = phieuNhapXuatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /Backup-phieu-nhap-xuats/:id} : get the "id" phieuNhapXuat.
     *
     * @param id the id of the phieuNhapXuatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phieuNhapXuatDTO, or with status {@code 404 (Not Found)}.

    @GetMapping("/{id}")
    public ResponseEntity<PhieuNhapXuatDTO> getPhieuNhapXuat(@PathVariable("id") String id) {
        LOG.debug("REST request to get PhieuNhapXuat : {}", id);
        Optional<PhieuNhapXuatDTO> phieuNhapXuatDTO = phieuNhapXuatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(phieuNhapXuatDTO);
    }
     */
    /**
     * {@code GET  /phieu-nhap-xuats/:id} : get the all the ChiTietNhapXuats by "id" of PhieuNhapXuat.
     *
     * @param id the id of the phieuNhapXuatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phieuNhapXuatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhieuNhapXuatDTO> getChiTietNhapXuat(@PathVariable("id") String id) {
        LOG.debug("REST request to get PhieuNhapXuat With Detail Ma Hang: {}", id);
        List<ChiTietNhapXuatDTO> chiTietNhapXuatDTOList = chiTietNhapXuatService.findByPhieuNhapXuat(id);
        for(int i =0; i< chiTietNhapXuatDTOList.size();i++){
            ChiTietNhapXuatDTO dto = chiTietNhapXuatDTOList.get(i);
            if(dto!=null)
                LOG.debug("PhieuNhapXuat With Detail Ma Hang : {} with data: {}",dto.getId(), dto.toString());
        }
        Optional<PhieuNhapXuatDTO> phieuNhapXuatDTO = phieuNhapXuatService.findOne(id);
        LOG.debug("REST Verified to get PhieuNhapXuat With Detail Ma Hang: {} have Customer ", id, phieuNhapXuatDTO.get().getKhachHang().getTenKH());

        phieuNhapXuatDTO.ifPresent(nhapXuatDTO -> nhapXuatDTO.setChiTietNhapXuatDTOList(chiTietNhapXuatDTOList));
        return ResponseUtil.wrapOrNotFound(phieuNhapXuatDTO);
    }


    /**
     * {@code DELETE  /phieu-nhap-xuats/:id} : delete the "id" phieuNhapXuat.
     *
     * @param id the id of the phieuNhapXuatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhieuNhapXuat(@PathVariable("id") String id) {
        LOG.debug("REST request to delete PhieuNhapXuat : {}", id);
        phieuNhapXuatService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
