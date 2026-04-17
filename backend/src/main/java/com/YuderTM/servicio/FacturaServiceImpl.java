package com.YuderTM.servicio;


import com.YuderTM.modelo.Factura;
import com.YuderTM.repositorio.IFacturaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;


@Service
@Transactional
public class FacturaServiceImpl implements IFacturaService {

    @Autowired
    private final IFacturaRepository iFacturaRepository;
//constructor -inyeccion de dependencias
    public FacturaServiceImpl(IFacturaRepository iFacturaRepository) {
        this.iFacturaRepository = iFacturaRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Factura> listarFactura() {
        return iFacturaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Factura buscarFacturaId(Integer id_factura) {
        return iFacturaRepository.findById(id_factura).orElse(null);
    }

    @Override
    public Factura guardarFactura(Factura factura) {

        // guardar primero
        Factura guardarFactura = iFacturaRepository.save(factura);

        // usar el id como numero de factura
        guardarFactura.setNumero_factura(String.format("FAC-%04d", guardarFactura.getId_factura()));

        // guardar nuevamente
        return iFacturaRepository.save(guardarFactura);
    }

    @Override
    public void eliminarFacturaId(Integer id_factura) {

        if(!iFacturaRepository.existsById(id_factura)) {
            throw new NoSuchElementException("Factura no encontrado " + id_factura);
        }
        iFacturaRepository.deleteById(id_factura);
    }

    @Override
    public Long contarFacturasDia() {
        return iFacturaRepository.contarFacturasDia();
    }

    @Override
    public Long contarFacturasMes() {
        LocalDateTime hoy = LocalDateTime.now();
        return iFacturaRepository.contarFacturasMes(hoy.getMonthValue(), hoy.getYear());
    }

    @Override
    public Long contarFacturasAnio() {
        return iFacturaRepository.contarFacturasAnio(LocalDateTime.now().getYear());
    }

}