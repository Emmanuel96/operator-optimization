package com.wailo.service;

import com.wailo.service.dto.OperationalItemDTO;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.wailo.domain.OperationalItem}.
 */
public interface OperationalItemService {
    /**
     * Save a operationalItem.
     *
     * @param operationalItemDTO the entity to save.
     * @return the persisted entity.
     */
    OperationalItemDTO save(OperationalItemDTO operationalItemDTO);

    /**
     * Updates a operationalItem.
     *
     * @param operationalItemDTO the entity to update.
     * @return the persisted entity.
     */
    OperationalItemDTO update(OperationalItemDTO operationalItemDTO);

    /**
     * Partially updates a operationalItem.
     *
     * @param operationalItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperationalItemDTO> partialUpdate(OperationalItemDTO operationalItemDTO);

    /**
     * Get all the operationalItems.
     *
     * @return the list of entities.
     */
    List<OperationalItemDTO> findAll();

    /**
     * Get the "id" operationalItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperationalItemDTO> findOne(Long id);

    /**
     * Delete the "id" operationalItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Mono<Void> download();
}
