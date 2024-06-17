package pe.edu.uni.BIBLIOTECA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class PrestamoDTO {
    private String codigoAlumno;
    private String ejemplarID;
    private int empleadoID;
    private String tipoPrestamo;
}
