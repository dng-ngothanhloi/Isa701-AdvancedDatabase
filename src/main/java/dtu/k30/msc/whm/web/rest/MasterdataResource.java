package dtu.k30.msc.whm.web.rest;

import dtu.k30.msc.whm.repository.MasterdataRepository;
import dtu.k30.msc.whm.service.MasterdataService;
import dtu.k30.msc.whm.service.dto.MasterdataDTO;
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
 * REST controller for managing {@link dtu.k30.msc.whm.domain.Masterdata}.
 */
@RestController
@RequestMapping("/api/masterdata")
public class MasterdataResource {

    private static final Logger LOG = LoggerFactory.getLogger(MasterdataResource.class);

    private static final String ENTITY_NAME = "masterdata";
    private final MasterdataService masterdataService;
    private final MasterdataRepository masterdataRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public MasterdataResource(MasterdataService masterdataService, MasterdataRepository masterdataRepository) {
        this.masterdataService = masterdataService;
        this.masterdataRepository = masterdataRepository;
    }

    /**
     * {@code POST  /masterdata} : Create a new masterdata.
     *
     * @param masterdataDTO the masterdataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterdataDTO, or with status {@code 400 (Bad Request)} if the masterdata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MasterdataDTO> createMasterdata(@Valid @RequestBody MasterdataDTO masterdataDTO) throws URISyntaxException {
        LOG.debug("REST request to save Masterdata : {}", masterdataDTO);
        if (masterdataDTO.getId() != null) {
            throw new BadRequestAlertException("A new masterdata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        masterdataDTO = masterdataService.save(masterdataDTO);
        return ResponseEntity.created(new URI("/api/masterdata/" + masterdataDTO.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, masterdataDTO.getId()))
                .body(masterdataDTO);
    }

    /**
     * {@code PUT  /masterdata/:id} : Updates an existing masterdata.
     *
     * @param id            the id of the masterdataDTO to save.
     * @param masterdataDTO the masterdataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterdataDTO,
     * or with status {@code 400 (Bad Request)} if the masterdataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterdataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MasterdataDTO> updateMasterdata(
            @PathVariable(value = "id", required = false) final String id,
            @Valid @RequestBody MasterdataDTO masterdataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Masterdata : {}, {}", id, masterdataDTO);
        if (masterdataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterdataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterdataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        masterdataDTO = masterdataService.update(masterdataDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterdataDTO.getId()))
                .body(masterdataDTO);
    }

    /**
     * {@code PATCH  /masterdata/:id} : Partial updates given fields of an existing masterdata, field will ignore if it is null
     *
     * @param id            the id of the masterdataDTO to save.
     * @param masterdataDTO the masterdataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterdataDTO,
     * or with status {@code 400 (Bad Request)} if the masterdataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the masterdataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterdataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<MasterdataDTO> partialUpdateMasterdata(
            @PathVariable(value = "id", required = false) final String id,
            @NotNull @RequestBody MasterdataDTO masterdataDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Masterdata partially : {}, {}", id, masterdataDTO);
        if (masterdataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterdataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterdataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterdataDTO> result = masterdataService.partialUpdate(masterdataDTO);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterdataDTO.getId())
        );
    }

    /**
     * {@code GET  /masterdata} : get all the masterdata.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of masterdata in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MasterdataDTO>> getAllMasterdata(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Masterdata");
        Page<MasterdataDTO> page = masterdataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /masterdata/:id} : get the "id" masterdata.
     *
     * @param id the id of the masterdataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterdataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MasterdataDTO> getMasterdata(@PathVariable("id") String id) {
        LOG.debug("REST request to get Masterdata : {}", id);
        Optional<MasterdataDTO> masterdataDTO = masterdataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(masterdataDTO);
    }

    /**
     * {@code DELETE  /masterdata/:id} : delete the "id" masterdata.
     *
     * @param id the id of the masterdataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMasterdata(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Masterdata : {}", id);
        masterdataService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
