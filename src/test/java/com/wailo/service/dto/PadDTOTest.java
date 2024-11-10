package com.wailo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.wailo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PadDTO.class);
        PadDTO padDTO1 = new PadDTO();
        padDTO1.setId(1L);
        PadDTO padDTO2 = new PadDTO();
        assertThat(padDTO1).isNotEqualTo(padDTO2);
        padDTO2.setId(padDTO1.getId());
        assertThat(padDTO1).isEqualTo(padDTO2);
        padDTO2.setId(2L);
        assertThat(padDTO1).isNotEqualTo(padDTO2);
        padDTO1.setId(null);
        assertThat(padDTO1).isNotEqualTo(padDTO2);
    }
}
