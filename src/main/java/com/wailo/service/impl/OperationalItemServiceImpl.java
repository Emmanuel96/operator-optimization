package com.wailo.service.impl;

import com.wailo.domain.OperationalItem;
import com.wailo.repository.OperationalItemRepository;
import com.wailo.service.OperationalItemService;
import com.wailo.service.dto.OperationalItemDTO;
import com.wailo.service.mapper.OperationalItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OperationalItem}.
 */
@Service
@Transactional
public class OperationalItemServiceImpl implements OperationalItemService {

    private final Logger log = LoggerFactory.getLogger(OperationalItemServiceImpl.class);

    private final OperationalItemRepository operationalItemRepository;

    private final OperationalItemMapper operationalItemMapper;

    public OperationalItemServiceImpl(OperationalItemRepository operationalItemRepository, OperationalItemMapper operationalItemMapper) {
        this.operationalItemRepository = operationalItemRepository;
        this.operationalItemMapper = operationalItemMapper;
    }

    @Override
    public OperationalItemDTO save(OperationalItemDTO operationalItemDTO) {
        log.debug("Request to save OperationalItem : {}", operationalItemDTO);
        OperationalItem operationalItem = operationalItemMapper.toEntity(operationalItemDTO);
        operationalItem = operationalItemRepository.save(operationalItem);
        return operationalItemMapper.toDto(operationalItem);
    }

    @Override
    public OperationalItemDTO update(OperationalItemDTO operationalItemDTO) {
        log.debug("Request to update OperationalItem : {}", operationalItemDTO);
        OperationalItem operationalItem = operationalItemMapper.toEntity(operationalItemDTO);
        operationalItem = operationalItemRepository.save(operationalItem);
        return operationalItemMapper.toDto(operationalItem);
    }

    @Override
    public Optional<OperationalItemDTO> partialUpdate(OperationalItemDTO operationalItemDTO) {
        log.debug("Request to partially update OperationalItem : {}", operationalItemDTO);

        return operationalItemRepository
            .findById(operationalItemDTO.getId())
            .map(existingOperationalItem -> {
                operationalItemMapper.partialUpdate(existingOperationalItem, operationalItemDTO);

                return existingOperationalItem;
            })
            .map(operationalItemRepository::save)
            .map(operationalItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperationalItemDTO> findAll() {
        log.debug("Request to get all OperationalItems");
        return operationalItemRepository
            .findAll()
            .stream()
            .map(operationalItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperationalItemDTO> findOne(Long id) {
        log.debug("Request to get OperationalItem : {}", id);
        return operationalItemRepository.findById(id).map(operationalItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OperationalItem : {}", id);
        operationalItemRepository.deleteById(id);
    }
}
