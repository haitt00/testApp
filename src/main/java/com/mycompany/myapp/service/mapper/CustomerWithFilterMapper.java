package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.CustomerWithFilter;
import com.mycompany.myapp.service.dto.CustomerWithFilterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerWithFilter} and its DTO {@link CustomerWithFilterDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerWithFilterMapper extends EntityMapper<CustomerWithFilterDTO, CustomerWithFilter> {}
