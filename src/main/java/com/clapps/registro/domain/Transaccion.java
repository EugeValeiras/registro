package com.clapps.registro.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.clapps.registro.domain.enumeration.TipoDeDinero;

import com.clapps.registro.domain.enumeration.TipoDeTransaccion;

/**
 * A Transaccion.
 */
@Entity
@Table(name = "transaccion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cantidad")
    private Long cantidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_dinero")
    private TipoDeDinero tipoDeDinero;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_transaccion")
    private TipoDeTransaccion tipoDeTransaccion;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public TipoDeDinero getTipoDeDinero() {
        return tipoDeDinero;
    }

    public void setTipoDeDinero(TipoDeDinero tipoDeDinero) {
        this.tipoDeDinero = tipoDeDinero;
    }

    public TipoDeTransaccion getTipoDeTransaccion() {
        return tipoDeTransaccion;
    }

    public void setTipoDeTransaccion(TipoDeTransaccion tipoDeTransaccion) {
        this.tipoDeTransaccion = tipoDeTransaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaccion transaccion = (Transaccion) o;
        if(transaccion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transaccion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Transaccion{" +
            "id=" + id +
            ", cantidad='" + cantidad + "'" +
            ", tipoDeDinero='" + tipoDeDinero + "'" +
            ", tipoDeTransaccion='" + tipoDeTransaccion + "'" +
            ", descripcion='" + descripcion + "'" +
            ", date='" + date + "'" +
            '}';
    }
}
