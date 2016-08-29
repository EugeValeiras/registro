package com.clapps.registro.repository;

import com.clapps.registro.domain.Transaccion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Transaccion entity.
 */
public interface TransaccionRepository extends JpaRepository<Transaccion,Long> {

    @Query("select transaccion from Transaccion transaccion where transaccion.user.login = ?#{principal.username}")
    List<Transaccion> findByUserIsCurrentUser();

}
