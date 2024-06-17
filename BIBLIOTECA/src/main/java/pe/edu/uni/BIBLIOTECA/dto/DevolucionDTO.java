package pe.edu.uni.BIBLIOTECA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class DevolucionDTO {
    private int prestamoID;
    private String codigoAlumno;
    private int empleadoID;
}
