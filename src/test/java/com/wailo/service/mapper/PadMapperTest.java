package com.wailo.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PadMapperTest {

    private PadMapper padMapper;

    @BeforeEach
    public void setUp() {
        padMapper = new PadMapperImpl();
    }
}
