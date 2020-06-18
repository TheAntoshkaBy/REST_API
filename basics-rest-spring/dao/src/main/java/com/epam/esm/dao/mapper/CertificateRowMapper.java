package com.epam.esm.dao.mapper;

import com.epam.esm.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CertificateRowMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet certificateResultSet, int index) throws SQLException {
        return new Certificate(
                certificateResultSet.getInt("id_certificate"),
                certificateResultSet.getString("name"),
                certificateResultSet.getString("description"),
                certificateResultSet.getDouble("price"),
                certificateResultSet.getDate("date_of_creation"),
                certificateResultSet.getDate("date_of_modification"),
                certificateResultSet.getInt("duration_days")
        );
    }
}