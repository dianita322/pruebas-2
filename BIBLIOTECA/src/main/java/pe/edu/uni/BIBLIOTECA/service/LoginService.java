package pe.edu.uni.BIBLIOTECA.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import pe.edu.uni.BIBLIOTECA.dto.LoginDTO;

@Service
public class LoginService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public LoginDTO accesoUsuario(LoginDTO bean) throws SQLException {
		// Validando que no este vacia las credenciales
		if (bean.getCodigo().isEmpty() || bean.getPassword().isEmpty()) {
			throw new RuntimeException("Error, no ha insertado nada.");
		}
		// Validando que exista en la tabla Alumnos
		String sql = "select count(1) cont from Alumnos where ";
		sql += "CodigoAlumno = ? or Usuario = ?";
		int cont = jdbcTemplate.queryForObject(sql, Integer.class,
				bean.getCodigo(), bean.getCodigo());
		if (cont != 1) {
			// Validando su existencia en la tabla Empleados
			sql = "select count(1) cont from Empleados where ";
			sql += "Usuario = ?";
			cont = jdbcTemplate.queryForObject(sql, Integer.class,
					bean.getCodigo());
			if (cont != 1) {
				throw new RuntimeException("Error, este usuario no existe.");
			} else {
				bean.setTipo("Empleados");
			}
		} else {
			bean.setTipo("Alumnos");
			// Validando que el alumno no este bloqueado
			sql = "select count(1) cont from Alumnos where ";
			sql += "Usuario = ? and Estado = 'ACTIVO' ";
			cont = jdbcTemplate.queryForObject(sql, Integer.class, bean.getCodigo());
			if (cont != 1) {
				bean.setEstado("BLOQUEADO");
				return bean;
			} else {
				bean.setEstado("ACTIVO");
			}
		}
		// Validando las credenciales si son correctas o no
		sql = "select count(1) cont from " + bean.getTipo();
		sql += " where Usuario = ? and Contraseña = ?";
		cont = jdbcTemplate.queryForObject(sql, Integer.class, bean.getCodigo(), bean.getPassword());
		if (cont != 1) {
			bean.setActiva(false);
		} else {
			bean.setActiva(true);
		}
		return bean;
	}
	
}
