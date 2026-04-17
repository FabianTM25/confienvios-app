package com.YuderTM.repositorio;

import com.YuderTM.modelo.Rotulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRotuloRepository extends JpaRepository<Rotulo, Integer> {
    Optional<Rotulo> findById(Integer id);
}
