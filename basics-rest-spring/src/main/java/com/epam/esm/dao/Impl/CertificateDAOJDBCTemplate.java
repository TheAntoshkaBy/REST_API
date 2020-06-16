package com.epam.esm.dao.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateDAOJDBCTemplate implements CertificateDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CertificateDAOJDBCTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Certificate getCertificateById(int id) {
        String sql = "select * from rest_api_basics.certificate where id_certificate = :id";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        return jdbcTemplate.queryForObject(sql, namedParameters, new CertificateMapper());
    }

    @Override
    public List<Certificate> getAll() {
        String sql = "select * from rest_api_basics.certificate";
        return jdbcTemplate.query(sql, new CertificateMapper());
    }

    @Override
    public void addCertificate(Certificate certificate) {

        String SQL = "INSERT INTO rest_api_basics.certificate (name, " +
                "description, " +
                "date_of_creation, " +
                "date_of_modification, " +
                "duration_days, " +
                "price, " +
                "id_certificate) " +
                "VALUES (:name,:description,:date_of_creation,:date_of_modification,:duration_days,:price, DEFAULT )";
        jdbcTemplate.update(SQL, namedParamsCreate(certificate));
    }

    @Override
    public void updateCertificate(int id, Certificate certificate) {
        String SQL = "UPDATE rest_api_basics.certificate SET " +
                "name = :name, " +
                "description = :description, " +
                "date_of_creation = :date_of_creation, " +
                "date_of_modification = :date_of_modification, " +
                "duration_days = :duration_days, " +
                "price = :price " +
                "WHERE id_certificate = :id";
        Map<String, Object> namedParameters = namedParamsCreate(certificate);
        namedParameters.put("id", id);
        jdbcTemplate.update(SQL, namedParameters);
    }

    @Override
    public void deleteCertificateById(int id) {
        String SQL = "DELETE FROM rest_api_basics.certificate WHERE id_certificate = :id";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        jdbcTemplate.update(SQL, namedParameters);
    }

    private Map<String, Object> namedParamsCreate(Certificate certificate) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("name", certificate.getName());
        namedParameters.put("description", certificate.getDescription());
        namedParameters.put("price", certificate.getPrice());
        namedParameters.put("date_of_creation", certificate.getCreationDate());
        namedParameters.put("date_of_modification", certificate.getModification());
        namedParameters.put("duration_days", certificate.getDurationDays());
        return namedParameters;
    }

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
