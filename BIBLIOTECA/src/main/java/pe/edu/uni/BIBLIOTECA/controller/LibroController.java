package pe.edu.uni.BIBLIOTECA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.uni.BIBLIOTECA.dto.LibroDTO;
import pe.edu.uni.BIBLIOTECA.service.LibroService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/libros")
@CrossOrigin("")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @PostMapping("/agregar")
    public String agregar(@RequestBody LibroDTO dto){
        String mensaje;
        try {
            dto = libroService.agregarLibro(dto);
            mensaje = "Libro agregado.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }

    @DeleteMapping("/eliminar/{libroID}")
    public ResponseEntity<String> eliminar(@PathVariable String libroID) {
        try {
            libroService.eliminarLibro(libroID);
            return new ResponseEntity<>("Libro eliminado.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/actualizar/{libroID}")
    public ResponseEntity<String> actualizar(@PathVariable String libroID, @RequestBody LibroDTO dto) {
        try {
            boolean actualizado = libroService.actualizarLibro(libroID, dto);
            if (actualizado) {
                return new ResponseEntity<>("Libro actualizado.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error al actualizar.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mostrarPorID/{libroID}")
    public ResponseEntity<Map<String, Object>> mostrarPorID(@PathVariable String libroID) {
        try {
            Map<String, Object> libro = libroService.mostrarLibroPorID(libroID);
            if (libro.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(libro, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mostrarTodos")
    public List<Map<String,Object>> mostrarTodos(){
        return libroService.mostrarTodosLibros();
    }

    @GetMapping("/mostrarPorTitulo")
    public ResponseEntity<List<Map<String, Object>>> buscarLibroPorTitulo(@RequestParam String titulo) {
        try {
            List<Map<String, Object>> libros = libroService.buscarLibroPorTitulo(titulo);
            return ResponseEntity.ok(libros);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/mostrarPorAutor")
    public ResponseEntity<List<Map<String,Object>>> mostrarPorAutor(@RequestParam String autor){
        try {
            List<Map<String,Object>> libros = libroService.buscarLibrorPorAutor(autor);
            return ResponseEntity.ok(libros);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/mostrarPorCategoria")
    public ResponseEntity<List<Map<String,Object>>> mostrarPorCategoria(@RequestParam String categoria){
        try {
            List<Map<String,Object>> libros = libroService.buscarLibroPorCategoria(categoria);
            return ResponseEntity.ok(libros);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }

    }

    @GetMapping("/mostrarPorEditorial")
    public ResponseEntity<List<Map<String,Object>>> mostrarPorEditorial(@RequestParam String editorial){
        try {
            List<Map<String,Object>> libros = libroService.buscarLibroPorEditorial(editorial);
            return ResponseEntity.ok(libros);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }

    }

    @GetMapping("/mostrarPorAnio")
    public ResponseEntity<List<Map<String,Object>>> mostrarPorAnio(@RequestParam int anio){
        try {
            List<Map<String,Object>> libros = libroService.buscarLibroPorAnio(anio);
            return ResponseEntity.ok(libros);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }

    }

    @GetMapping("/mostrarPorISBN")
    public ResponseEntity<Map<String,Object>> mostrarPorISBN(@RequestParam String isbn){
        try {
            Map<String,Object> libro = libroService.buscarLibroPorISBN(isbn);
            return ResponseEntity.ok(libro);
        }catch (RuntimeException e){
            return ResponseEntity.status(404).body(null);
        }

    }





}
