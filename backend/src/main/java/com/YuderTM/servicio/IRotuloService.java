package com.YuderTM.servicio;

import com.YuderTM.modelo.Rotulo;

public interface IRotuloService {

    // Guardar
    Rotulo guardarRotulo(Rotulo rotulo);


    Rotulo buscarPorId(Integer id);
}