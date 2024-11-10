package com.wailo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OperationalItemMapperTest {

    private OperationalItemMapper operationalItemMapper;

    @BeforeEach
    public void setUp() {
        operationalItemMapper = new OperationalItemMapperImpl();
    }
}
