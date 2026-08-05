package com.YuderTM.repositorio;

import com.YuderTM.modelo.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDocumentoRepository extends JpaRepository<Documento, Integer> {
}
