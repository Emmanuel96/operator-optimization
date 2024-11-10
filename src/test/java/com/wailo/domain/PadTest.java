package com.wailo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wailo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pad.class);
        Pad pad1 = new Pad();
        pad1.setId(1L);
        Pad pad2 = new Pad();
        pad2.setId(pad1.getId());
        assertThat(pad1).isEqualTo(pad2);
        pad2.setId(2L);
        assertThat(pad1).isNotEqualTo(pad2);
        pad1.setId(null);
        assertThat(pad1).isNotEqualTo(pad2);
    }
}
