package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerWithFilterMapperTest {

    private CustomerWithFilterMapper customerWithFilterMapper;

    @BeforeEach
    public void setUp() {
        customerWithFilterMapper = new CustomerWithFilterMapperImpl();
    }
}
