package com.YuderTM.servicio;

import com.YuderTM.modelo.Material;

import java.util.List;

public interface IMaterialService {

    List<Material> listarMateriales();

    Material buscarMaterialId(Integer id_material);

    // Guarda el material con los datos del cliente rmt/dto ya copiados (snapshot), sin modificarlos
    Material guardarMaterial(Material material);

    void eliminarMaterialId(Integer id_material);
}
