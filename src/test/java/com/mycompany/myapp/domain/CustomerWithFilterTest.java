package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerWithFilterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerWithFilter.class);
        CustomerWithFilter customerWithFilter1 = new CustomerWithFilter();
        customerWithFilter1.setId(1L);
        CustomerWithFilter customerWithFilter2 = new CustomerWithFilter();
        customerWithFilter2.setId(customerWithFilter1.getId());
        assertThat(customerWithFilter1).isEqualTo(customerWithFilter2);
        customerWithFilter2.setId(2L);
        assertThat(customerWithFilter1).isNotEqualTo(customerWithFilter2);
        customerWithFilter1.setId(null);
        assertThat(customerWithFilter1).isNotEqualTo(customerWithFilter2);
    }
}
