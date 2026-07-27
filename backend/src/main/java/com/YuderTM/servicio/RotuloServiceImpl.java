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
        // El rótulo no tiene id autogenerado: comparte el id con la factura
        // asociada, por lo que siempre debe llegar explícito.
        if (rotulo.getId_rotulo() == null) {
            throw new IllegalArgumentException(
                "El rótulo debe tener el mismo id que la factura asociada"
            );
        }
        return iRotuloRepository.save(rotulo);
    }

    @Override
    public Rotulo buscarPorId(Integer id) {
        return iRotuloRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarRotulo(Integer id) {
        // No toda factura tiene rótulo generado; eliminar es opcional/idempotente.
        if (iRotuloRepository.existsById(id)) {
            iRotuloRepository.deleteById(id);
        }
    }
}
