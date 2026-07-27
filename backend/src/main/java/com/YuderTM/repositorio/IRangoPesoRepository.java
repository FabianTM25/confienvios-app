package com.YuderTM.repositorio;

import com.YuderTM.modelo.RangoPeso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRangoPesoRepository extends JpaRepository<RangoPeso, Integer> {
}
