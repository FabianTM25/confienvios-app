package com.YuderTM.servicio;

import com.YuderTM.modelo.Rotulo;

public interface IRotuloService {

    // Guardar
    Rotulo guardarRotulo(Rotulo rotulo);

    // 🔥 ESTE TE FALTABA
    Rotulo buscarPorId(Integer id);
}