package com.epam.esm.dao.Impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.constant.SQLRequests;
import com.epam.esm.dao.mapper.TagDAORowMapper;
import com.epam.esm.entity.Tag;
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
    public Tag findTagById(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);

        return jdbcTemplate.queryForObject(SQLRequests.FIND_TAG_BY_ID, namedParameters, new TagDAORowMapper());
    }

    @Override
    public int addTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("tag_name", tag.getName());

        jdbcTemplate.update(SQLRequests.ADD_TAG, sqlParameterSource, keyHolder);

        return (int) Objects.requireNonNull(keyHolder.getKeys()).get("id_tag");
    }

    @Override
    public void deleteTagById(int id) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", id);

        jdbcTemplate.update(SQLRequests.DELETE_TAG_BY_ID, namedParameters);
    }

    @Override
    public void deleteAll() {
        Map<String, Object> namedParameters = new HashMap<>();

        jdbcTemplate.update(SQLRequests.DELETE_ALL_TAGS, namedParameters);
    }

}
