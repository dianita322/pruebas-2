package pe.edu.uni.BIBLIOTECA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class LibroDTO {
    private String titulo;
    private String autor;
    private String categoria;
    private int cdu;
    private int anioPublicacion;
    private String editorial;
    private String isbn;
    private String descripcion;
}
