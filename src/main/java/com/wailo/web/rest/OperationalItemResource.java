package com.wailo.web.rest;

import com.wailo.repository.OperationalItemRepository;
import com.wailo.service.OperationalItemService;
import com.wailo.service.dto.OperationalItemDTO;
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
 * REST controller for managing {@link com.wailo.domain.OperationalItem}.
 */
@RestController
@RequestMapping("/api")
public class OperationalItemResource {

    private final Logger log = LoggerFactory.getLogger(OperationalItemResource.class);

    private static final String ENTITY_NAME = "operationalItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperationalItemService operationalItemService;

    private final OperationalItemRepository operationalItemRepository;

    public OperationalItemResource(OperationalItemService operationalItemService, OperationalItemRepository operationalItemRepository) {
        this.operationalItemService = operationalItemService;
        this.operationalItemRepository = operationalItemRepository;
    }

    /**
     * {@code POST  /operational-items} : Create a new operationalItem.
     *
     * @param operationalItemDTO the operationalItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operationalItemDTO, or with status {@code 400 (Bad Request)} if the operationalItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operational-items")
    public ResponseEntity<OperationalItemDTO> createOperationalItem(@Valid @RequestBody OperationalItemDTO operationalItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save OperationalItem : {}", operationalItemDTO);
        if (operationalItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new operationalItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OperationalItemDTO result = operationalItemService.save(operationalItemDTO);
        return ResponseEntity
            .created(new URI("/api/operational-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operational-items/:id} : Updates an existing operationalItem.
     *
     * @param id the id of the operationalItemDTO to save.
     * @param operationalItemDTO the operationalItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operationalItemDTO,
     * or with status {@code 400 (Bad Request)} if the operationalItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operationalItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operational-items/{id}")
    public ResponseEntity<OperationalItemDTO> updateOperationalItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OperationalItemDTO operationalItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OperationalItem : {}, {}", id, operationalItemDTO);
        if (operationalItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operationalItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationalItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OperationalItemDTO result = operationalItemService.update(operationalItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operationalItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operational-items/:id} : Partial updates given fields of an existing operationalItem, field will ignore if it is null
     *
     * @param id the id of the operationalItemDTO to save.
     * @param operationalItemDTO the operationalItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operationalItemDTO,
     * or with status {@code 400 (Bad Request)} if the operationalItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the operationalItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the operationalItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operational-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OperationalItemDTO> partialUpdateOperationalItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OperationalItemDTO operationalItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OperationalItem partially : {}, {}", id, operationalItemDTO);
        if (operationalItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operationalItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operationalItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OperationalItemDTO> result = operationalItemService.partialUpdate(operationalItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operationalItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /operational-items} : get all the operationalItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operationalItems in body.
     */
    @GetMapping("/operational-items")
    public List<OperationalItemDTO> getAllOperationalItems() {
        log.debug("REST request to get all OperationalItems");
        return operationalItemService.findAll();
    }

    /**
     * {@code GET  /operational-items/:id} : get the "id" operationalItem.
     *
     * @param id the id of the operationalItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operationalItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operational-items/{id}")
    public ResponseEntity<OperationalItemDTO> getOperationalItem(@PathVariable Long id) {
        log.debug("REST request to get OperationalItem : {}", id);
        Optional<OperationalItemDTO> operationalItemDTO = operationalItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(operationalItemDTO);
    }

    /**
     * {@code DELETE  /operational-items/:id} : delete the "id" operationalItem.
     *
     * @param id the id of the operationalItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operational-items/{id}")
    public ResponseEntity<Void> deleteOperationalItem(@PathVariable Long id) {
        log.debug("REST request to delete OperationalItem : {}", id);
        operationalItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
