package pe.edu.uni.BIBLIOTECA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.uni.BIBLIOTECA.service.EjemplarService;
import pe.edu.uni.BIBLIOTECA.service.FaltasService;

@RestController
@RequestMapping("/faltas")
@CrossOrigin("")
public class FaltasController {

    @Autowired
    private  FaltasService faltasService;

    @GetMapping("/porAlumno/{CodigoAlumno}")
    public ResponseEntity<Integer> porAlumno(@PathVariable String CodigoAlumno) {
        try {
            int numeroFaltas = faltasService.obtenerFaltasbyAlumno(CodigoAlumno);
            return ResponseEntity.ok(numeroFaltas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1); // O cualquier otro valor predeterminado
        }
    }

    @PutMapping("/agregar")
    public String agregar(@RequestParam String CodigoAlumno, @RequestParam int PrestamoID){
        String mensaje;
        try {
            faltasService.AgregarFalta(CodigoAlumno,PrestamoID);
            mensaje = "Falta agregada a alumno "+CodigoAlumno;
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }
}
