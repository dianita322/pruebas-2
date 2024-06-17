package pe.edu.uni.BIBLIOTECA.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class EjemplarService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    //para administradores
    public void AgregarEjemplar(String LibroID){
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where LibroID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, LibroID);
        if (filas != 1){
            throw new RuntimeException("El libro no existe");
        }
        //obtenemos la cantidad actual de ejemplares del libro
        sql = "select count(1) filas from Ejemplares where LibroID=?";
        int cantidad = jdbcTemplate.queryForObject(sql, Integer.class, LibroID);
        //crear el ejemplar ID
        cantidad++;
        String IdNumerico = "000"+cantidad;
        IdNumerico = IdNumerico.substring(IdNumerico.length()-3);
        String EjemplarID = LibroID+IdNumerico;
        //agregar ejemplar a la tabla
        sql = "insert into Ejemplares values(?,?,'DISPONIBLE')";
        jdbcTemplate.update(sql,EjemplarID,LibroID);
        //modificar cantidad de libros
        sql = "update Libros set Cantidad="+cantidad+" where LibroID=?";
        jdbcTemplate.update(sql,LibroID);
    }

    @Transactional
    //para administradores
    public void actualizarEstadoEjemplar(String EjemplarID, String Estado){
        //validar existencia de ejemplar
        String sql = "select count(1) filas from Ejemplares where EjemplarID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, EjemplarID);
        if (filas != 1){
            throw new RuntimeException("El id del ejemplar no existe.");
        }
        //modificar estado
        sql = "update Ejemplares set Estado='"+Estado+"' where EjemplarID=?";
        jdbcTemplate.update(sql,EjemplarID);
    }

    @Transactional
    //para administradores
    public void eliminarEjemplar(String EjemplarID){
        //validar existencia de ejemplar
        String sql = "select count(1) filas from Ejemplares where EjemplarID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, EjemplarID);
        if (filas != 1){
            throw new RuntimeException("El id del ejemplar no existe.");
        }
        //obtenemos el libro id
        sql = "select LibroID from Ejemplares where EjemplarID=?";
        String LibroID = jdbcTemplate.queryForObject(sql, String.class, EjemplarID);
        //obtenemos la cantidad actual de ejemplares del libro
        sql = "select count(1) cantidad from Ejemplares where LibroID=?";
        int cantidad = jdbcTemplate.queryForObject(sql, Integer.class, LibroID);
        //eliminar ejemplar
        sql = "DELETE FROM Ejemplares where EjemplarID=?";
        jdbcTemplate.update(sql, EjemplarID);
        //actualizar la cantidad de ejemplares
        cantidad--;
        sql = "update Libros set Cantidad = "+cantidad+" where LibroID=?";
        jdbcTemplate.update(sql,LibroID);

    }

    //para administradores y bibliotecarios
    public List<Map<String,Object>> mostrarTodosEjemplares(){
        String sql = "select * from Ejemplares";
        return jdbcTemplate.queryForList(sql);
    }

    //para administradores y bibliotecarios
    public Map<String,Object> mostrarEjemplarbyID(String EjemplarID){
        //validar existencia de ejemplar
        String sql = "select count(1) filas from Ejemplares where EjemplarID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, EjemplarID);
        if (filas != 1){
            throw new RuntimeException("El id del ejemplar no existe.");
        }
        sql = "select EjemplarID,LibroID,Estado from Ejemplares where EjemplarID=?";
        return jdbcTemplate.queryForMap(sql,EjemplarID);
    }

    //para administradores y bibliotecarios
    public List<Map<String,Object>> mostrarEjemplarbyLibroID(String LibroID){
        //verificar que el libro exista
        String sql = "select count(1) filas from Libros where LibroID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, LibroID);
        if (filas != 1){
            throw new RuntimeException("El libro no existe");
        }
        sql = "select EjemplarID,Estado from Ejemplares where LibroID=?";
        return jdbcTemplate.queryForList(sql,LibroID);
    }
}
