package com.epam.esm.dao.Impl;

import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.dao.constant.SQLRequests;
import com.epam.esm.dao.mapper.CertificateRowMapper;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateDAOJDBCTemplate implements CertificateDAO {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private TagDAOJDBCTemplate tagDAO;

    public CertificateDAOJDBCTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public Certificate findCertificateById(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        Certificate certificate = jdbcTemplate.queryForObject(
                SQLRequests.GET_CERTIFICATE_BY_ID, namedParameters, new CertificateRowMapper()
        );
        certificate.setTags(getAllTagsWhichBelowConcreteCertificate(certificate));
        return certificate;
    }

    @Override
    public List<Certificate> findAll() {
        List<Certificate> certificates = jdbcTemplate.query(
                SQLRequests.GET_ALL_CERTIFICATES, new CertificateRowMapper()
        );
        certificates.forEach(certificate -> certificate.
                setTags(getAllTagsWhichBelowConcreteCertificate(certificate)));
        return certificates;
    }

    @Override
    public void addCertificate(Certificate certificate) {
        jdbcTemplate.update(SQLRequests.ADD_CERTIFICATE, namedParamsCreate(certificate));
    }

    @Override
    public void updateCertificate(int id, Certificate certificate) {
        Map<String, Object> namedParameters = namedParamsCreate(certificate);
        namedParameters.put("id", id);
        jdbcTemplate.update(SQLRequests.UPDATE_CERTIFICATE, namedParameters);
    }

    @Override
    public void deleteCertificateById(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        jdbcTemplate.update(SQLRequests.DELETE_CERTIFICATE, namedParameters);
    }

    @Override
    public void addTag(int id, Tag tag) {
        int tagId = tagDAO.addTag(tag);
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id_certificate", id);
        namedParameters.put("id_tag", tagId);
        jdbcTemplate.update(SQLRequests.ADD_TAG_TO_CERTIFICATE, namedParameters);
    }

    @Override
    public void addTag(int idCertificate, int idTag) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id_certificate", idCertificate);
        namedParameters.put("id_tag", idTag);
        jdbcTemplate.update(SQLRequests.ADD_TAG_TO_CERTIFICATE, namedParameters);
    }

    @Override
    public void deleteTag(int idCertificate, int idTag) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id_certificate", idCertificate);
        namedParameters.put("id_tag", idTag);
        jdbcTemplate.update(SQLRequests.DELETE_TAG_FROM_CERTIFICATE, namedParameters);
    }

    @Override
    public List<Certificate> findCertificateWhereIdMoreThanParameter(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        List<Certificate> certificates = jdbcTemplate.query(
                SQLRequests.GET_ALL_CERTIFICATES_WHERE_ID_MORE_THAN_PARAMETER,
                namedParameters,
                new CertificateRowMapper()
        );
        certificates.forEach(certificate -> certificate.
                setTags(getAllTagsWhichBelowConcreteCertificate(certificate)));
        return certificates;
    }

    @Override
    public List<Certificate> findCertificateWhereTagNameIs(Tag tag) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("name", tag.getName());
        List<Certificate> certificates = jdbcTemplate.query(
                SQLRequests.FIND_CERTIFICATE_BY_TAG,
                namedParameters,
                new CertificateRowMapper()
        );
        certificates.forEach(certificate -> certificate.
                setTags(getAllTagsWhichBelowConcreteCertificate(certificate)));
        return certificates;
    }

    @Override
    public List<Certificate> findCertificateByNamePart(String text) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("text", text);
        List<Certificate> certificates = jdbcTemplate.query(
                SQLRequests.FIND_BY_PART_OF_NAME,
                namedParameters,
                new CertificateRowMapper()
        );
        certificates.forEach(certificate -> certificate.
                setTags(getAllTagsWhichBelowConcreteCertificate(certificate)));
        return certificates;
    }

    @Override
    public void deleteAll() {
        Map<String, Object> namedParameters = new HashMap<>();
        jdbcTemplate.update(SQLRequests.DELETE_ALL_CERTIFICATES,namedParameters);
    }

    private List<Tag> getAllTagsWhichBelowConcreteCertificate(Certificate certificate) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", certificate.getId());
        return jdbcTemplate.query(SQLRequests.GET_TAGS_BELOW_CONCRETE_CERTIFICATE, namedParameters, new TagRowMapper());
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

    @Autowired
    public CertificateDAOJDBCTemplate(NamedParameterJdbcTemplate jdbcTemplate, TagDAOJDBCTemplate tagDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagDAO = tagDAO;
    }

    public CertificateDAOJDBCTemplate() {
    }

    public TagDAOJDBCTemplate getTagDAO() {
        return tagDAO;
    }

    //fixme проверять тест cover (Clover).
}
