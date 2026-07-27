package com.YuderTM.servicio;

import com.YuderTM.modelo.Configuracion;

public interface IConfiguracionService {

    Configuracion obtenerConfiguracion();

    Configuracion actualizarConfiguracion(Configuracion configuracion);
}
