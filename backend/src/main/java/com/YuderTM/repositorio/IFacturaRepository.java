package com.YuderTM.repositorio;
import com.YuderTM.modelo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
    public interface IFacturaRepository extends JpaRepository<Factura, Integer> {

    @Query(value = "SELECT COUNT(*) FROM factura WHERE DATE(fecha_creacion) = CURRENT_DATE AND estado = '1'", nativeQuery = true)
    Long contarFacturasDia();

    @Query(value = "SELECT COUNT(*) FROM factura WHERE EXTRACT(MONTH FROM fecha_creacion) = :mes AND EXTRACT(YEAR FROM fecha_creacion) = :anio AND estado = '1'", nativeQuery = true)
    Long contarFacturasMes(@Param("mes") int mes, @Param("anio") int anio);

    @Query(value = "SELECT COUNT(*) FROM factura WHERE EXTRACT(YEAR FROM fecha_creacion) = :anio AND estado = '1'", nativeQuery = true)
    Long contarFacturasAnio(@Param("anio") int anio);
    }

