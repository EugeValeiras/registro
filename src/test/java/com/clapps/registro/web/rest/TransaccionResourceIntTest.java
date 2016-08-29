package com.clapps.registro.web.rest;

import com.clapps.registro.RegistroApp;
import com.clapps.registro.domain.Transaccion;
import com.clapps.registro.repository.TransaccionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.clapps.registro.domain.enumeration.TipoDeDinero;
import com.clapps.registro.domain.enumeration.TipoDeTransaccion;

/**
 * Test class for the TransaccionResource REST controller.
 *
 * @see TransaccionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RegistroApp.class)
@WebAppConfiguration
@IntegrationTest
public class TransaccionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final TipoDeDinero DEFAULT_TIPO_DE_DINERO = TipoDeDinero.PESO_ARGENTINO;
    private static final TipoDeDinero UPDATED_TIPO_DE_DINERO = TipoDeDinero.PESO_URUGUAYO;

    private static final TipoDeTransaccion DEFAULT_TIPO_DE_TRANSACCION = TipoDeTransaccion.EGRESO;
    private static final TipoDeTransaccion UPDATED_TIPO_DE_TRANSACCION = TipoDeTransaccion.INGRESO;
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    @Inject
    private TransaccionRepository transaccionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTransaccionMockMvc;

    private Transaccion transaccion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransaccionResource transaccionResource = new TransaccionResource();
        ReflectionTestUtils.setField(transaccionResource, "transaccionRepository", transaccionRepository);
        this.restTransaccionMockMvc = MockMvcBuilders.standaloneSetup(transaccionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        transaccion = new Transaccion();
        transaccion.setCantidad(DEFAULT_CANTIDAD);
        transaccion.setTipoDeDinero(DEFAULT_TIPO_DE_DINERO);
        transaccion.setTipoDeTransaccion(DEFAULT_TIPO_DE_TRANSACCION);
        transaccion.setDescripcion(DEFAULT_DESCRIPCION);
        transaccion.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createTransaccion() throws Exception {
        int databaseSizeBeforeCreate = transaccionRepository.findAll().size();

        // Create the Transaccion

        restTransaccionMockMvc.perform(post("/api/transaccions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transaccion)))
                .andExpect(status().isCreated());

        // Validate the Transaccion in the database
        List<Transaccion> transaccions = transaccionRepository.findAll();
        assertThat(transaccions).hasSize(databaseSizeBeforeCreate + 1);
        Transaccion testTransaccion = transaccions.get(transaccions.size() - 1);
        assertThat(testTransaccion.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testTransaccion.getTipoDeDinero()).isEqualTo(DEFAULT_TIPO_DE_DINERO);
        assertThat(testTransaccion.getTipoDeTransaccion()).isEqualTo(DEFAULT_TIPO_DE_TRANSACCION);
        assertThat(testTransaccion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testTransaccion.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setDate(null);

        // Create the Transaccion, which fails.

        restTransaccionMockMvc.perform(post("/api/transaccions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transaccion)))
                .andExpect(status().isBadRequest());

        List<Transaccion> transaccions = transaccionRepository.findAll();
        assertThat(transaccions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransaccions() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        // Get all the transaccions
        restTransaccionMockMvc.perform(get("/api/transaccions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transaccion.getId().intValue())))
                .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
                .andExpect(jsonPath("$.[*].tipoDeDinero").value(hasItem(DEFAULT_TIPO_DE_DINERO.toString())))
                .andExpect(jsonPath("$.[*].tipoDeTransaccion").value(hasItem(DEFAULT_TIPO_DE_TRANSACCION.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }

    @Test
    @Transactional
    public void getTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        // Get the transaccion
        restTransaccionMockMvc.perform(get("/api/transaccions/{id}", transaccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(transaccion.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.tipoDeDinero").value(DEFAULT_TIPO_DE_DINERO.toString()))
            .andExpect(jsonPath("$.tipoDeTransaccion").value(DEFAULT_TIPO_DE_TRANSACCION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingTransaccion() throws Exception {
        // Get the transaccion
        restTransaccionMockMvc.perform(get("/api/transaccions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();

        // Update the transaccion
        Transaccion updatedTransaccion = new Transaccion();
        updatedTransaccion.setId(transaccion.getId());
        updatedTransaccion.setCantidad(UPDATED_CANTIDAD);
        updatedTransaccion.setTipoDeDinero(UPDATED_TIPO_DE_DINERO);
        updatedTransaccion.setTipoDeTransaccion(UPDATED_TIPO_DE_TRANSACCION);
        updatedTransaccion.setDescripcion(UPDATED_DESCRIPCION);
        updatedTransaccion.setDate(UPDATED_DATE);

        restTransaccionMockMvc.perform(put("/api/transaccions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTransaccion)))
                .andExpect(status().isOk());

        // Validate the Transaccion in the database
        List<Transaccion> transaccions = transaccionRepository.findAll();
        assertThat(transaccions).hasSize(databaseSizeBeforeUpdate);
        Transaccion testTransaccion = transaccions.get(transaccions.size() - 1);
        assertThat(testTransaccion.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testTransaccion.getTipoDeDinero()).isEqualTo(UPDATED_TIPO_DE_DINERO);
        assertThat(testTransaccion.getTipoDeTransaccion()).isEqualTo(UPDATED_TIPO_DE_TRANSACCION);
        assertThat(testTransaccion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testTransaccion.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);
        int databaseSizeBeforeDelete = transaccionRepository.findAll().size();

        // Get the transaccion
        restTransaccionMockMvc.perform(delete("/api/transaccions/{id}", transaccion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Transaccion> transaccions = transaccionRepository.findAll();
        assertThat(transaccions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
