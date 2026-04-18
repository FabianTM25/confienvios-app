package com.YuderTM.controlador;


import com.YuderTM.servicio.ReporteService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/reportes", produces = "application/json")
/*@CrossOrigin(origins = {
    "http://localhost:4200",
    "https://confienvios-app-two.vercel.app/"  // reemplaza con tu URL real de Angular
})*/
@CrossOrigin(origins = "*")
public class ReportControlador {

    private final ReporteService reporteService;

    public ReportControlador(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/report/{id}")
    public ResponseEntity<byte[]> generarReporte(@PathVariable Integer id) {
        try {

            byte[] report = reporteService.generarReporte(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename=Factura.pdf");

            return new ResponseEntity<>(report, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar PDF: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/rotulo/{id}")
    public ResponseEntity<byte[]> generarRotulo(@PathVariable Integer id) {
        try {
            byte[] pdf = reporteService.generarRotulo(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename=Rotulo.pdf");

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }
}