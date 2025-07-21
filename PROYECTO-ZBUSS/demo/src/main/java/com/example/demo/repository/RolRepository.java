package com.example.demo.repository;

import com.example.demo.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // El método findById ya es proporcionado por JpaRepository, no es necesario sobrescribirlo
}
