package com.YuderTM.repositorio;

import com.YuderTM.modelo.Cliente_rmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
    public interface IClienteRmtRepository extends JpaRepository<Cliente_rmt, Integer> {
    Optional<Cliente_rmt> findByDocumentoClienteRmt(String documento);
}

