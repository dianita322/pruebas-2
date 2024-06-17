package pe.edu.uni.BIBLIOTECA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class AlumnoDTO {

    private String codigoAlumno;
    private String nombres;
    private String apellidos;
    private String correo;
    private String usuario;
    private String clave;


}
