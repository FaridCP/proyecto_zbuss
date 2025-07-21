package com.example.demo.repository;

import com.example.demo.model.Pasajero;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {

    List<Pasajero> findByUsuario(Usuario usuario);

    @Query("SELECT p FROM Pasajero p WHERE " +
       "(:dni IS NULL OR LOWER(p.dni) LIKE %:dni%) AND " +
       "(:fechaNacimiento IS NULL OR p.fechaNacimiento = :fechaNacimiento) AND " +
       "(:correo IS NULL OR LOWER(p.usuario.correo) LIKE %:correo%)")
    List<Pasajero> buscarConFiltros(@Param("dni") String dni,
                                 @Param("fechaNacimiento") LocalDate fechaNacimiento,
                                 @Param("correo") String correo);


}
