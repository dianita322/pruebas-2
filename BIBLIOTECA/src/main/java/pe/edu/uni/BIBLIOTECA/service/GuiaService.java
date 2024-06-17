package pe.edu.uni.BIBLIOTECA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuiaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public String actualizarGuia(String parametro, int valor) {
        String sql = "UPDATE Guia SET Valor = ? WHERE Parametro = ?";
        jdbcTemplate.update(sql, valor, parametro);
        return "Guía actualizada con éxito";
    }

    public int obtenerValorGuia(String parametro) {
        String sql = "SELECT Valor FROM Guia WHERE Parametro = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, parametro);
    }

}
