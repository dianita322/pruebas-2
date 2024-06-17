package pe.edu.uni.BIBLIOTECA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.uni.BIBLIOTECA.dto.LibroDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class LibroService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    //para administradores
    public LibroDTO agregarLibro(LibroDTO dto){
        //verificar que el libro no exista mediante el ISBN
        String sql1 = "select count(1) filas from Libros where ISBN=?";
        int filas = jdbcTemplate.queryForObject(sql1,Integer.class,dto.getIsbn());
        if (filas==1){
            throw new RuntimeException("El libro que está ingresando ya está registrado.");
        }

        //crear id del libro

        //3 caracteres del autor
        ArrayList<Character> caracteres = new ArrayList<>();
        // Recorremos la cadena y añadimos los caracteres que no son espacios en blanco
        for (int i = 0; i < dto.getAutor().length(); i++) {
            char c = dto.getAutor().charAt(i);
            if ((c != ' ' && c != '.') && (c != ',' && c != '-')) {
                caracteres.add(c);
            }
        }
        // Seleccionamos tres caracteres aleatorios y los convertimos a mayúsculas
        Random random = new Random();
        StringBuilder seleccionados = new StringBuilder(3); // Usamos StringBuilder para concatenar los caracteres
        for (int i = 0; i < 3; i++) {
            int index = random.nextInt(caracteres.size());
            seleccionados.append(Character.toUpperCase(caracteres.get(index)));
            caracteres.remove(index); // Evita seleccionar el mismo carácter más de una vez
        }
        String string1 = seleccionados.toString();

        //3 caracteres del titulo
        ArrayList<Character> caracteres1 = new ArrayList<>();
        // Recorremos la cadena y añadimos los caracteres que no son espacios en blanco
        for (int i = 0; i < dto.getTitulo().length(); i++) {
            char s = dto.getTitulo().charAt(i);
            if (s != ' ' && s != '.' && s != ',' && s != ':') {
                caracteres1.add(s);
            }
        }
        // Seleccionamos tres caracteres aleatorios
        Random random1 = new Random();
        StringBuilder seleccionados1 = new StringBuilder(3); // Usamos StringBuilder para concatenar los caracteres
        for (int i = 0; i < 3; i++) {
            int index1 = random1.nextInt(caracteres1.size());
            seleccionados1.append(caracteres1.get(index1));
            caracteres1.remove(index1); // Evita seleccionar el mismo carácter más de una vez
        }
        String string2 = seleccionados1.toString();
        //formar LibroID
        String LibroID = dto.getCdu()+string1+string2;

        //agregar libro
        String sql = "INSERT into Libros (LibroID,Título,Autor,Categoria,CDU,AñoPublicacion,Editorial,ISBN,Descripcion,Cantidad) VALUES (?,?,?,?,?,?,?,?,?,1)";
        jdbcTemplate.update(sql,LibroID,dto.getTitulo(),dto.getAutor(), dto.getCategoria(), dto.getCdu(), dto.getAnioPublicacion(), dto.getEditorial(),dto.getIsbn(),dto.getDescripcion());

        return dto;

    }

    @Transactional
    //para administradores
    public void eliminarLibro(String libroID){
        //verificar que el libro exista
        String sql1 = "select count(1) filas from Libros where LibroID=?";
        int filas = jdbcTemplate.queryForObject(sql1, Integer.class, libroID);
        if (filas != 1){
            throw new RuntimeException("El libro no existe.");
        }
        String sql = "DELETE FROM Libros where LibroID=?";
        jdbcTemplate.update(sql, libroID);

    }

    @Transactional
    //para administradores
    public boolean actualizarLibro(String libroID, LibroDTO dto){
        try {
            // Actualizar el libro
            String sql = "UPDATE Libros SET Título = ?, Autor = ?, Categoria = ?, CDU= ?, AñoPublicacion = ?, Editorial=?, ISBN = ?, Descripcion = ? WHERE LibroID = ?";
            jdbcTemplate.update(sql, dto.getTitulo(), dto.getAutor(), dto.getCategoria(), dto.getCdu(), dto.getAnioPublicacion(), dto.getEditorial(), dto.getIsbn(), dto.getDescripcion(), libroID);
            return true;
        } catch (Exception e){
            return false;
        }
    }


    //para administradores y bibliotecario
    public Map<String,Object> mostrarLibroPorID(String LibroID){
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where LibroID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, LibroID);
        if (filas != 1){
            throw new RuntimeException("El libro no está registrado.");
        }
        sql = "select * from Libros where LibroID=?";
        return jdbcTemplate.queryForMap(sql,LibroID);
    }

    //para administradores y bibliotecario
    public List<Map<String,Object>> mostrarTodosLibros(){
        String sql = "select * from Libros";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> buscarLibroPorTitulo(String titulo) {
        // Convertir el título a minúsculas y agregar comodines para la búsqueda
        String tituloBusqueda = "%" + titulo.toLowerCase() + "%";

        // Verificar que el libro exista
        String sql = "SELECT COUNT(1) FROM Libros WHERE LOWER(Título) LIKE ?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, tituloBusqueda);
        if (filas == 0) {
            throw new RuntimeException("No se encontraron resultados para el título ingresado.");
        }

        // Buscar libro por título
        sql = "SELECT Título, Autor, Categoria, AñoPublicacion, Editorial, ISBN, Descripcion FROM Libros WHERE LOWER(Título) LIKE ?";
        return jdbcTemplate.queryForList(sql, tituloBusqueda);
    }

    public List<Map<String,Object>> buscarLibrorPorAutor(String autor){
        // Convertir el autor a minúsculas y agregar comodines para la búsqueda
        String autorBusqueda = "%" + autor.toLowerCase() + "%";
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where LOWER(Autor) LIKE ?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, autorBusqueda);
        if (filas == 0){
            throw new RuntimeException("No se encontró resultados al autor ingresado.");
        }
        //buscar libro por autor
        sql = "select Título,Autor,Categoria,AñoPublicacion,Editorial,ISBN,Descripcion from Libros where LOWER(Autor) LIKE ?";
        return jdbcTemplate.queryForList(sql,autorBusqueda);
    }

    public List<Map<String,Object>> buscarLibroPorCategoria(String categoria){
        // Convertir el categoria a minúsculas y agregar comodines para la búsqueda
        String categoriaBusqueda = "%" + categoria.toLowerCase() + "%";
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where LOWER(Categoria) LIKE ?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, categoriaBusqueda);
        if (filas == 0){
            throw new RuntimeException("No se encontró resultados a la categoría ingresada.");
        }
        //buscar libro por categoria
        sql = "select Título,Autor,Categoria,AñoPublicacion,Editorial,ISBN,Descripcion from Libros where LOWER(Categoria) LIKE ?";
        return jdbcTemplate.queryForList(sql,categoriaBusqueda);
    }

    public List<Map<String,Object>> buscarLibroPorAnio(int AnioPublicacion){
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where AñoPublicacion=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, AnioPublicacion);
        if (filas == 0){
            throw new RuntimeException("No se encontró resultados al año de publicación ingresado.");
        }
        //buscar libro por año
        sql = "select Título,Autor,Categoria,AñoPublicacion,Editorial,ISBN,Descripcion from Libros where AñoPublicacion=?";
        return jdbcTemplate.queryForList(sql,AnioPublicacion);
    }

    public List<Map<String,Object>> buscarLibroPorEditorial(String editorial){
        // Convertir el editorial a minúsculas y agregar comodines para la búsqueda
        String editorialBusqueda = "%" + editorial.toLowerCase() + "%";
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where LOWER(Editorial) LIKE ?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, editorialBusqueda);
        if (filas == 0){
            throw new RuntimeException("No se encontró resultados a la editorial ingresada.");
        }
        //buscar libro por editorial
        sql = "select Título,Autor,Categoria,AñoPublicacion,Editorial,ISBN,Descripcion from Libros where LOWER(Editorial) LIKE ?";
        return jdbcTemplate.queryForList(sql,editorialBusqueda);
    }

    public Map<String,Object> buscarLibroPorISBN(String ISBN){
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where ISBN=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, ISBN);
        if (filas == 0){
            throw new RuntimeException("No se encontró resultados al ISBN ingresado.");
        }
        //buscar libro por ISBN
        sql = "select Título,Autor,Categoria,AñoPublicacion,Editorial,ISBN,Descripcion from Libros where ISBN=?";
        return jdbcTemplate.queryForMap(sql,ISBN);
    }


}
