package com.wailo.web.rest;

import com.wailo.service.LocationService;
import com.wailo.service.OperationalItemService;
import com.wailo.service.PadService;
import com.wailo.service.impl.LocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeResource {
    private final LocationService locationService;
    private final PadService padService;
    private final OperationalItemService operationalItemService;
    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    @GetMapping("/downloadEntities")
    public ResponseEntity<Void> downloadEntities(){
        log.info("received request to download entities");
        padService.download().subscribe();
        locationService.download().subscribe();
        operationalItemService.download().subscribe();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
