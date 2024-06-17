package pe.edu.uni.BIBLIOTECA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class FaltasService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //para alumno
    public int obtenerFaltasbyAlumno(String CodigoAlumno){
        //verificar que el alumno exista
        String sql="select count(1) filas from Alumnos where CodigoAlumno=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class,CodigoAlumno);
        if (filas != 1){
            throw new RuntimeException("El código del alumno no existe.");
        }
        //obtener faltas
        sql="select NumeroFaltas from Alumnos where CodigoAlumno=?";
        return jdbcTemplate.queryForObject(sql, Integer.class,CodigoAlumno);
    }

    @Transactional
    //para administrador
    public void AgregarFalta(String CodigoAlumno, int PrestamoID){
        //verificar el ingreso del codigoalumno
        String sql = "select count(1) filas from Alumnos where CodigoAlumno=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class, CodigoAlumno);
        if (filas != 1){
            throw new RuntimeException("El código del alumno no existe.");
        }
        //validar el id de prestamo
        sql = "select count(1) filas from Devoluciones where PrestamoID=? and CodigoAlumno=?";
        filas = jdbcTemplate.queryForObject(sql, Integer.class,PrestamoID,CodigoAlumno);
        if (filas !=1){
            throw new RuntimeException("El id de prestamo para el alumno no esta registrado en la tabla devoluciones.");
        }
        //verificar los dias de demora del prestamo
        sql = "select convert(varchar(10),FechaDevolucion,103) FechaDevolucion from Devoluciones where PrestamoID=?";
        String FechaDevolucionD = jdbcTemplate.queryForObject(sql, String.class, PrestamoID);
        sql = "select convert(varchar(10),FechaDevolucion,103) FechaDevolucion from Prestamos where PrestamoID=?";
        String FechaDevolucionP = jdbcTemplate.queryForObject(sql, String.class, PrestamoID);
        sql = "select convert(varchar(10),FechaPrestamo,103) FechaPrestamo from Prestamos where PrestamoID=?";
        String FechaPrestamo = jdbcTemplate.queryForObject(sql, String.class,PrestamoID);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate FechaEstablecida = LocalDate.parse(FechaDevolucionP,formatter);
        LocalDate FechaDevuelta = LocalDate.parse(FechaDevolucionD,formatter);
        LocalDate FechaPrestada = LocalDate.parse(FechaPrestamo,formatter);
        long diasDiferencia1 = ChronoUnit.DAYS.between(FechaPrestada,FechaEstablecida);
        long diasDiferencia2 = ChronoUnit.DAYS.between(FechaPrestada,FechaDevuelta);
        if ((diasDiferencia1 == diasDiferencia2) || (diasDiferencia2 < diasDiferencia1)){
            throw new RuntimeException("No se encontró demora en la devolución del libro. No se puede agregar la falta.");
        }
        long diasDiferencia = diasDiferencia2 - diasDiferencia1;
        //fijar el peso de la falta segun los dias de demora
        sql = "select PesoFalta from Faltas where ? between MinDias and MaxDias";
        int PesoFalta = jdbcTemplate.queryForObject(sql, Integer.class, diasDiferencia );
        //actualizar el numero de faltas
        sql = "update Alumnos set NumeroFaltas = NumeroFaltas + ? where CodigoAlumno=?";
        jdbcTemplate.update(sql, PesoFalta, CodigoAlumno);

    }






}
