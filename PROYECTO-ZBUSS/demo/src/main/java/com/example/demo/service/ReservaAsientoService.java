package com.example.demo.service;

import com.example.demo.model.Asiento;
import com.example.demo.model.Horario;
import com.example.demo.model.ReservaAsiento;
import com.example.demo.repository.ReservaAsientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservaAsientoService {

    @Autowired
    private ReservaAsientoRepository reservaAsientoRepository;

    public boolean estaReservado(Long idAsiento, Long idHorario) {
        return reservaAsientoRepository
                .existsByAsientoIdAsientoAndHorarioIdHorarioAndReservadoTrue(idAsiento, idHorario);
    }

    public Set<Long> obtenerReservadosPorHorario(Long idHorario) {
        List<ReservaAsiento> reservas = reservaAsientoRepository.findByHorarioIdHorario(idHorario);
        Set<Long> idsReservados = new HashSet<>();

        for (ReservaAsiento r : reservas) {
            if (Boolean.TRUE.equals(r.getReservado())) {
                idsReservados.add(r.getAsiento().getIdAsiento());
            }
        }

        return idsReservados;
    }

    public void guardarReservas(List<ReservaAsiento> reservas) {
        reservaAsientoRepository.saveAll(reservas);
    }

    public void marcarComoReservado(Asiento asiento, Horario horario) {
        ReservaAsiento reserva = new ReservaAsiento();
        reserva.setAsiento(asiento);
        reserva.setHorario(horario);
        reserva.setReservado(true);
        reservaAsientoRepository.save(reserva);
    }

}
