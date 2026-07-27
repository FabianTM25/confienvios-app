package com.YuderTM.servicio;

import com.YuderTM.modelo.RangoPeso;
import com.YuderTM.repositorio.IRangoPesoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class RangoPesoServiceImpl implements IRangoPesoService {

    private final IRangoPesoRepository iRangoPesoRepository;

    public RangoPesoServiceImpl(IRangoPesoRepository iRangoPesoRepository) {
        this.iRangoPesoRepository = iRangoPesoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RangoPeso> listarRangos() {
        return iRangoPesoRepository.findAll().stream()
                .sorted(Comparator.comparing(RangoPeso::getPeso_desde))
                .toList();
    }

    @Override
    public RangoPeso guardarRango(RangoPeso rangoPeso) {
        return iRangoPesoRepository.save(rangoPeso);
    }

    @Override
    public void eliminarRango(Integer id_rango) {
        if (!iRangoPesoRepository.existsById(id_rango)) {
            throw new NoSuchElementException("Rango no encontrado " + id_rango);
        }
        iRangoPesoRepository.deleteById(id_rango);
    }
}
