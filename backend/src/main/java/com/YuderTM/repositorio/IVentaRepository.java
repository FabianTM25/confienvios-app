package com.YuderTM.repositorio;

import com.YuderTM.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Integer> {

    // una factura solo puede tener una venta activa (no anulada) asociada
    @Query("SELECT COUNT(v) > 0 FROM Venta v WHERE v.numero_factura = :numero AND v.estado <> '2'")
    boolean existeVentaActivaPorFactura(@Param("numero") String numeroFactura);
}
