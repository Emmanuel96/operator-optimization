package com.wailo.service.impl;

import com.wailo.config.ApplicationProperties;
import com.wailo.domain.Location;
import com.wailo.domain.enumeration.LocationType;
import com.wailo.models.ezops.responses.LocationResponse;
import com.wailo.repository.LocationRepository;
import com.wailo.repository.PadRepository;
import com.wailo.service.LocationService;
import com.wailo.service.dto.LocationDTO;
import com.wailo.service.mapper.LocationMapper;
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
 * Service Implementation for managing {@link Location}.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;
    private final PadRepository padRepository;

    private final LocationMapper locationMapper;
    private final WebClient webClient;
    private final ApplicationProperties appProperties;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper,
                               WebClient webclient, ApplicationProperties appProperties,
                               PadRepository padRepository) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.webClient = webclient;
        this.appProperties = appProperties;
        this.padRepository = padRepository;
    }

    @Override
    public LocationDTO save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public LocationDTO update(LocationDTO locationDTO) {
        log.debug("Request to update Location : {}", locationDTO);
        Location location = locationMapper.toEntity(locationDTO);
        location = locationRepository.save(location);
        return locationMapper.toDto(location);
    }

    @Override
    public Optional<LocationDTO> partialUpdate(LocationDTO locationDTO) {
        log.debug("Request to partially update Location : {}", locationDTO);

        return locationRepository
            .findById(locationDTO.getId())
            .map(existingLocation -> {
                locationMapper.partialUpdate(existingLocation, locationDTO);

                return existingLocation;
            })
            .map(locationRepository::save)
            .map(locationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDTO> findAll() {
        log.debug("Request to get all Locations");
        return locationRepository.findAll().stream().map(locationMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LocationDTO> findOne(Long id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id).map(locationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Location : {}", id);
        locationRepository.deleteById(id);
    }

    @Override
    public Mono<Void> download(){
        log.info("downloading locations");
        return webClient.get().uri(appProperties.getEzOperationsBaseUrl() + "/field_locations")
            .header("Authorization", appProperties.getEzOperationsBearerToken())
            .header("Cookie", "XSRF-TOKEN=" + appProperties.getEzOperationsXsrfToken())
            .retrieve()
            .bodyToMono(LocationResponse.class)
            .doOnSuccess(res -> log.info("response: {}", res))
            .doOnError(error -> log.error(error.getMessage()))
            .flatMap(this::handleLocationResponse);
    }

    private Mono<Void> handleLocationResponse(LocationResponse locationResponse){
        locationResponse.getData()
            .forEach(data -> {
                Optional<Location> location = locationRepository.findByEzopsId(data.getId());
                if(location.isEmpty()){
                    Location newLocation = new Location()
                        .ezopsId(data.getId())
                        .name(data.getName())
                        .uwi(data.getUwi())
                        .latitude(AppUtils.parseStringToFloat(data.getSurfaceLatitude()))
                        .longitude(AppUtils.parseStringToFloat(data.getSurfaceLongitude()))
                        .type(getLocationType(data.getType()))
                        .pad(padRepository.findByEzopsId(data.getId()).orElse(null));

                    locationRepository.save(newLocation);
                }
            });
        return Mono.empty();
    }

    private LocationType getLocationType(String string){
        try{
            return LocationType.valueOf(string);
        }catch (IllegalArgumentException | NullPointerException e){
            return LocationType.WELL;
        }
    }
}
