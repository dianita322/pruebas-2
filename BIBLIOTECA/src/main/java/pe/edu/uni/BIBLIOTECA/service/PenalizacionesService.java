package pe.edu.uni.BIBLIOTECA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pe.edu.uni.BIBLIOTECA.dto.PenalizacionesDTO;
import pe.edu.uni.BIBLIOTECA.dto.registrarPagoPenalizacionDTO;

import java.util.List;
import java.util.Map;

@Service
public class PenalizacionesService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //para administradores
    public List<Map<String,Object>> mostrarTodasPenalizaciones(){
        String sql = "select * from Penalizaciones";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String,Object>> mostrarPenalizacionesbyAlumno(String CodigoAlumno){
        String sql="select count(1) filas from Alumnos where CodigoAlumno=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class,CodigoAlumno);
        if (filas != 1){
            throw new RuntimeException("El código del alumno no existe.");
        }
        sql = "select * from Penalizaciones where CodigoAlumno=?";
        return jdbcTemplate.queryForList(sql,CodigoAlumno);
    }

    public List<Map<String,Object>> mostrarPenalizacionesPendientesbyAlumno(String CodigoAlumno){
        String sql="select count(1) filas from Alumnos where CodigoAlumno=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class,CodigoAlumno);
        if (filas != 1){
            throw new RuntimeException("El código del alumno no existe.");
        }
        sql = "select Tipo,EmpleadoID,FechaPenalizacion,Monto from Penalizaciones where CodigoAlumno=? and Estado='NO PAGADO'";
        return jdbcTemplate.queryForList(sql,CodigoAlumno);
    }

    //para administradores
    public PenalizacionesDTO penalizarAlumno(PenalizacionesDTO dto){
        //validar empleado
        String sql = "select COUNT(1) filas from Empleados E where E.EmpleadoID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getEmpleadoID());
        if (filas!=1){
            throw new RuntimeException("Id de empleado no existe. ");
        }
        // Validar que el empleado labora
        sql = "SELECT COUNT(1) filas FROM Empleados WHERE EmpleadoID = ? and Estado = 'EN LABOR'";
        filas = jdbcTemplate.queryForObject(sql, Integer.class, dto.getEmpleadoID());
        if (filas == 0) {
            throw new RuntimeException( "El empleado ya no labora en el establecimiento.");
        }
        //verificar permisos de administrador del empleado
        sql ="select count(1) filas from Empleados where EmpleadoID=? and Tipo = 'ADMINISTRADOR'";
        filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getEmpleadoID());
        if (filas != 1){
            throw new RuntimeException("El empleado ingresado no tiene los permisos para realizar esta acción.");
        }
        //verificar existencia de alumno
        sql = "select count(1) filas from Alumnos where CodigoAlumno=?";
        filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getCodigoAlumno());
        if (filas != 1){
            throw new RuntimeException("El código de alumno ingresado no existe.");
        }
        //verificar que se sobrepaso el limite de faltas (si fuese el caso)
        if (dto.getTipo() == "LIMITE DE FALTAS") {
            sql = "select NumeroFaltas from Alumnos where CodigoAlumno=?";
            int NumeroFaltas = jdbcTemplate.queryForObject(sql, Integer.class, dto.getCodigoAlumno());
            sql = "select Valor from Guia where Parametro='Faltas maximas'";
            int Valor = jdbcTemplate.queryForObject(sql, Integer.class);
            if (NumeroFaltas < Valor) {
                throw new RuntimeException("El alumno no paso el límite máximo de faltas establecido.");
            }
        }
        //obtener monto segun tipo de penalizaciones
        sql = "select Monto from TarifaPenalizaciones where Tipo=?";
        double Monto = jdbcTemplate.queryForObject(sql, Double.class,dto.getTipo());
        Monto += dto.getMontoInicial();
        //registrar penalizacion
        sql = "insert into Penalizaciones values(?,?,?,convert(varchar(10),getdate(),103),?,'NO PAGADO')";
        jdbcTemplate.update(sql,dto.getCodigoAlumno(),dto.getTipo(),dto.getEmpleadoID(),Monto);
        //actualizar estado de alumno penalizado
        sql = "update Alumnos set Estado = 'BLOQUEADO' where CodigoAlumno=?";
        jdbcTemplate.update(sql,dto.getCodigoAlumno());

        return dto;

    }

    //para adminitradores
    public registrarPagoPenalizacionDTO registrarPago(registrarPagoPenalizacionDTO dto){
        //validar empleado
        String sql = "select COUNT(1) filas from Empleados E where E.EmpleadoID=?";
        int filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getEmpleadoID());
        if (filas!=1){
            throw new RuntimeException("Id de empleado no existe. ");
        }
        // Validar que el empleado labora
        sql = "SELECT COUNT(1) filas FROM Empleados WHERE EmpleadoID = ? and Estado = 'EN LABOR'";
        filas = jdbcTemplate.queryForObject(sql, Integer.class, dto.getEmpleadoID());
        if (filas == 0) {
            throw new RuntimeException( "El empleado ya no labora en el establecimiento.");
        }
        //verificar permisos de administrador del empleado
        sql ="select count(1) filas from Empleados where EmpleadoID=? and Tipo = 'ADMINISTRADOR'";
        filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getEmpleadoID());
        if (filas != 1){
            throw new RuntimeException("El empleado ingresado no tiene los permisos para realizar esta acción.");
        }
        //validar que la penalizacion existe
        sql = "select count(1) filas from Penalizaciones P where P.PenalizacionID=? and P.Estado = 'NO PAGADO'";
        filas = jdbcTemplate.queryForObject(sql, Integer.class, dto.getPenalizacionID());
        if (filas!=1){
            throw new RuntimeException("Id de prenalización incorrecto. La penalización no está registrado o ya ha sido pagada.");
        }
        //validar codigo de alumno
        sql = "select count(1) filas from Penalizaciones P where P.CodigoAlumno=? and P.PenalizacionID=?";
        filas = jdbcTemplate.queryForObject(sql, Integer.class,dto.getCodigoAlumno(),dto.getPenalizacionID());
        if (filas!=1){
            throw new RuntimeException("Código de alumno incorrecto. ");
        }
        //verificar monto correcto
        sql = "select Monto from Penalizaciones where PenalizacionID=?";
        double montoRegistrado = jdbcTemplate.queryForObject(sql, Double.class,dto.getPenalizacionID());
        if(dto.getMonto() != montoRegistrado){
            throw new RuntimeException("El monto ingresado es incorrecto.");
        }
        //obtener el tipo de penalizacion
        sql = "select Tipo from Penalizaciones where PenalizacionID=?";
        String Tipo = jdbcTemplate.queryForObject(sql, String.class, dto.getPenalizacionID());
        //actualizar el estado de la penalizacion
        sql = "update Penalizaciones set Estado = 'PAGADO' where PenalizacionID=?";
        jdbcTemplate.update(sql,dto.getPenalizacionID());
        //actualizar estado del alumno
        sql = "update Alumnos set Estado = 'ACTIVO' where CodigoAlumno=?";
        jdbcTemplate.update(sql,dto.getCodigoAlumno());
        if (Tipo.equals("LIMITE DE FALTAS")){
            //reiniciar numero de faltas
            sql = "update Alumnos set NumeroFaltas = 0 where CodigoAlumno=?";
            jdbcTemplate.update(sql,dto.getCodigoAlumno());
        }

        return dto;

    }



}
