package com.YuderTM.servicio;

import com.YuderTM.modelo.Documento;

import java.util.List;

public interface IDocumentoService {

    List<Documento> listarDocumentos();

    Documento buscarDocumentoId(Integer id_documento);

    // Guarda el documento con los datos del cliente rmt ya copiados (snapshot), sin modificarlos
    Documento guardarDocumento(Documento documento);

    void eliminarDocumentoId(Integer id_documento);
}
