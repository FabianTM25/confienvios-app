package com.YuderTM.servicio;

import com.YuderTM.modelo.Configuracion;
import com.YuderTM.repositorio.IConfiguracionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ConfiguracionServiceImpl implements IConfiguracionService {

    private static final Integer ID_UNICO = 1;

    private final IConfiguracionRepository iConfiguracionRepository;

    public ConfiguracionServiceImpl(IConfiguracionRepository iConfiguracionRepository) {
        this.iConfiguracionRepository = iConfiguracionRepository;
    }

    @Override
    public Configuracion obtenerConfiguracion() {
        return iConfiguracionRepository.findById(ID_UNICO)
                .orElseGet(() -> {
                    Configuracion configuracion = new Configuracion();
                    configuracion.setId_configuracion(ID_UNICO);
                    configuracion.setPrecio_caja(BigDecimal.ZERO);
                    return iConfiguracionRepository.save(configuracion);
                });
    }

    @Override
    public Configuracion actualizarConfiguracion(Configuracion configuracion) {
        configuracion.setId_configuracion(ID_UNICO);
        return iConfiguracionRepository.save(configuracion);
    }
}
