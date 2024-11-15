package com.wailo.service;

import com.wailo.service.dto.PadDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.wailo.domain.Pad}.
 */
public interface PadService {
    /**
     * Save a pad.
     *
     * @param padDTO the entity to save.
     * @return the persisted entity.
     */
    PadDTO save(PadDTO padDTO);

    /**
     * Updates a pad.
     *
     * @param padDTO the entity to update.
     * @return the persisted entity.
     */
    PadDTO update(PadDTO padDTO);

    /**
     * Partially updates a pad.
     *
     * @param padDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PadDTO> partialUpdate(PadDTO padDTO);

    /**
     * Get all the pads.
     *
     * @return the list of entities.
     */
    List<PadDTO> findAll();

    /**
     * Get the "id" pad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PadDTO> findOne(Long id);

    /**
     * Delete the "id" pad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Mono<Void> download();

}
