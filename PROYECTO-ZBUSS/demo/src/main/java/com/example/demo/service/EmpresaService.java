package com.example.demo.service;

import com.example.demo.model.Empresa;
import com.example.demo.repository.EmpresaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public Empresa buscarPorRuc(String ruc) {
        return empresaRepository.findByRuc(ruc);
    }

    public Empresa guardar(Empresa empresa) {
        Empresa existente = buscarPorRuc(empresa.getRuc());
        if (existente != null) {
            return existente;
        }
        return empresaRepository.save(empresa);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public long contarTodos() {
        return empresaRepository.count();
    }

}
