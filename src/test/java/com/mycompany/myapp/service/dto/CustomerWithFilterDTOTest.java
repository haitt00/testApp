package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerWithFilterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerWithFilterDTO.class);
        CustomerWithFilterDTO customerWithFilterDTO1 = new CustomerWithFilterDTO();
        customerWithFilterDTO1.setId(1L);
        CustomerWithFilterDTO customerWithFilterDTO2 = new CustomerWithFilterDTO();
        assertThat(customerWithFilterDTO1).isNotEqualTo(customerWithFilterDTO2);
        customerWithFilterDTO2.setId(customerWithFilterDTO1.getId());
        assertThat(customerWithFilterDTO1).isEqualTo(customerWithFilterDTO2);
        customerWithFilterDTO2.setId(2L);
        assertThat(customerWithFilterDTO1).isNotEqualTo(customerWithFilterDTO2);
        customerWithFilterDTO1.setId(null);
        assertThat(customerWithFilterDTO1).isNotEqualTo(customerWithFilterDTO2);
    }
}
