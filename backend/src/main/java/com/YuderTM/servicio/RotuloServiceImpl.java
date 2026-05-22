package com.YuderTM.servicio;

import com.YuderTM.modelo.Rotulo;
import com.YuderTM.repositorio.IRotuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RotuloServiceImpl implements IRotuloService {

    @Autowired
    private IRotuloRepository iRotuloRepository;

    @Override
    public Rotulo guardarRotulo(Rotulo rotulo) {
        return iRotuloRepository.save(rotulo);
    }

    @Override
    public Rotulo buscarPorId(Integer id) {
        return iRotuloRepository.findById(id).orElse(null);
    }
}
