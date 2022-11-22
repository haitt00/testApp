package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CustomerWithFilter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerWithFilter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerWithFilterRepository
    extends JpaRepository<CustomerWithFilter, Long>, JpaSpecificationExecutor<CustomerWithFilter> {}
