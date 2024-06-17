package pe.edu.uni.BIBLIOTECA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.uni.BIBLIOTECA.dto.PrestamoDTO;
import pe.edu.uni.BIBLIOTECA.service.PrestamoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prestamo")
@CrossOrigin("")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping("/verPendientesPorAlumno/{CodigoAlumno}")
    public ResponseEntity<List<Map<String,Object>>> verPendientesPorAlumno(@PathVariable String CodigoAlumno){
        try {
            List<Map<String,Object>> pendientes = prestamoService.verPrestamosPendientesByAlumno(CodigoAlumno);
            return ResponseEntity.ok(pendientes);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }

    }

    @GetMapping("/verPorAlumnos/{CodigoAlumno}")
    public ResponseEntity<List<Map<String,Object>>> verPorAlumno(@PathVariable String CodigoAlumno){
        try {
            List<Map<String,Object>> prestamos = prestamoService.mostrarPrestamosbyAlumno(CodigoAlumno);
            return ResponseEntity.ok(prestamos);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }

    }

    @GetMapping("/verTodos")
    public List<Map<String,Object>> verTodos(){
        return prestamoService.mostrarPrestamos();
    }

    @PostMapping("/registrar")
    public String registrar(@RequestBody PrestamoDTO dto){
        String mensaje;
        try {
            dto = prestamoService.resgitrarPrestamo(dto);
            mensaje = "Prestamo registrado.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }

    @PutMapping("/renovar/{PrestamoID}")
    public String renovar(@PathVariable int PrestamoID){
        String mensaje;
        try {
            prestamoService.renovarPrestamo(PrestamoID);
            mensaje = "Prestamo renovado.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Map<String, Object>>> historialPrestamosPorMes(@RequestParam int mes, @RequestParam int anio) {
        try {
            List<Map<String,Object>> historial = prestamoService.historialPrestamosPorMes(mes, anio);
            return ResponseEntity.ok(historial);
        } catch (RuntimeException e) {
            e.printStackTrace(); // Imprime la traza de la excepción en la consola
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
