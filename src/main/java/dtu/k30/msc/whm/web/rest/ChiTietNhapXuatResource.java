package dtu.k30.msc.whm.web.rest;

import dtu.k30.msc.whm.repository.ChiTietNhapXuatRepository;
import dtu.k30.msc.whm.service.ChiTietNhapXuatService;
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
 * REST controller for managing {@link dtu.k30.msc.whm.domain.ChiTietNhapXuat}.
 */
@RestController
@RequestMapping("/api/chi-tiet-nhap-xuats")
public class ChiTietNhapXuatResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChiTietNhapXuatResource.class);

    private static final String ENTITY_NAME = "chiTietNhapXuat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChiTietNhapXuatService chiTietNhapXuatService;

    private final ChiTietNhapXuatRepository chiTietNhapXuatRepository;

    public ChiTietNhapXuatResource(ChiTietNhapXuatService chiTietNhapXuatService, ChiTietNhapXuatRepository chiTietNhapXuatRepository) {
        this.chiTietNhapXuatService = chiTietNhapXuatService;
        this.chiTietNhapXuatRepository = chiTietNhapXuatRepository;
    }

    /**
     * {@code POST  /chi-tiet-nhap-xuats} : Create a new chiTietNhapXuat.
     *
     * @param chiTietNhapXuatDTO the chiTietNhapXuatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chiTietNhapXuatDTO, or with status {@code 400 (Bad Request)} if the chiTietNhapXuat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChiTietNhapXuatDTO> createChiTietNhapXuat(@Valid @RequestBody ChiTietNhapXuatDTO chiTietNhapXuatDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ChiTietNhapXuat : {}", chiTietNhapXuatDTO);
        if (chiTietNhapXuatDTO.getId() != null) {
            throw new BadRequestAlertException("A new chiTietNhapXuat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chiTietNhapXuatDTO = chiTietNhapXuatService.save(chiTietNhapXuatDTO);
        return ResponseEntity.created(new URI("/api/chi-tiet-nhap-xuats/" + chiTietNhapXuatDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chiTietNhapXuatDTO.getId()))
            .body(chiTietNhapXuatDTO);
    }

    /**
     * {@code PUT  /chi-tiet-nhap-xuats/:id} : Updates an existing chiTietNhapXuat.
     *
     * @param id the id of the chiTietNhapXuatDTO to save.
     * @param chiTietNhapXuatDTO the chiTietNhapXuatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chiTietNhapXuatDTO,
     * or with status {@code 400 (Bad Request)} if the chiTietNhapXuatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chiTietNhapXuatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChiTietNhapXuatDTO> updateChiTietNhapXuat(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody ChiTietNhapXuatDTO chiTietNhapXuatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChiTietNhapXuat : {}, {}", id, chiTietNhapXuatDTO);
        if (chiTietNhapXuatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chiTietNhapXuatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chiTietNhapXuatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chiTietNhapXuatDTO = chiTietNhapXuatService.update(chiTietNhapXuatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chiTietNhapXuatDTO.getId()))
            .body(chiTietNhapXuatDTO);
    }

    /**
     * {@code PATCH  /chi-tiet-nhap-xuats/:id} : Partial updates given fields of an existing chiTietNhapXuat, field will ignore if it is null
     *
     * @param id the id of the chiTietNhapXuatDTO to save.
     * @param chiTietNhapXuatDTO the chiTietNhapXuatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chiTietNhapXuatDTO,
     * or with status {@code 400 (Bad Request)} if the chiTietNhapXuatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chiTietNhapXuatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chiTietNhapXuatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChiTietNhapXuatDTO> partialUpdateChiTietNhapXuat(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody ChiTietNhapXuatDTO chiTietNhapXuatDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChiTietNhapXuat partially : {}, {}", id, chiTietNhapXuatDTO);
        if (chiTietNhapXuatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chiTietNhapXuatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chiTietNhapXuatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChiTietNhapXuatDTO> result = chiTietNhapXuatService.partialUpdate(chiTietNhapXuatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chiTietNhapXuatDTO.getId())
        );
    }

    /**
     * {@code GET  /chi-tiet-nhap-xuats} : get all the chiTietNhapXuats.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chiTietNhapXuats in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChiTietNhapXuatDTO>> getAllChiTietNhapXuats(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ChiTietNhapXuats");
        Page<ChiTietNhapXuatDTO> page = chiTietNhapXuatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chi-tiet-nhap-xuats/:id} : get the "id" chiTietNhapXuat.
     *
     * @param id the id of the chiTietNhapXuatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chiTietNhapXuatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChiTietNhapXuatDTO> getChiTietNhapXuat(@PathVariable("id") String id) {
        LOG.debug("REST request to get ChiTietNhapXuat : {}", id);
        Optional<ChiTietNhapXuatDTO> chiTietNhapXuatDTO = chiTietNhapXuatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chiTietNhapXuatDTO);
    }

    /**
     * {@code DELETE  /chi-tiet-nhap-xuats/:id} : delete the "id" chiTietNhapXuat.
     *
     * @param id the id of the chiTietNhapXuatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChiTietNhapXuat(@PathVariable("id") String id) {
        LOG.debug("REST request to delete ChiTietNhapXuat : {}", id);
        chiTietNhapXuatService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
