package pe.edu.uni.BIBLIOTECA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class registrarPagoPenalizacionDTO {
    private int penalizacionID;
    private String codigoAlumno;
    private int empleadoID;
    private double monto;
}
