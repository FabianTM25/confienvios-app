package com.YuderTM.repositorio;

import com.YuderTM.modelo.DataOld;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDataOldRepository extends JpaRepository<DataOld, Integer> {

    @Query("SELECT d FROM DataOld d WHERE " +
            "LOWER(d.cedulaRmt) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(d.nombreRmt) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(d.nombreDto) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(d.documentoDto) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<DataOld> buscarPorTexto(@Param("texto") String texto);
}
