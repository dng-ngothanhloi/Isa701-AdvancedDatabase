package dtu.k30.msc.whm.web.rest;

import dtu.k30.msc.whm.repository.KhachHangRepository;
import dtu.k30.msc.whm.service.KhachHangService;
import dtu.k30.msc.whm.service.dto.KhachHangDTO;
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
 * REST controller for managing {@link dtu.k30.msc.whm.domain.KhachHang}.
 */
@RestController
@RequestMapping("/api/khach-hangs")
public class KhachHangResource {

    private static final Logger LOG = LoggerFactory.getLogger(KhachHangResource.class);

    private static final String ENTITY_NAME = "khachHang";
    private final KhachHangService khachHangService;
    private final KhachHangRepository khachHangRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public KhachHangResource(KhachHangService khachHangService, KhachHangRepository khachHangRepository) {
        this.khachHangService = khachHangService;
        this.khachHangRepository = khachHangRepository;
    }

    /**
     * {@code POST  /khach-hangs} : Create a new khachHang.
     *
     * @param khachHangDTO the khachHangDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new khachHangDTO, or with status {@code 400 (Bad Request)} if the khachHang has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KhachHangDTO> createKhachHang(@Valid @RequestBody KhachHangDTO khachHangDTO) throws URISyntaxException {
        LOG.debug("REST request to save KhachHang : {}", khachHangDTO);
        if (khachHangDTO.getId() != null) {
            throw new BadRequestAlertException("A new khachHang cannot already have an ID", ENTITY_NAME, "idexists");
        }
        khachHangDTO = khachHangService.save(khachHangDTO);
        return ResponseEntity.created(new URI("/api/khach-hangs/" + khachHangDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, khachHangDTO.getId()))
                .body(khachHangDTO);
    }

    /**
     * {@code PUT  /khach-hangs/:id} : Updates an existing khachHang.
     *
     * @param id           the id of the khachHangDTO to save.
     * @param khachHangDTO the khachHangDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated khachHangDTO,
     * or with status {@code 400 (Bad Request)} if the khachHangDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the khachHangDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KhachHangDTO> updateKhachHang(
            @PathVariable(value = "id", required = false) final String id,
            @Valid @RequestBody KhachHangDTO khachHangDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update KhachHang : {}, {}", id, khachHangDTO);
        if (khachHangDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, khachHangDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!khachHangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        khachHangDTO = khachHangService.update(khachHangDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, khachHangDTO.getId()))
                .body(khachHangDTO);
    }

    /**
     * {@code PATCH  /khach-hangs/:id} : Partial updates given fields of an existing khachHang, field will ignore if it is null
     *
     * @param id           the id of the khachHangDTO to save.
     * @param khachHangDTO the khachHangDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated khachHangDTO,
     * or with status {@code 400 (Bad Request)} if the khachHangDTO is not valid,
     * or with status {@code 404 (Not Found)} if the khachHangDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the khachHangDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<KhachHangDTO> partialUpdateKhachHang(
            @PathVariable(value = "id", required = false) final String id,
            @NotNull @RequestBody KhachHangDTO khachHangDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KhachHang partially : {}, {}", id, khachHangDTO);
        if (khachHangDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, khachHangDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!khachHangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KhachHangDTO> result = khachHangService.partialUpdate(khachHangDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, khachHangDTO.getId())
        );
    }

    /**
     * {@code GET  /khach-hangs} : get all the khachHangs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of khachHangs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KhachHangDTO>> getAllKhachHangs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of KhachHangs");
        Page<KhachHangDTO> page = khachHangService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /khach-hangs/:id} : get the "id" khachHang.
     *
     * @param id the id of the khachHangDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the khachHangDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KhachHangDTO> getKhachHang(@PathVariable("id") String id) {
        LOG.debug("REST request to get KhachHang : {}", id);
        Optional<KhachHangDTO> khachHangDTO = khachHangService.findOne(id);
        return ResponseUtil.wrapOrNotFound(khachHangDTO);
    }

    /**
     * {@code DELETE  /khach-hangs/:id} : delete the "id" khachHang.
     *
     * @param id the id of the khachHangDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKhachHang(@PathVariable("id") String id) {
        LOG.debug("REST request to delete KhachHang : {}", id);
        khachHangService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
