package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateRowMapper implements RowMapper<Certificate> {
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