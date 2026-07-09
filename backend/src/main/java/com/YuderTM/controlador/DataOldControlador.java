package com.YuderTM.controlador;

import com.YuderTM.modelo.DataOld;
import com.YuderTM.servicio.IDataOldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/data-old", produces = "application/json")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:64764",
        "https://confienvios-app-two.vercel.app"
})
public class DataOldControlador {

    private final IDataOldService iDataOldService;

    public DataOldControlador(IDataOldService iDataOldService) {
        this.iDataOldService = iDataOldService;
    }

    // =============================
    // BUSCAR EN DATA_OLD
    // =============================
    @GetMapping("/buscar")
    public ResponseEntity<List<DataOld>> buscar(@RequestParam String texto) {
        return ResponseEntity.ok(iDataOldService.buscarPorTexto(texto));
    }
}
