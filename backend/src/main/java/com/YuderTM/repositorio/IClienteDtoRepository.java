package com.YuderTM.repositorio;

import com.YuderTM.modelo.Cliente_dto;
<<<<<<< HEAD
import com.YuderTM.modelo.Cliente_rmt;
=======
>>>>>>> c95e1604e7ea771cec1d1287270e1c842491141f
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
    public interface IClienteDtoRepository extends JpaRepository<Cliente_dto, Integer> {
    Optional<Cliente_dto> findByDocumentoClienteDto(String documento);
    }

