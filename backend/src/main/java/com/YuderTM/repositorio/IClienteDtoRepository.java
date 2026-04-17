package com.YuderTM.repositorio;

import com.YuderTM.modelo.Cliente_dto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
    public interface IClienteDtoRepository extends JpaRepository<Cliente_dto, Integer> {
    Optional<Cliente_dto> findByDocumentoClienteDto(String documento);
    }

