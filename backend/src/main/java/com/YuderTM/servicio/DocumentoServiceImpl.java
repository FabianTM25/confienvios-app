package com.YuderTM.servicio;

import com.YuderTM.modelo.Documento;
import com.YuderTM.repositorio.IDocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class DocumentoServiceImpl implements IDocumentoService {

    private final IDocumentoRepository iDocumentoRepository;

    public DocumentoServiceImpl(IDocumentoRepository iDocumentoRepository) {
        this.iDocumentoRepository = iDocumentoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Documento> listarDocumentos() {
        return iDocumentoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Documento buscarDocumentoId(Integer id_documento) {
        return iDocumentoRepository.findById(id_documento).orElse(null);
    }

    @Override
    public Documento guardarDocumento(Documento documento) {

        if (documento.getNombre_cliente_rmt() == null || documento.getNombre_cliente_rmt().isBlank()) {
            throw new IllegalArgumentException("Debe buscar y seleccionar un cliente");
        }

        if (documento.getTipo_documento() == null || documento.getTipo_documento().isBlank()) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio");
        }

        Documento guardado = iDocumentoRepository.save(documento);

        // El numero de radicado se asigna una sola vez, al crear, usando el id ya generado
        if (guardado.getNumero_radicado() == null || guardado.getNumero_radicado().isBlank()) {
            guardado.setNumero_radicado(String.format("RAD-%04d", guardado.getId_documento()));
            guardado = iDocumentoRepository.save(guardado);
        }

        return guardado;
    }

    @Override
    public void eliminarDocumentoId(Integer id_documento) {

        if (!iDocumentoRepository.existsById(id_documento)) {
            throw new NoSuchElementException("Documento no encontrado " + id_documento);
        }

        iDocumentoRepository.deleteById(id_documento);
    }
}
