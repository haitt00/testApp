package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CustomerWithFilterRepository;
import com.mycompany.myapp.service.CustomerWithFilterQueryService;
import com.mycompany.myapp.service.CustomerWithFilterService;
import com.mycompany.myapp.service.criteria.CustomerWithFilterCriteria;
import com.mycompany.myapp.service.dto.CustomerWithFilterDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.CustomerWithFilter}.
 */
@RestController
@RequestMapping("/api")
public class CustomerWithFilterResource {

    private final Logger log = LoggerFactory.getLogger(CustomerWithFilterResource.class);

    private static final String ENTITY_NAME = "customerWithFilter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerWithFilterService customerWithFilterService;

    private final CustomerWithFilterRepository customerWithFilterRepository;

    private final CustomerWithFilterQueryService customerWithFilterQueryService;

    public CustomerWithFilterResource(
        CustomerWithFilterService customerWithFilterService,
        CustomerWithFilterRepository customerWithFilterRepository,
        CustomerWithFilterQueryService customerWithFilterQueryService
    ) {
        this.customerWithFilterService = customerWithFilterService;
        this.customerWithFilterRepository = customerWithFilterRepository;
        this.customerWithFilterQueryService = customerWithFilterQueryService;
    }

    /**
     * {@code POST  /customer-with-filters} : Create a new customerWithFilter.
     *
     * @param customerWithFilterDTO the customerWithFilterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerWithFilterDTO, or with status {@code 400 (Bad Request)} if the customerWithFilter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-with-filters")
    public ResponseEntity<CustomerWithFilterDTO> createCustomerWithFilter(@RequestBody CustomerWithFilterDTO customerWithFilterDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerWithFilter : {}", customerWithFilterDTO);
        if (customerWithFilterDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerWithFilter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerWithFilterDTO result = customerWithFilterService.save(customerWithFilterDTO);
        return ResponseEntity
            .created(new URI("/api/customer-with-filters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-with-filters/:id} : Updates an existing customerWithFilter.
     *
     * @param id the id of the customerWithFilterDTO to save.
     * @param customerWithFilterDTO the customerWithFilterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerWithFilterDTO,
     * or with status {@code 400 (Bad Request)} if the customerWithFilterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerWithFilterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-with-filters/{id}")
    public ResponseEntity<CustomerWithFilterDTO> updateCustomerWithFilter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomerWithFilterDTO customerWithFilterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerWithFilter : {}, {}", id, customerWithFilterDTO);
        if (customerWithFilterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerWithFilterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerWithFilterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerWithFilterDTO result = customerWithFilterService.update(customerWithFilterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerWithFilterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-with-filters/:id} : Partial updates given fields of an existing customerWithFilter, field will ignore if it is null
     *
     * @param id the id of the customerWithFilterDTO to save.
     * @param customerWithFilterDTO the customerWithFilterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerWithFilterDTO,
     * or with status {@code 400 (Bad Request)} if the customerWithFilterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerWithFilterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerWithFilterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-with-filters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerWithFilterDTO> partialUpdateCustomerWithFilter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomerWithFilterDTO customerWithFilterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerWithFilter partially : {}, {}", id, customerWithFilterDTO);
        if (customerWithFilterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerWithFilterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerWithFilterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerWithFilterDTO> result = customerWithFilterService.partialUpdate(customerWithFilterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerWithFilterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-with-filters} : get all the customerWithFilters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerWithFilters in body.
     */
    @GetMapping("/customer-with-filters")
    public ResponseEntity<List<CustomerWithFilterDTO>> getAllCustomerWithFilters(
        CustomerWithFilterCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomerWithFilters by criteria: {}", criteria);
        Page<CustomerWithFilterDTO> page = customerWithFilterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-with-filters/count} : count all the customerWithFilters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-with-filters/count")
    public ResponseEntity<Long> countCustomerWithFilters(CustomerWithFilterCriteria criteria) {
        log.debug("REST request to count CustomerWithFilters by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerWithFilterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-with-filters/:id} : get the "id" customerWithFilter.
     *
     * @param id the id of the customerWithFilterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerWithFilterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-with-filters/{id}")
    public ResponseEntity<CustomerWithFilterDTO> getCustomerWithFilter(@PathVariable Long id) {
        log.debug("REST request to get CustomerWithFilter : {}", id);
        Optional<CustomerWithFilterDTO> customerWithFilterDTO = customerWithFilterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerWithFilterDTO);
    }

    /**
     * {@code DELETE  /customer-with-filters/:id} : delete the "id" customerWithFilter.
     *
     * @param id the id of the customerWithFilterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-with-filters/{id}")
    public ResponseEntity<Void> deleteCustomerWithFilter(@PathVariable Long id) {
        log.debug("REST request to delete CustomerWithFilter : {}", id);
        customerWithFilterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
