package com.YuderTM.servicio;

import com.YuderTM.modelo.DataOld;

import java.util.List;

public interface IDataOldService {

    List<DataOld> buscarPorTexto(String texto);
}
