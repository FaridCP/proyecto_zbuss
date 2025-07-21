package com.example.demo.controller.admin;

import com.example.demo.model.Empresa;
import com.example.demo.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/empresas")
public class AdminEmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public String listarEmpresas(Model model) {
        List<Empresa> empresas = empresaService.listarTodas();
        model.addAttribute("empresas", empresas);
        return "admin/empresas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "admin/empresas/formulario";
    }

    @PostMapping("/guardar")
    public String guardarEmpresa(@ModelAttribute("empresa") Empresa empresa) {
        empresaService.guardar(empresa);  // Ya evita duplicados internamente
        return "redirect:/admin/empresas";
    }

    
}
