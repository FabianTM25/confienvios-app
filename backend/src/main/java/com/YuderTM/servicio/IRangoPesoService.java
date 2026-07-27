package com.YuderTM.servicio;

import com.YuderTM.modelo.RangoPeso;

import java.util.List;

public interface IRangoPesoService {

    List<RangoPeso> listarRangos();

    RangoPeso guardarRango(RangoPeso rangoPeso);

    void eliminarRango(Integer id_rango);
}
