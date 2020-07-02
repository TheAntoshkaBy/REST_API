package com.epam.esm.dao.Impl;

import com.epam.esm.constant.ErrorTextMessageConstants;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.constant.SQLRequests;
import com.epam.esm.dao.mapper.TagDAORowMapper;
import com.epam.esm.entity.InvalidDataMessage;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.certificate.CertificateNotFoundException;
import com.epam.esm.exception.tag.TagInvalidDataException;
import com.epam.esm.exception.tag.TagNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class TagDAOJDBCTemplate implements TagDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TagDAOJDBCTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SQLRequests.FIND_ALL_TAGS, new TagDAORowMapper());
    }

    @Override
    public Tag findTagById(int id) throws TagNotFoundException {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);

        Tag tag;
        try {
            tag = jdbcTemplate.queryForObject(SQLRequests.FIND_TAG_BY_ID, namedParameters, new TagDAORowMapper());
        } catch (DataAccessException e) {
            throw new TagNotFoundException(
                    new InvalidDataMessage(ErrorTextMessageConstants.NOT_FOUND_TAG
                    ));
        }
        return tag;
    }

    @Override
    public int addTag(Tag tag) {
        final String FIELD = "tag_name";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(FIELD, tag.getName());

        try {
            jdbcTemplate.update(SQLRequests.ADD_TAG, sqlParameterSource, keyHolder);

        }catch (DuplicateKeyException e){
            throw new TagInvalidDataException(
                    new InvalidDataMessage(FIELD,ErrorTextMessageConstants.TAG_DUPLICATE_NAME
                    ));
        }

        return (int) Objects.requireNonNull(keyHolder.getKeys()).get("id_tag");
    }

    @Override
    public void deleteTagById(int id) throws TagNotFoundException {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);

        if (jdbcTemplate.update(SQLRequests.DELETE_TAG_BY_ID, namedParameters) == 0) {
            throw new TagNotFoundException(
                    new InvalidDataMessage(ErrorTextMessageConstants.NOT_FOUND_TAG
                    ));
        }
    }

    @Override
    public void deleteAll() throws TagNotFoundException {
        Map<String, Object> namedParameters = new HashMap<>();

        if (jdbcTemplate.update(SQLRequests.DELETE_ALL_TAGS, namedParameters) == 0) {
            throw new TagNotFoundException(
                    new InvalidDataMessage(ErrorTextMessageConstants.EMPTY_DATA
                    ));
        }
    }
}
