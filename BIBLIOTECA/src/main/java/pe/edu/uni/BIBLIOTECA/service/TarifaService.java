package pe.edu.uni.BIBLIOTECA.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TarifaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public String actualizarTarifa(String tipo, double monto) {
        String sql = "UPDATE TarifaPenalizaciones SET Monto = ? WHERE Tipo = ?";
        jdbcTemplate.update(sql, monto, tipo);
        return "Tarifa actualizada con éxito";
    }

    public double obtenerTarifa(String tipo) {
        String sql = "SELECT Monto FROM TarifaPenalizaciones WHERE Tipo = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, tipo);
    }

}
