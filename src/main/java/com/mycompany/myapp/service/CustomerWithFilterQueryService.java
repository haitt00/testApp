package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.CustomerWithFilter;
import com.mycompany.myapp.repository.CustomerWithFilterRepository;
import com.mycompany.myapp.service.criteria.CustomerWithFilterCriteria;
import com.mycompany.myapp.service.dto.CustomerWithFilterDTO;
import com.mycompany.myapp.service.mapper.CustomerWithFilterMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CustomerWithFilter} entities in the database.
 * The main input is a {@link CustomerWithFilterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerWithFilterDTO} or a {@link Page} of {@link CustomerWithFilterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerWithFilterQueryService extends QueryService<CustomerWithFilter> {

    private final Logger log = LoggerFactory.getLogger(CustomerWithFilterQueryService.class);

    private final CustomerWithFilterRepository customerWithFilterRepository;

    private final CustomerWithFilterMapper customerWithFilterMapper;

    public CustomerWithFilterQueryService(
        CustomerWithFilterRepository customerWithFilterRepository,
        CustomerWithFilterMapper customerWithFilterMapper
    ) {
        this.customerWithFilterRepository = customerWithFilterRepository;
        this.customerWithFilterMapper = customerWithFilterMapper;
    }

    /**
     * Return a {@link List} of {@link CustomerWithFilterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerWithFilterDTO> findByCriteria(CustomerWithFilterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerWithFilter> specification = createSpecification(criteria);
        return customerWithFilterMapper.toDto(customerWithFilterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerWithFilterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerWithFilterDTO> findByCriteria(CustomerWithFilterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerWithFilter> specification = createSpecification(criteria);
        return customerWithFilterRepository.findAll(specification, page).map(customerWithFilterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerWithFilterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerWithFilter> specification = createSpecification(criteria);
        return customerWithFilterRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerWithFilterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerWithFilter> createSpecification(CustomerWithFilterCriteria criteria) {
        Specification<CustomerWithFilter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerWithFilter_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CustomerWithFilter_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), CustomerWithFilter_.address));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), CustomerWithFilter_.phone));
            }
        }
        return specification;
    }
}
