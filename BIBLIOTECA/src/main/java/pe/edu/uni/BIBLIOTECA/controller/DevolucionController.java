package pe.edu.uni.BIBLIOTECA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.uni.BIBLIOTECA.dto.DevolucionDTO;
import pe.edu.uni.BIBLIOTECA.service.DevolucionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devolucion")
@CrossOrigin("")
public class DevolucionController {

    @Autowired
    public DevolucionService devolucionService;

    @GetMapping("/mostrarTodos")
    public List<Map<String,Object>> mostrarTodos(){

        return devolucionService.mostrarTodasDevoluciones();
    }

    @GetMapping("/mostrarPorAlumno")
    public ResponseEntity<List<Map<String,Object>>> mostrarPorAlumno(@RequestParam String CodigoAlumno) {
        try {
            List<Map<String,Object>> devoluciones = devolucionService.mostrarDevolucionesbyAlumno(CodigoAlumno);
            return ResponseEntity.ok(devoluciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/mostrarTardePorAlumno")
    public ResponseEntity<List<Map<String,Object>>> mostrarTardePorAlumno(@RequestParam String CodigoAlumno) {
        try {
            List<Map<String,Object>> devolucionesTarde = devolucionService.mostrarDevolucionesTardebyAlumno(CodigoAlumno);
            return ResponseEntity.ok(devolucionesTarde);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PostMapping("/registrar")
    public String registrar(@RequestBody DevolucionDTO dto){
        String mensaje;
        try {
            dto = devolucionService.registrarDevolucion(dto);
            mensaje = "Devolución registrado de manera exitosa.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }
}
