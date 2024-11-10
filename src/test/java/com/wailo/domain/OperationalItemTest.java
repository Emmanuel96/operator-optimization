package com.wailo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wailo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperationalItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OperationalItem.class);
        OperationalItem operationalItem1 = new OperationalItem();
        operationalItem1.setId(1L);
        OperationalItem operationalItem2 = new OperationalItem();
        operationalItem2.setId(operationalItem1.getId());
        assertThat(operationalItem1).isEqualTo(operationalItem2);
        operationalItem2.setId(2L);
        assertThat(operationalItem1).isNotEqualTo(operationalItem2);
        operationalItem1.setId(null);
        assertThat(operationalItem1).isNotEqualTo(operationalItem2);
    }
}
