package com.wailo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wailo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperationalItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationalItemDTO.class);
        OperationalItemDTO operationalItemDTO1 = new OperationalItemDTO();
        operationalItemDTO1.setId(1L);
        OperationalItemDTO operationalItemDTO2 = new OperationalItemDTO();
        assertThat(operationalItemDTO1).isNotEqualTo(operationalItemDTO2);
        operationalItemDTO2.setId(operationalItemDTO1.getId());
        assertThat(operationalItemDTO1).isEqualTo(operationalItemDTO2);
        operationalItemDTO2.setId(2L);
        assertThat(operationalItemDTO1).isNotEqualTo(operationalItemDTO2);
        operationalItemDTO1.setId(null);
        assertThat(operationalItemDTO1).isNotEqualTo(operationalItemDTO2);
    }
}
