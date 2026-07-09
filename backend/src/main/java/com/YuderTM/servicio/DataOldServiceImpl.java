package com.YuderTM.servicio;

import com.YuderTM.modelo.DataOld;
import com.YuderTM.repositorio.IDataOldRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DataOldServiceImpl implements IDataOldService {

    private final IDataOldRepository iDataOldRepository;

    public DataOldServiceImpl(IDataOldRepository iDataOldRepository) {
        this.iDataOldRepository = iDataOldRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataOld> buscarPorTexto(String texto) {
        return iDataOldRepository.buscarPorTexto(texto);
    }
}
