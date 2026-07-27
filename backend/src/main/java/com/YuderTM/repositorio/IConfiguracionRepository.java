package com.YuderTM.repositorio;

import com.YuderTM.modelo.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfiguracionRepository extends JpaRepository<Configuracion, Integer> {
}
