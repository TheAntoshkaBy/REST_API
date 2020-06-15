package com.epam.esm.dao.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.entity.Certificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateDAOPostgres implements CertificateDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CertificateDAOPostgres(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Certificate getCertificateById(Integer id) {
        String sql = "select * from rest_api_basics.certificate where id_certificate = :id";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        return jdbcTemplate.queryForObject(sql, namedParameters, new CertificateMapper());
    }

    @Override
    public List<Certificate> getAll() {
        String sql = "select * from rest_api_basics.certificate";
        return jdbcTemplate.query(sql,new CertificateMapper());
    }

   /* @Override
    public void addCertificate(Certificate certificate) {
        String sql = "";
        return jdbcTemplate.query(sql,new CertificateMapper());
    }*/


    private static final class CertificateMapper implements RowMapper<Certificate> {
        @Override
        public Certificate mapRow(ResultSet rs, int i) throws SQLException {
            return new Certificate(
                    rs.getInt("id_certificate"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getDate("date_of_creation"),
                    rs.getDate("date_of_modification"),
                    rs.getInt("duration_days")
            );
        }
    }
}
