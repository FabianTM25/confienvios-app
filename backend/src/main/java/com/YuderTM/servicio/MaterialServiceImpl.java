package com.YuderTM.servicio;

import com.YuderTM.modelo.Material;
import com.YuderTM.repositorio.IMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class MaterialServiceImpl implements IMaterialService {

    private final IMaterialRepository iMaterialRepository;

    public MaterialServiceImpl(IMaterialRepository iMaterialRepository) {
        this.iMaterialRepository = iMaterialRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> listarMateriales() {
        return iMaterialRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Material buscarMaterialId(Integer id_material) {
        return iMaterialRepository.findById(id_material).orElse(null);
    }

    @Override
    public Material guardarMaterial(Material material) {

        if (material.getNombre_cliente_rmt() == null || material.getNombre_cliente_rmt().isBlank()) {
            throw new IllegalArgumentException("Debe buscar y seleccionar un cliente");
        }

        if (material.getTipo_documento() == null || material.getTipo_documento().isBlank()) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio");
        }

        Material guardado = iMaterialRepository.save(material);

        // El numero de radicado se asigna una sola vez, al crear, usando el id ya generado
        if (guardado.getNumero_radicado() == null || guardado.getNumero_radicado().isBlank()) {
            guardado.setNumero_radicado(String.format("MAT-%04d", guardado.getId_material()));
            guardado = iMaterialRepository.save(guardado);
        }

        return guardado;
    }

    @Override
    public void eliminarMaterialId(Integer id_material) {

        if (!iMaterialRepository.existsById(id_material)) {
            throw new NoSuchElementException("Material no encontrado " + id_material);
        }

        iMaterialRepository.deleteById(id_material);
    }
}
