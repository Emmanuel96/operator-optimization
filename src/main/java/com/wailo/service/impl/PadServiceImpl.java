package com.wailo.service.impl;

import com.wailo.config.ApplicationProperties;
import com.wailo.domain.Pad;
import com.wailo.models.ezops.responses.PadResponse;
import com.wailo.repository.PadRepository;
import com.wailo.service.PadService;
import com.wailo.service.dto.PadDTO;
import com.wailo.service.mapper.PadMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Pad}.
 */
@Service
@Transactional
public class PadServiceImpl implements PadService {

    private final Logger log = LoggerFactory.getLogger(PadServiceImpl.class);

    private final PadRepository padRepository;

    private final PadMapper padMapper;
    private final WebClient webClient;
    private final ApplicationProperties appProperties;

    public PadServiceImpl(PadRepository padRepository, PadMapper padMapper, WebClient webClient,
                          ApplicationProperties appProperties) {
        this.padRepository = padRepository;
        this.padMapper = padMapper;
        this.webClient = webClient;
        this.appProperties = appProperties;
    }

    @Override
    public PadDTO save(PadDTO padDTO) {
        log.debug("Request to save Pad : {}", padDTO);
        Pad pad = padMapper.toEntity(padDTO);
        pad = padRepository.save(pad);
        return padMapper.toDto(pad);
    }

    @Override
    public PadDTO update(PadDTO padDTO) {
        log.debug("Request to update Pad : {}", padDTO);
        Pad pad = padMapper.toEntity(padDTO);
        pad = padRepository.save(pad);
        return padMapper.toDto(pad);
    }

    @Override
    public Optional<PadDTO> partialUpdate(PadDTO padDTO) {
        log.debug("Request to partially update Pad : {}", padDTO);

        return padRepository
            .findById(padDTO.getId())
            .map(existingPad -> {
                padMapper.partialUpdate(existingPad, padDTO);

                return existingPad;
            })
            .map(padRepository::save)
            .map(padMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PadDTO> findAll() {
        log.debug("Request to get all Pads");
        return padRepository.findAll().stream().map(padMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PadDTO> findOne(Long id) {
        log.debug("Request to get Pad : {}", id);
        return padRepository.findById(id).map(padMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pad : {}", id);
        padRepository.deleteById(id);
    }

    @Override
    public Mono<Void> download(){
        log.info("downloading pads");
        return webClient.get().uri(appProperties.getEzOperationsBaseUrl() + "/pads")
            .header("Authorization", appProperties.getEzOperationsBearerToken())
            .header("Cookie", "XSRF-TOKEN=" + appProperties.getEzOperationsXsrfToken())
            .retrieve()
            .bodyToMono(PadResponse.class)
            .doOnSuccess(res -> log.info("response: {}", res))
            .doOnError(error -> log.error(error.getMessage()))
            .flatMap(this::handlePadResponse);
    }

    private Mono<Void> handlePadResponse(PadResponse response){
        response.getData()
            .forEach(data -> {
                Optional<Pad> pad = padRepository.findByEzopsId(data.getId());
                if(pad.isEmpty()){
                    Pad newLocation = new Pad()
                        .ezopsId(data.getId())
                        .name(data.getAlias());

                    padRepository.save(newLocation);
                }
            });
        return Mono.empty();
    }
}
