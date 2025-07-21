package com.example.demo.service;

import com.example.demo.model.Asiento;
import com.example.demo.model.ReservaAsiento;
import com.example.demo.repository.AsientoRepository;
import com.example.demo.repository.ReservaAsientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AsientoService {

    @Autowired
    private AsientoRepository asientoRepo;

    @Autowired
    private ReservaAsientoRepository reservaRepo;

    @Autowired
    private RutaService rutaService;

    public List<Asiento> obtenerAsientosPorRuta(Long idRuta) {
        Long idBus = rutaService.getRutaById(idRuta).get().getBus().getIdBus();
        return asientoRepo.findByBus_IdBus(idBus);
    }

    public boolean estaReservado(Long idAsiento, Long idHorario) {
        return reservaRepo.existsByAsientoIdAsientoAndHorarioIdHorarioAndReservadoTrue(idAsiento, idHorario);
    }

    public List<Asiento> obtenerPorIds(List<Long> ids) {
        return asientoRepo.findAllById(ids);
    }

    public List<Asiento> obtenerPorNumeros(List<String> numeros, Long rutaId) {
        Long idBus = rutaService.getRutaById(rutaId).orElseThrow().getBus().getIdBus();
        return asientoRepo.findByBus_IdBusAndNumeroIn(idBus, numeros);
    }

    public List<Asiento> obtenerAsientosDisponibles(Long rutaId, Long horarioId) {
        Long idBus = rutaService.getRutaById(rutaId).orElseThrow().getBus().getIdBus();
        List<Asiento> todos = asientoRepo.findByBus_IdBus(idBus);

        return todos.stream()
            .filter(asiento -> !estaReservado(asiento.getIdAsiento(), horarioId))
            .toList();
    }

    public List<Asiento> obtenerAsientosConReservas(Long idRuta, Long idHorario) {
        List<Asiento> asientos = obtenerAsientosPorRuta(idRuta);
        Set<Long> ocupados = reservaRepo.findByHorarioIdHorario(idHorario)
                                        .stream()
                                        .filter(ReservaAsiento::getReservado)
                                        .map(ra -> ra.getAsiento().getIdAsiento())
                                        .collect(Collectors.toSet());

        for (Asiento a : asientos) {
            a.setOcupado(ocupados.contains(a.getIdAsiento()));
        }
        return asientos;
    }

}
