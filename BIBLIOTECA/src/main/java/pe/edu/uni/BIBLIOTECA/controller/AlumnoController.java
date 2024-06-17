package pe.edu.uni.BIBLIOTECA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.uni.BIBLIOTECA.dto.AlumnoDTO;
import pe.edu.uni.BIBLIOTECA.service.AlumnoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alumno")
@CrossOrigin("")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("/mostrarTodos")
    public List<Map<String,Object>> mostrarTodos(){

        return alumnoService.obtenerTodosAlumnos();
    }

    @GetMapping("/mostrarPorAlumno/{codigoAlumno}")
    public Map<String,Object> mostrarPorAlumno(@PathVariable String codigoAlumno){
        return alumnoService.obtenerAlumnoPorCodigo(codigoAlumno);
    }

    @GetMapping("/mostrarDatos/{codigoAlumno}")
    public Map<String,Object> mostrarDatos(@PathVariable String codigoAlumno){
        return alumnoService.obtenerDatosPorCodigo(codigoAlumno);
    }

    @PostMapping("/agregar")
    public String agregar(@RequestBody AlumnoDTO dto){
        String mensaje;
        try {
            dto = alumnoService.agregarAlumno(dto);
            mensaje = "Alumno "+dto.getCodigoAlumno()+" registrado con éxito.";
        }catch (Exception e){
            mensaje = "Error: "+e.getMessage();
        }

        return mensaje;
    }

    @PutMapping("/actualizar/{codigoAlumno}")
    public String actualizar(@PathVariable String codigoAlumno,@RequestBody AlumnoDTO dto){
        String mensaje;
        boolean actualizado = alumnoService.actualizarAlumno(codigoAlumno,dto);
        if(actualizado){
            mensaje = "Alumno actualizado.";
        }
        else {
            mensaje = "Error al actualizar.";
        }

        return mensaje;
    }

    @DeleteMapping("/eliminar/{codigoAlumno}")
    public String eliminar(@PathVariable String codigoAlumno){
        String mensaje;
        boolean actualizado = alumnoService.eliminarAlumno(codigoAlumno);
        if (actualizado){
            mensaje = "Alumno eliminado.";
        }
        else {
            mensaje = "Error al eliminar alumno.";
        }

        return mensaje;
    }
}
