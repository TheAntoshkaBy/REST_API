package com.epam.esm.dao.Impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@ContextConfiguration(locations = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TagDAOJDBCTemplateTest {

    @Autowired
    private TagDAOJDBCTemplate tagDAOJDBCTemplate;

    @Test
    public void getAll() {
        System.out.println(tagDAOJDBCTemplate.getAll());
    }

    @Test
    public void getTagById() {
    }

    @Test
    public void addTag() {
    }

    @Test
    public void deleteTagById() {
    }
}