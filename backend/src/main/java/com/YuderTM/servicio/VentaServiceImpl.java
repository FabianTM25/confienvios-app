package com.YuderTM.servicio;

import com.YuderTM.modelo.Configuracion;
import com.YuderTM.modelo.Factura;
import com.YuderTM.modelo.RangoPeso;
import com.YuderTM.modelo.Usuario;
import com.YuderTM.modelo.Venta;
import com.YuderTM.repositorio.IUserRepository;
import com.YuderTM.repositorio.IVentaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class VentaServiceImpl implements IVentaService {

    private final IVentaRepository iVentaRepository;
    private final IFacturaService iFacturaService;
    private final IConfiguracionService iConfiguracionService;
    private final IRangoPesoService iRangoPesoService;
    private final IUserRepository iUserRepository;

    public VentaServiceImpl(
            IVentaRepository iVentaRepository,
            IFacturaService iFacturaService,
            IConfiguracionService iConfiguracionService,
            IRangoPesoService iRangoPesoService,
            IUserRepository iUserRepository
    ) {
        this.iVentaRepository = iVentaRepository;
        this.iFacturaService = iFacturaService;
        this.iConfiguracionService = iConfiguracionService;
        this.iRangoPesoService = iRangoPesoService;
        this.iUserRepository = iUserRepository;
    }

    // Toma el vendedor del usuario autenticado (JWT); usa el nombre visible si existe,
    // si no cae al usuario de login, y si no hay sesion deja el campo vacio.
    private String obtenerVendedorActual() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        String username = authentication.getName();
        Usuario usuario = iUserRepository.findByUsuario(username);

        if (usuario == null) {
            return username;
        }

        return (usuario.getUser_name() != null && !usuario.getUser_name().isBlank())
                ? usuario.getUser_name()
                : username;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarVenta() {
        return iVentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Venta buscarVentaId(Integer id_venta) {
        return iVentaRepository.findById(id_venta).orElse(null);
    }

    @Override
    public Venta crearVenta(
            String numero_factura,
            BigDecimal peso,
            boolean con_caja,
            String observaciones,
            BigDecimal valor_avaluo,
            String forma_pago
    ) {

        Factura factura = iFacturaService.buscarFacturaPorNumero(numero_factura);

        if (factura == null) {
            throw new NoSuchElementException("No existe una factura con el numero " + numero_factura);
        }

        if (iVentaRepository.existeVentaActivaPorFactura(numero_factura)) {
            throw new IllegalArgumentException(
                    "Ya existe una venta activa para la factura " + numero_factura
                            + ". Debe anular la venta anterior antes de registrar una nueva."
            );
        }

        BigDecimal valorPeso = calcularValorPeso(peso);

        Configuracion configuracion = iConfiguracionService.obtenerConfiguracion();
        BigDecimal precioCaja = con_caja ? configuracion.getPrecio_caja() : BigDecimal.ZERO;

        Venta venta = new Venta();
        venta.setId_factura(factura.getId_factura());
        venta.setNumero_factura(factura.getNumero_factura());
        venta.setNombre_cliente_dto(factura.getNombre_cliente_dto());
        venta.setDocumento_cliente_dto(factura.getDocumento_cliente_dto());
        venta.setNombre_cliente_rmt(factura.getNombre_cliente_rmt());
        venta.setTipo_documento_rmt(factura.getTipo_documento_rmt());
        venta.setDocumento_cliente_rmt(factura.getDocumento_cliente_rmt());
        venta.setTelefono_cliente_rmt(factura.getTelefono_cliente_rmt());
        venta.setPeso(peso);
        venta.setValor_peso(valorPeso);
        venta.setCon_caja(con_caja);
        venta.setPrecio_caja_aplicado(con_caja ? configuracion.getPrecio_caja() : BigDecimal.ZERO);
        venta.setValor_envio(valorPeso.add(precioCaja));
        venta.setObservaciones(observaciones);
        venta.setValor_avaluo(valor_avaluo);
        venta.setVendedor(obtenerVendedorActual());
        venta.setForma_pago((forma_pago != null && !forma_pago.isBlank()) ? forma_pago : "Efectivo");
        venta.setEstado("1");

        // guardar primero para obtener el id
        Venta guardada = iVentaRepository.save(venta);

        // usar el id como numero de venta
        guardada.setNumero_venta(String.format("VEN-%04d", guardada.getId_venta()));

        return iVentaRepository.save(guardada);
    }

    private BigDecimal calcularValorPeso(BigDecimal peso) {

        List<RangoPeso> rangos = iRangoPesoService.listarRangos();

        return rangos.stream()
                .filter(rango ->
                        peso.compareTo(rango.getPeso_desde()) >= 0
                                && peso.compareTo(rango.getPeso_hasta()) <= 0
                )
                .findFirst()
                .map(RangoPeso::getValor)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "No hay un rango de peso configurado para " + peso + " kg"
                        )
                );
    }

    @Override
    public void eliminarVentaId(Integer id_venta) {
        if (!iVentaRepository.existsById(id_venta)) {
            throw new NoSuchElementException("Venta no encontrada " + id_venta);
        }
        iVentaRepository.deleteById(id_venta);
    }

    @Override
    public Venta anularVenta(Integer id_venta) {
        Venta venta = buscarVentaId(id_venta);

        if (venta == null) {
            throw new NoSuchElementException("Venta no encontrada " + id_venta);
        }

        venta.setEstado("2");

        return iVentaRepository.save(venta);
    }
}
