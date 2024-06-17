package pe.edu.uni.BIBLIOTECA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.uni.BIBLIOTECA.service.EjemplarService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ejemplar")
@CrossOrigin("")
public class EjemplarController {

    @Autowired
    private EjemplarService ejemplarService;

    @GetMapping("/mostrarTodos")
    public List<Map<String,Object>> mostrarTodos(){

        return ejemplarService.mostrarTodosEjemplares();
    }

    @GetMapping("/mostrarPorID")
    public Map<String,Object> mostrarPorID(@RequestParam String EjemplarID){
        return ejemplarService.mostrarEjemplarbyID(EjemplarID);
    }

    @GetMapping("/mostrarPorLibroID")
    public List<Map<String,Object>> mostrarPorLibroID(@RequestParam String LibroID){
        return ejemplarService.mostrarEjemplarbyLibroID(LibroID);
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam String LibroID){
        String mensaje;
        try {
            ejemplarService.AgregarEjemplar(LibroID);
            mensaje = "Ejemplar agregado de manera exitosa.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }

    @PutMapping("/actualizar")
    public String actualizar(@RequestParam String EjemplarID,@RequestParam String Estado){
        String mensaje;
        try {
            ejemplarService.actualizarEstadoEjemplar(EjemplarID,Estado);
            mensaje = "Ejemplar actualizado con éxito.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }

    @DeleteMapping("/eliminar")
    public String eliminar(@RequestParam String EjemplarID){
        String mensaje;
        try {
            ejemplarService.eliminarEjemplar(EjemplarID);
            mensaje = "Ejemplar eliminado de manera exitosa.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }

}
