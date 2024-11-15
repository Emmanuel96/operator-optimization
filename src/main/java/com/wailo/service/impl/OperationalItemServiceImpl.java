package com.wailo.service.impl;

import com.wailo.config.ApplicationProperties;
import com.wailo.domain.OperationalItem;
import com.wailo.domain.enumeration.OperationalItemTypes;
import com.wailo.models.ezops.responses.OperationalItemResponse;
import com.wailo.repository.LocationRepository;
import com.wailo.repository.OperationalItemRepository;
import com.wailo.service.OperationalItemService;
import com.wailo.service.dto.OperationalItemDTO;
import com.wailo.service.mapper.OperationalItemMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wailo.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link OperationalItem}.
 */
@Service
@Transactional
public class OperationalItemServiceImpl implements OperationalItemService {

    private final Logger log = LoggerFactory.getLogger(OperationalItemServiceImpl.class);

    private final OperationalItemRepository operationalItemRepository;

    private final OperationalItemMapper operationalItemMapper;
    private final WebClient webClient;
    private final ApplicationProperties appProperties;
    private final LocationRepository locationRepository;

    public OperationalItemServiceImpl(OperationalItemRepository operationalItemRepository,
                                      OperationalItemMapper operationalItemMapper,
                                      WebClient webClient, ApplicationProperties appProperties,
                                      LocationRepository locationRepository) {
        this.operationalItemRepository = operationalItemRepository;
        this.operationalItemMapper = operationalItemMapper;
        this.webClient = webClient;
        this.appProperties = appProperties;
        this.locationRepository = locationRepository;
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

    @Override
    public Mono<Void> download(){
        log.info("downloading locations");
        webClient.get().uri(appProperties.getEzOperationsBaseUrl() + "/operational/actions")
            .header("Authorization", appProperties.getEzOperationsBearerToken())
            .header("Cookie", "XSRF-TOKEN=" + appProperties.getEzOperationsXsrfToken())
            .retrieve()
            .bodyToMono(OperationalItemResponse.class)
            .doOnSuccess(res -> log.info("response: {}", res))
            .doOnError(error -> log.error(error.getMessage()))
            .map(res -> handleOperationalItemResponse(res, OperationalItemTypes.OPERATIONAL_ACTION))
            .subscribe();

        return webClient.get().uri(appProperties.getEzOperationsBaseUrl() + "/operational/alarms")
            .header("Authorization", appProperties.getEzOperationsBearerToken())
            .header("Cookie", "XSRF-TOKEN=" + appProperties.getEzOperationsXsrfToken())
            .retrieve()
            .bodyToMono(OperationalItemResponse.class)
            .doOnSuccess(res -> log.info("response: {}", res))
            .doOnError(error -> log.error(error.getMessage()))
            .flatMap(res -> handleOperationalItemResponse(res, OperationalItemTypes.OPERATIONAL_ALARM));
//            .subscribe();
    }

    private Mono<Void> handleOperationalItemResponse(OperationalItemResponse response,
                                                     OperationalItemTypes type){
        response.getData()
            .forEach(data -> {
                Optional<OperationalItem> opItem = operationalItemRepository.findByEzopsId(data.getId());
                if(opItem.isEmpty()){
                    OperationalItem newItem = new OperationalItem()
                        .ezopsId(data.getId())
                        .type(type)
                        .priorityScore(AppUtils.parseStringToDouble(data.getPriorityScore()))
                        .dueDate(data.getDueDate())
                        .location(locationRepository.findByEzopsId(data.getLocationableId())
                            .orElse(null));

                    operationalItemRepository.save(newItem);
                }
            });
        return Mono.empty();
    }
}
