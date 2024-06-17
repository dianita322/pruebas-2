package pe.edu.uni.BIBLIOTECA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.uni.BIBLIOTECA.dto.DevolucionDTO;

import java.util.List;
import java.util.Map;

@Service
public class DevolucionService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    //para administradores y bibliotecario
    public List<Map<String,Object>> mostrarTodasDevoluciones(){
        String sql = "select * from Devoluciones";
        return jdbcTemplate.queryForList(sql);
    }

    //para administradores y bibliotecario
    public List<Map<String,Object>> mostrarDevolucionesbyAlumno(String CodigoAlumno){
        String sql="select count(1) filas from Alumnos where CodigoAlumno=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class,CodigoAlumno);
        if (filas != 1){
            throw new RuntimeException("El código del alumno no existe.");
        }
        sql = "select PrestamoID, EmpleadoID, EjemplarID, FechaPrestamo, FechaDevolucion from Devoluciones where CodigoAlumno=?";
        return jdbcTemplate.queryForList(sql,CodigoAlumno);
    }

    //para administradores y bibliotecario
    public List<Map<String,Object>> mostrarDevolucionesTardebyAlumno(String CodigoAlumno){
        //verificar codigo de alumno
        String sql="select count(1) filas from Alumnos where CodigoAlumno=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class,CodigoAlumno);
        if (filas != 1){
            throw new RuntimeException("El código del alumno no existe.");
        }
        //obtener devoluciones
        sql = "SELECT P.PrestamoID, P.EmpleadoID, P.EjemplarID, P.FechaPrestamo, P.FechaDevolucion FROM Prestamos P JOIN Devoluciones D ON P.PrestamoID = D.PrestamoID WHERE P.FechaDevolucion != D.FechaDevolucion AND P.CodigoAlumno = ?";
        return jdbcTemplate.queryForList(sql,CodigoAlumno);
    }

    //para bibliotecario
    @Transactional
    public DevolucionDTO registrarDevolucion(DevolucionDTO dto){
        //validar que el prestamo existe
        String sql = "select count(1) filas from Prestamos P where P.PrestamoID=? and P.Estado = 'NO DEVUELTO'";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, dto.getPrestamoID());
        if (filas!=1){
            throw new RuntimeException("Id de préstamo incorrecto. El préstamo no está registrado o ya ha sido cancelado.");
        }
        //validar codigo de alumno
        sql = "select count(1) filas from Prestamos P where P.CodigoAlumno=? and P.PrestamoID=?";
        filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getCodigoAlumno(),dto.getPrestamoID());
        if (filas!=1){
            throw new RuntimeException("Código de alumno incorrecto. ");
        }
        //validar empleado
        sql = "select COUNT(1) filas from Empleados E where E.EmpleadoID=?";
        filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getEmpleadoID());
        if (filas!=1){
            throw new RuntimeException("Id de empleado no existe. ");
        }
        // Validar que el empleado labora
        sql = "SELECT COUNT(1) filas FROM Empleados WHERE EmpleadoID = ? and Estado = 'EN LABOR'";
        filas = jdbcTemplate.queryForObject(sql, Integer.class, dto.getEmpleadoID());
        if (filas == 0) {
            throw new RuntimeException( "El empleado ya no labora en el establecimiento.");
        }
        //obtener los campos del prestamo a cancelar
        sql = "select P.EjemplarID from Prestamos P where P.PrestamoID=?";
        String EjemplarID = jdbcTemplate.queryForObject(sql, String.class, dto.getPrestamoID());
        sql = "select P.FechaPrestamo from Prestamos P where P.PrestamoID=?";
        String FechaPrestamo = jdbcTemplate.queryForObject(sql, String.class, dto.getPrestamoID());
        //registrar devolucion
        sql = "insert into Devoluciones values(?,?,?,?,?,convert(varchar(10),getdate(),103))";
        jdbcTemplate.update(sql, dto.getPrestamoID(), dto.getCodigoAlumno(), dto.getEmpleadoID(), EjemplarID, FechaPrestamo);
        //cambiar el estado del prestamo en la tabla
        sql = "update Prestamos set Estado = 'DEVUELTO' where PrestamoID=?";
        jdbcTemplate.update(sql, dto.getPrestamoID());
        //cambiar el estado del ejemplar
        sql = "update Ejemplares set Estado = 'DISPONIBLE' where EjemplarID=?";
        jdbcTemplate.update(sql, EjemplarID);

        return dto;

    }

}
