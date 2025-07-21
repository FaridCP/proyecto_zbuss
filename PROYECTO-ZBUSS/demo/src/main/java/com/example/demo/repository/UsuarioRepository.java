package com.example.demo.repository;

import com.example.demo.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

        Usuario findByCorreo(String correo);
        List<Usuario> findAllByOrderByIdUsuarioAsc();

}
