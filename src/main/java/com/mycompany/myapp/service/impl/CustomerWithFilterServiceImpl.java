package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CustomerWithFilter;
import com.mycompany.myapp.repository.CustomerWithFilterRepository;
import com.mycompany.myapp.service.CustomerWithFilterService;
import com.mycompany.myapp.service.dto.CustomerWithFilterDTO;
import com.mycompany.myapp.service.mapper.CustomerWithFilterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomerWithFilter}.
 */
@Service
@Transactional
public class CustomerWithFilterServiceImpl implements CustomerWithFilterService {

    private final Logger log = LoggerFactory.getLogger(CustomerWithFilterServiceImpl.class);

    private final CustomerWithFilterRepository customerWithFilterRepository;

    private final CustomerWithFilterMapper customerWithFilterMapper;

    public CustomerWithFilterServiceImpl(
        CustomerWithFilterRepository customerWithFilterRepository,
        CustomerWithFilterMapper customerWithFilterMapper
    ) {
        this.customerWithFilterRepository = customerWithFilterRepository;
        this.customerWithFilterMapper = customerWithFilterMapper;
    }

    @Override
    public CustomerWithFilterDTO save(CustomerWithFilterDTO customerWithFilterDTO) {
        log.debug("Request to save CustomerWithFilter : {}", customerWithFilterDTO);
        CustomerWithFilter customerWithFilter = customerWithFilterMapper.toEntity(customerWithFilterDTO);
        customerWithFilter = customerWithFilterRepository.save(customerWithFilter);
        return customerWithFilterMapper.toDto(customerWithFilter);
    }

    @Override
    public CustomerWithFilterDTO update(CustomerWithFilterDTO customerWithFilterDTO) {
        log.debug("Request to update CustomerWithFilter : {}", customerWithFilterDTO);
        CustomerWithFilter customerWithFilter = customerWithFilterMapper.toEntity(customerWithFilterDTO);
        customerWithFilter = customerWithFilterRepository.save(customerWithFilter);
        return customerWithFilterMapper.toDto(customerWithFilter);
    }

    @Override
    public Optional<CustomerWithFilterDTO> partialUpdate(CustomerWithFilterDTO customerWithFilterDTO) {
        log.debug("Request to partially update CustomerWithFilter : {}", customerWithFilterDTO);

        return customerWithFilterRepository
            .findById(customerWithFilterDTO.getId())
            .map(existingCustomerWithFilter -> {
                customerWithFilterMapper.partialUpdate(existingCustomerWithFilter, customerWithFilterDTO);

                return existingCustomerWithFilter;
            })
            .map(customerWithFilterRepository::save)
            .map(customerWithFilterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerWithFilterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerWithFilters");
        return customerWithFilterRepository.findAll(pageable).map(customerWithFilterMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerWithFilterDTO> findOne(Long id) {
        log.debug("Request to get CustomerWithFilter : {}", id);
        return customerWithFilterRepository.findById(id).map(customerWithFilterMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerWithFilter : {}", id);
        customerWithFilterRepository.deleteById(id);
    }
}
