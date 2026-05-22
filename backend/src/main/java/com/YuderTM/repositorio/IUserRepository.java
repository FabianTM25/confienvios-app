package com.YuderTM.repositorio;
import com.YuderTM.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
    public interface IUserRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsuario(String usuario);

    }

