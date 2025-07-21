package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.model.Rol;
import com.example.demo.model.Empresa;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public Usuario registerUser(Usuario usuario) {
        Rol rolUsuario = rolRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rolUsuario);
        usuario.setActivo(true);

        if ("RUC".equalsIgnoreCase(usuario.getTipoDocumento().name())) {
            Empresa empresa = empresaRepository.findByRuc(usuario.getDocumento());
            if (empresa == null) {
                empresa = new Empresa();
                empresa.setRuc(usuario.getDocumento());
                empresa.setRazonSocial(usuario.getRazonSocial());
                empresaRepository.save(empresa);
            }
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario loginUser(String correo, String clave) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null && usuario.getClave().equals(clave)) {
            return usuario;
        }
        return null;
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void cambiarClave(Long idUsuario, String nuevaClave) {
        Optional<Usuario> optUsuario = usuarioRepository.findById(idUsuario);
        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            usuario.setClave(nuevaClave);
            usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAllByOrderByIdUsuarioAsc();
    }

    public long contarTodos() {
        return usuarioRepository.count();
    }

}
