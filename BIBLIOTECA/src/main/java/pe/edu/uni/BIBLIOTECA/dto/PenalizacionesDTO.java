package pe.edu.uni.BIBLIOTECA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class PenalizacionesDTO {
    private String codigoAlumno;
    private int empleadoID;
    private String tipo;
    private double montoInicial;
}
