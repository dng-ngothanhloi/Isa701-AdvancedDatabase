package dtu.k30.msc.whm.web.rest;

import dtu.k30.msc.whm.repository.DanhMucHangRepository;
import dtu.k30.msc.whm.service.DanhMucHangService;
import dtu.k30.msc.whm.service.dto.DanhMucHangDTO;
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
 * REST controller for managing {@link dtu.k30.msc.whm.domain.DanhMucHang}.
 */
@RestController
@RequestMapping("/api/danh-muc-hangs")
public class DanhMucHangResource {

    private static final Logger LOG = LoggerFactory.getLogger(DanhMucHangResource.class);

    private static final String ENTITY_NAME = "danhMucHang";
    private final DanhMucHangService danhMucHangService;
    private final DanhMucHangRepository danhMucHangRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public DanhMucHangResource(DanhMucHangService danhMucHangService, DanhMucHangRepository danhMucHangRepository) {
        this.danhMucHangService = danhMucHangService;
        this.danhMucHangRepository = danhMucHangRepository;
    }

    /**
     * {@code POST  /danh-muc-hangs} : Create a new danhMucHang.
     *
     * @param danhMucHangDTO the danhMucHangDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new danhMucHangDTO, or with status {@code 400 (Bad Request)} if the danhMucHang has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DanhMucHangDTO> createDanhMucHang(@Valid @RequestBody DanhMucHangDTO danhMucHangDTO) throws URISyntaxException {
        LOG.debug("REST request to save DanhMucHang : {}", danhMucHangDTO);
        if (danhMucHangDTO.getId() != null) {
            throw new BadRequestAlertException("A new danhMucHang cannot already have an ID", ENTITY_NAME, "idexists");
        }
        danhMucHangDTO = danhMucHangService.save(danhMucHangDTO);
        return ResponseEntity.created(new URI("/api/danh-muc-hangs/" + danhMucHangDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, danhMucHangDTO.getId()))
                .body(danhMucHangDTO);
    }

    /**
     * {@code PUT  /danh-muc-hangs/:id} : Updates an existing danhMucHang.
     *
     * @param id             the id of the danhMucHangDTO to save.
     * @param danhMucHangDTO the danhMucHangDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated danhMucHangDTO,
     * or with status {@code 400 (Bad Request)} if the danhMucHangDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the danhMucHangDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DanhMucHangDTO> updateDanhMucHang(
            @PathVariable(value = "id", required = false) final String id,
            @Valid @RequestBody DanhMucHangDTO danhMucHangDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DanhMucHang : {}, {}", id, danhMucHangDTO);
        if (danhMucHangDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, danhMucHangDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danhMucHangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        danhMucHangDTO = danhMucHangService.update(danhMucHangDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, danhMucHangDTO.getId()))
                .body(danhMucHangDTO);
    }

    /**
     * {@code PATCH  /danh-muc-hangs/:id} : Partial updates given fields of an existing danhMucHang, field will ignore if it is null
     *
     * @param id             the id of the danhMucHangDTO to save.
     * @param danhMucHangDTO the danhMucHangDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated danhMucHangDTO,
     * or with status {@code 400 (Bad Request)} if the danhMucHangDTO is not valid,
     * or with status {@code 404 (Not Found)} if the danhMucHangDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the danhMucHangDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<DanhMucHangDTO> partialUpdateDanhMucHang(
            @PathVariable(value = "id", required = false) final String id,
            @NotNull @RequestBody DanhMucHangDTO danhMucHangDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DanhMucHang partially : {}, {}", id, danhMucHangDTO);
        if (danhMucHangDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, danhMucHangDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!danhMucHangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DanhMucHangDTO> result = danhMucHangService.partialUpdate(danhMucHangDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, danhMucHangDTO.getId())
        );
    }

    /**
     * {@code GET  /danh-muc-hangs} : get all the danhMucHangs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of danhMucHangs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DanhMucHangDTO>> getAllDanhMucHangs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of DanhMucHangs");
        Page<DanhMucHangDTO> page = danhMucHangService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /danh-muc-hangs/:id} : get the "id" danhMucHang.
     *
     * @param id the id of the danhMucHangDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the danhMucHangDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DanhMucHangDTO> getDanhMucHang(@PathVariable("id") String id) {
        LOG.debug("REST request to get DanhMucHang : {}", id);
        Optional<DanhMucHangDTO> danhMucHangDTO = danhMucHangService.findOne(id);
        return ResponseUtil.wrapOrNotFound(danhMucHangDTO);
    }

    /**
     * {@code DELETE  /danh-muc-hangs/:id} : delete the "id" danhMucHang.
     *
     * @param id the id of the danhMucHangDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhMucHang(@PathVariable("id") String id) {
        LOG.debug("REST request to delete DanhMucHang : {}", id);
        danhMucHangService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
