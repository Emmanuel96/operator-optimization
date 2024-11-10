package com.wailo.web.rest;

import com.wailo.repository.PadRepository;
import com.wailo.service.PadService;
import com.wailo.service.dto.PadDTO;
import com.wailo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wailo.domain.Pad}.
 */
@RestController
@RequestMapping("/api")
public class PadResource {

    private final Logger log = LoggerFactory.getLogger(PadResource.class);

    private static final String ENTITY_NAME = "pad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PadService padService;

    private final PadRepository padRepository;

    public PadResource(PadService padService, PadRepository padRepository) {
        this.padService = padService;
        this.padRepository = padRepository;
    }

    /**
     * {@code POST  /pads} : Create a new pad.
     *
     * @param padDTO the padDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new padDTO, or with status {@code 400 (Bad Request)} if the pad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pads")
    public ResponseEntity<PadDTO> createPad(@Valid @RequestBody PadDTO padDTO) throws URISyntaxException {
        log.debug("REST request to save Pad : {}", padDTO);
        if (padDTO.getId() != null) {
            throw new BadRequestAlertException("A new pad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PadDTO result = padService.save(padDTO);
        return ResponseEntity
            .created(new URI("/api/pads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pads/:id} : Updates an existing pad.
     *
     * @param id the id of the padDTO to save.
     * @param padDTO the padDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated padDTO,
     * or with status {@code 400 (Bad Request)} if the padDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the padDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pads/{id}")
    public ResponseEntity<PadDTO> updatePad(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody PadDTO padDTO)
        throws URISyntaxException {
        log.debug("REST request to update Pad : {}, {}", id, padDTO);
        if (padDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, padDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!padRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PadDTO result = padService.update(padDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, padDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pads/:id} : Partial updates given fields of an existing pad, field will ignore if it is null
     *
     * @param id the id of the padDTO to save.
     * @param padDTO the padDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated padDTO,
     * or with status {@code 400 (Bad Request)} if the padDTO is not valid,
     * or with status {@code 404 (Not Found)} if the padDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the padDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PadDTO> partialUpdatePad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PadDTO padDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pad partially : {}, {}", id, padDTO);
        if (padDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, padDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!padRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PadDTO> result = padService.partialUpdate(padDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, padDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pads} : get all the pads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pads in body.
     */
    @GetMapping("/pads")
    public List<PadDTO> getAllPads() {
        log.debug("REST request to get all Pads");
        return padService.findAll();
    }

    /**
     * {@code GET  /pads/:id} : get the "id" pad.
     *
     * @param id the id of the padDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the padDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pads/{id}")
    public ResponseEntity<PadDTO> getPad(@PathVariable Long id) {
        log.debug("REST request to get Pad : {}", id);
        Optional<PadDTO> padDTO = padService.findOne(id);
        return ResponseUtil.wrapOrNotFound(padDTO);
    }

    /**
     * {@code DELETE  /pads/:id} : delete the "id" pad.
     *
     * @param id the id of the padDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pads/{id}")
    public ResponseEntity<Void> deletePad(@PathVariable Long id) {
        log.debug("REST request to delete Pad : {}", id);
        padService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
