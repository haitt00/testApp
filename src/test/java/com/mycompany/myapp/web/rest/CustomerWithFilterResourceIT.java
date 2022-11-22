package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CustomerWithFilter;
import com.mycompany.myapp.repository.CustomerWithFilterRepository;
import com.mycompany.myapp.service.criteria.CustomerWithFilterCriteria;
import com.mycompany.myapp.service.dto.CustomerWithFilterDTO;
import com.mycompany.myapp.service.mapper.CustomerWithFilterMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CustomerWithFilterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomerWithFilterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-with-filters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerWithFilterRepository customerWithFilterRepository;

    @Autowired
    private CustomerWithFilterMapper customerWithFilterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerWithFilterMockMvc;

    private CustomerWithFilter customerWithFilter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerWithFilter createEntity(EntityManager em) {
        CustomerWithFilter customerWithFilter = new CustomerWithFilter().name(DEFAULT_NAME).address(DEFAULT_ADDRESS).phone(DEFAULT_PHONE);
        return customerWithFilter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerWithFilter createUpdatedEntity(EntityManager em) {
        CustomerWithFilter customerWithFilter = new CustomerWithFilter().name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE);
        return customerWithFilter;
    }

    @BeforeEach
    public void initTest() {
        customerWithFilter = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomerWithFilter() throws Exception {
        int databaseSizeBeforeCreate = customerWithFilterRepository.findAll().size();
        // Create the CustomerWithFilter
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);
        restCustomerWithFilterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerWithFilter testCustomerWithFilter = customerWithFilterList.get(customerWithFilterList.size() - 1);
        assertThat(testCustomerWithFilter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomerWithFilter.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCustomerWithFilter.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createCustomerWithFilterWithExistingId() throws Exception {
        // Create the CustomerWithFilter with an existing ID
        customerWithFilter.setId(1L);
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);

        int databaseSizeBeforeCreate = customerWithFilterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerWithFilterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomerWithFilters() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList
        restCustomerWithFilterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerWithFilter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getCustomerWithFilter() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get the customerWithFilter
        restCustomerWithFilterMockMvc
            .perform(get(ENTITY_API_URL_ID, customerWithFilter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customerWithFilter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getCustomerWithFiltersByIdFiltering() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        Long id = customerWithFilter.getId();

        defaultCustomerWithFilterShouldBeFound("id.equals=" + id);
        defaultCustomerWithFilterShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerWithFilterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerWithFilterShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerWithFilterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerWithFilterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where name equals to DEFAULT_NAME
        defaultCustomerWithFilterShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the customerWithFilterList where name equals to UPDATED_NAME
        defaultCustomerWithFilterShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCustomerWithFilterShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the customerWithFilterList where name equals to UPDATED_NAME
        defaultCustomerWithFilterShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where name is not null
        defaultCustomerWithFilterShouldBeFound("name.specified=true");

        // Get all the customerWithFilterList where name is null
        defaultCustomerWithFilterShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByNameContainsSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where name contains DEFAULT_NAME
        defaultCustomerWithFilterShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the customerWithFilterList where name contains UPDATED_NAME
        defaultCustomerWithFilterShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where name does not contain DEFAULT_NAME
        defaultCustomerWithFilterShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the customerWithFilterList where name does not contain UPDATED_NAME
        defaultCustomerWithFilterShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where address equals to DEFAULT_ADDRESS
        defaultCustomerWithFilterShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the customerWithFilterList where address equals to UPDATED_ADDRESS
        defaultCustomerWithFilterShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCustomerWithFilterShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the customerWithFilterList where address equals to UPDATED_ADDRESS
        defaultCustomerWithFilterShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where address is not null
        defaultCustomerWithFilterShouldBeFound("address.specified=true");

        // Get all the customerWithFilterList where address is null
        defaultCustomerWithFilterShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByAddressContainsSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where address contains DEFAULT_ADDRESS
        defaultCustomerWithFilterShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the customerWithFilterList where address contains UPDATED_ADDRESS
        defaultCustomerWithFilterShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where address does not contain DEFAULT_ADDRESS
        defaultCustomerWithFilterShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the customerWithFilterList where address does not contain UPDATED_ADDRESS
        defaultCustomerWithFilterShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where phone equals to DEFAULT_PHONE
        defaultCustomerWithFilterShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the customerWithFilterList where phone equals to UPDATED_PHONE
        defaultCustomerWithFilterShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCustomerWithFilterShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the customerWithFilterList where phone equals to UPDATED_PHONE
        defaultCustomerWithFilterShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where phone is not null
        defaultCustomerWithFilterShouldBeFound("phone.specified=true");

        // Get all the customerWithFilterList where phone is null
        defaultCustomerWithFilterShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where phone contains DEFAULT_PHONE
        defaultCustomerWithFilterShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the customerWithFilterList where phone contains UPDATED_PHONE
        defaultCustomerWithFilterShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomerWithFiltersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        // Get all the customerWithFilterList where phone does not contain DEFAULT_PHONE
        defaultCustomerWithFilterShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the customerWithFilterList where phone does not contain UPDATED_PHONE
        defaultCustomerWithFilterShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerWithFilterShouldBeFound(String filter) throws Exception {
        restCustomerWithFilterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerWithFilter.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restCustomerWithFilterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerWithFilterShouldNotBeFound(String filter) throws Exception {
        restCustomerWithFilterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerWithFilterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomerWithFilter() throws Exception {
        // Get the customerWithFilter
        restCustomerWithFilterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomerWithFilter() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();

        // Update the customerWithFilter
        CustomerWithFilter updatedCustomerWithFilter = customerWithFilterRepository.findById(customerWithFilter.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerWithFilter are not directly saved in db
        em.detach(updatedCustomerWithFilter);
        updatedCustomerWithFilter.name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE);
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(updatedCustomerWithFilter);

        restCustomerWithFilterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerWithFilterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
        CustomerWithFilter testCustomerWithFilter = customerWithFilterList.get(customerWithFilterList.size() - 1);
        assertThat(testCustomerWithFilter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerWithFilter.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomerWithFilter.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingCustomerWithFilter() throws Exception {
        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();
        customerWithFilter.setId(count.incrementAndGet());

        // Create the CustomerWithFilter
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerWithFilterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customerWithFilterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomerWithFilter() throws Exception {
        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();
        customerWithFilter.setId(count.incrementAndGet());

        // Create the CustomerWithFilter
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWithFilterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomerWithFilter() throws Exception {
        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();
        customerWithFilter.setId(count.incrementAndGet());

        // Create the CustomerWithFilter
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWithFilterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithFilterWithPatch() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();

        // Update the customerWithFilter using partial update
        CustomerWithFilter partialUpdatedCustomerWithFilter = new CustomerWithFilter();
        partialUpdatedCustomerWithFilter.setId(customerWithFilter.getId());

        partialUpdatedCustomerWithFilter.name(UPDATED_NAME).address(UPDATED_ADDRESS);

        restCustomerWithFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerWithFilter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerWithFilter))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
        CustomerWithFilter testCustomerWithFilter = customerWithFilterList.get(customerWithFilterList.size() - 1);
        assertThat(testCustomerWithFilter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerWithFilter.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomerWithFilter.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithFilterWithPatch() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();

        // Update the customerWithFilter using partial update
        CustomerWithFilter partialUpdatedCustomerWithFilter = new CustomerWithFilter();
        partialUpdatedCustomerWithFilter.setId(customerWithFilter.getId());

        partialUpdatedCustomerWithFilter.name(UPDATED_NAME).address(UPDATED_ADDRESS).phone(UPDATED_PHONE);

        restCustomerWithFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomerWithFilter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerWithFilter))
            )
            .andExpect(status().isOk());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
        CustomerWithFilter testCustomerWithFilter = customerWithFilterList.get(customerWithFilterList.size() - 1);
        assertThat(testCustomerWithFilter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomerWithFilter.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCustomerWithFilter.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomerWithFilter() throws Exception {
        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();
        customerWithFilter.setId(count.incrementAndGet());

        // Create the CustomerWithFilter
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerWithFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customerWithFilterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomerWithFilter() throws Exception {
        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();
        customerWithFilter.setId(count.incrementAndGet());

        // Create the CustomerWithFilter
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWithFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomerWithFilter() throws Exception {
        int databaseSizeBeforeUpdate = customerWithFilterRepository.findAll().size();
        customerWithFilter.setId(count.incrementAndGet());

        // Create the CustomerWithFilter
        CustomerWithFilterDTO customerWithFilterDTO = customerWithFilterMapper.toDto(customerWithFilter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerWithFilterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customerWithFilterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomerWithFilter in the database
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomerWithFilter() throws Exception {
        // Initialize the database
        customerWithFilterRepository.saveAndFlush(customerWithFilter);

        int databaseSizeBeforeDelete = customerWithFilterRepository.findAll().size();

        // Delete the customerWithFilter
        restCustomerWithFilterMockMvc
            .perform(delete(ENTITY_API_URL_ID, customerWithFilter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomerWithFilter> customerWithFilterList = customerWithFilterRepository.findAll();
        assertThat(customerWithFilterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
