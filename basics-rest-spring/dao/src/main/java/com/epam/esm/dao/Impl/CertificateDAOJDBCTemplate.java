package com.epam.esm.dao.Impl;

import com.epam.esm.constant.ErrorTextMessageConstants;
import com.epam.esm.dao.CertificateDAO;
import com.epam.esm.constant.SQLRequests;
import com.epam.esm.dao.mapper.CertificateDAORowMapper;
import com.epam.esm.dao.mapper.TagDAORowMapper;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.InvalidDataMessage;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

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

    @Autowired
    public CertificateDAOJDBCTemplate(NamedParameterJdbcTemplate jdbcTemplate, TagDAOJDBCTemplate tagDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagDAO = tagDAO;
    }

    public CertificateDAOJDBCTemplate() {
    }

    @Override
    public Certificate findCertificateById(int id) throws CertificateNotFoundException {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);
        Certificate certificate;
        try {
            certificate = jdbcTemplate.queryForObject(
                    SQLRequests.FIND_CERTIFICATE_BY_ID, namedParameters, new CertificateDAORowMapper()
            );
        } catch (DataAccessException e) {
            throw new CertificateNotFoundException(new InvalidDataMessage
                    (ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE));
        }

        certificate.setTags(findAllTagsWhichBelowConcreteCertificate(certificate));

        return certificate;
    }

    @Override
    public List<Certificate> findAll() throws CertificateNotFoundException {
        List<Certificate> certificates;

        try {
            certificates = jdbcTemplate.query(
                    SQLRequests.FIND_ALL_CERTIFICATES, new CertificateDAORowMapper()
            );
        } catch (DataAccessException e) {
            throw new CertificateNotFoundException(
                    new InvalidDataMessage(ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE
                    ));
        }

        certificates.forEach(certificate -> certificate.
                setTags(findAllTagsWhichBelowConcreteCertificate(certificate)));

        return certificates;
    }

    @Override
    public List<Certificate> findAllByDate() throws CertificateNotFoundException {
        List<Certificate> certificates;

        try {
            certificates = jdbcTemplate.query(
                    SQLRequests.FIND_ALL_CERTIFICATES_BY_DATE, new CertificateDAORowMapper()
            );
        } catch (DataAccessException e) {
            throw new CertificateNotFoundException(
                    new InvalidDataMessage(ErrorTextMessageConstants.EMPTY_DATA)
            );
        }

        certificates.forEach(certificate -> certificate.
                setTags(findAllTagsWhichBelowConcreteCertificate(certificate)));

        return certificates;
    }

    @Override
    public List<Certificate> findCertificateWhereIdMoreThanParameter(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);

        List<Certificate> certificates = jdbcTemplate.query(
                SQLRequests.FIND_ALL_CERTIFICATES_WHERE_ID_MORE_THAN_PARAMETER,
                namedParameters,
                new CertificateDAORowMapper()
        );

        certificates.forEach(certificate -> certificate.
                setTags(findAllTagsWhichBelowConcreteCertificate(certificate)));

        return certificates;
    }

    @Override
    public List<Certificate> findCertificateWhereTagNameIs(Tag tag) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("name", tag.getName());

        List<Certificate> certificates = jdbcTemplate.query(
                SQLRequests.FIND_CERTIFICATE_BY_TAG_NAME,
                namedParameters,
                new CertificateDAORowMapper()
        );

        certificates.forEach(certificate -> certificate.
                setTags(findAllTagsWhichBelowConcreteCertificate(certificate)));

        return certificates;
    }

    @Override
    public List<Certificate> findCertificateByNamePart(String text) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("text", text);

        List<Certificate> certificates = jdbcTemplate.query(
                SQLRequests.FIND_BY_PART_OF_NAME,
                namedParameters,
                new CertificateDAORowMapper()
        );

        certificates.forEach(certificate -> certificate.
                setTags(findAllTagsWhichBelowConcreteCertificate(certificate)));

        return certificates;
    }

    @Override
    public void addCertificate(Certificate certificate) {
        jdbcTemplate.update(SQLRequests.ADD_CERTIFICATE, namedParamsCreate(certificate));
    }

    @Override
    public void addTag(int idCertificate, int idTag) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id_certificate", idCertificate);
        namedParameters.put("id_tag", idTag);

        jdbcTemplate.update(SQLRequests.ADD_TAG_TO_CERTIFICATE, namedParameters);
    }

    @Override
    public void updateCertificate(int id, Certificate certificate) throws CertificateNotFoundException {
        Map<String, Object> namedParameters = namedParamsCreate(certificate);
        namedParameters.put("id", id);

        if (jdbcTemplate.update(SQLRequests.UPDATE_CERTIFICATE, namedParameters) == 0) {
            throw new CertificateNotFoundException(new InvalidDataMessage(
                    ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE
            ));
        }
    }

    @Override
    public void deleteCertificateById(int id) throws CertificateNotFoundException {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);

        if (jdbcTemplate.update(SQLRequests.DELETE_CERTIFICATE, namedParameters) == 0) {
            throw new CertificateNotFoundException(new InvalidDataMessage(
                    ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE
            ));
        }
    }

    @Override
    public void deleteTag(int idCertificate, int idTag) throws CertificateNotFoundException {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id_certificate", idCertificate);
        namedParameters.put("id_tag", idTag);

        if (jdbcTemplate.update(SQLRequests.DELETE_TAG_FROM_CERTIFICATE, namedParameters) == 0) {
            throw new CertificateNotFoundException(new InvalidDataMessage(
                    ErrorTextMessageConstants.NOT_FOUND_CERTIFICATE_OR_TAG_BY_ID
            ));
        }
    }

    @Override
    public void deleteAll() {
        Map<String, Object> namedParameters = new HashMap<>();

        jdbcTemplate.update(SQLRequests.DELETE_ALL_CERTIFICATES, namedParameters);
    }

    private List<Tag> findAllTagsWhichBelowConcreteCertificate(Certificate certificate) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", certificate.getId());

        return jdbcTemplate.query(
                SQLRequests.FIND_TAGS_BELOW_CONCRETE_CERTIFICATE, namedParameters, new TagDAORowMapper()
        );
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

    public TagDAOJDBCTemplate getTagDAO() {
        return tagDAO;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
