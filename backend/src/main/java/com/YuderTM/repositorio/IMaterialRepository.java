package com.YuderTM.repositorio;

import com.YuderTM.modelo.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMaterialRepository extends JpaRepository<Material, Integer> {
}
