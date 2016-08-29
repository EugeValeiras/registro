package com.clapps.registro.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.clapps.registro.domain.Transaccion;
import com.clapps.registro.repository.TransaccionRepository;
import com.clapps.registro.security.SecurityUtils;
import com.clapps.registro.service.UserService;
import com.clapps.registro.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Transaccion.
 */
@RestController
@RequestMapping("/api")
public class TransaccionResource {

    private final Logger log = LoggerFactory.getLogger(TransaccionResource.class);
        
    @Inject
    private TransaccionRepository transaccionRepository;

    @Inject
    private UserService userService;
    
    /**
     * POST  /transaccions : Create a new transaccion.
     *
     * @param transaccion the transaccion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transaccion, or with status 400 (Bad Request) if the transaccion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/transaccions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Transaccion> createTransaccion(@Valid @RequestBody Transaccion transaccion) throws URISyntaxException {
        log.debug("REST request to save Transaccion : {}", transaccion);
        if (transaccion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("transaccion", "idexists", "A new transaccion cannot already have an ID")).body(null);
        }
        Transaccion result = transaccionRepository.save(transaccion);
        return ResponseEntity.created(new URI("/api/transaccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("transaccion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaccions : Updates an existing transaccion.
     *
     * @param transaccion the transaccion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transaccion,
     * or with status 400 (Bad Request) if the transaccion is not valid,
     * or with status 500 (Internal Server Error) if the transaccion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/transaccions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Transaccion> updateTransaccion(@Valid @RequestBody Transaccion transaccion) throws URISyntaxException {
        log.debug("REST request to update Transaccion : {}", transaccion);
        if (transaccion.getId() == null) {
            return createTransaccion(transaccion);
        }
        Transaccion result = transaccionRepository.save(transaccion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("transaccion", transaccion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaccions : get all the transaccions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transaccions in body
     */
    @RequestMapping(value = "/transaccions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Transaccion> getAllTransaccions() {
        log.debug("REST request to get all Transaccions");
        List<Transaccion> transaccions = transaccionRepository.findAll();
        return transaccions;
    }

    /**
     * GET  /transaccions/:id : get the "id" transaccion.
     *
     * @param id the id of the transaccion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transaccion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/transaccions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Transaccion> getTransaccion(@PathVariable Long id) {
        log.debug("REST request to get Transaccion : {}", id);
        Transaccion transaccion = transaccionRepository.findOne(id);
        return Optional.ofNullable(transaccion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /transaccions/:id : delete the "id" transaccion.
     *
     * @param id the id of the transaccion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/transaccions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTransaccion(@PathVariable Long id) {
        log.debug("REST request to delete Transaccion : {}", id);
        transaccionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("transaccion", id.toString())).build();
    }

    /**
     * GET  /transaccions : get all the transaccions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transaccions in body
     */
    @RequestMapping(value = "/logged/transaccions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Transaccion> getLoggedTransaccions() {
        log.debug("REST request to get all Transaccions");
        List<Transaccion> transaccions = transaccionRepository.findAll().stream().filter(t -> t.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin())).collect(Collectors.toList());
        return transaccions;
    }
    
    /**
     * POST  /transaccions : Create a new transaccion.
     *
     * @param transaccion the transaccion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transaccion, or with status 400 (Bad Request) if the transaccion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/logged/transaccions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Transaccion> createTransaccionForLogged(@RequestBody Transaccion transaccion) throws URISyntaxException {
        log.debug("REST request to save Transaccion : {}", transaccion);
        transaccion.setUser(userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).get());
        
        if (transaccion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("transaccion", "idexists", "A new transaccion cannot already have an ID")).body(null);
        }
        Transaccion result = transaccionRepository.save(transaccion);
        return ResponseEntity.created(new URI("/api/transaccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("transaccion", result.getId().toString()))
            .body(result);
    }

}
