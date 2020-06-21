package com.epam.esm.dao.Impl;

import com.epam.esm.entity.Tag;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(locations = "classpath:test-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TagDAOJDBCTemplateTest {

    private List<Tag> tagsListActual;
    private List<Tag> tagsListExpected;
    private Tag tagActual;
    private Tag tagExpected;

    @Autowired
    private TagDAOJDBCTemplate tagDAOJDBCTemplate;

    @Before
    public void init() {
        tagsListExpected = new ArrayList<>();

        Tag allGreat = new Tag(1, "AllGreat");
        tagDAOJDBCTemplate.addTag(allGreat);
        tagsListExpected.add(allGreat);

        Tag liveIs = new Tag(2, "LiveIsWonderful");
        tagDAOJDBCTemplate.addTag(liveIs);
        tagsListExpected.add(liveIs);

        Tag playTheMan = new Tag(3, "PlayTheMan");
        tagDAOJDBCTemplate.addTag(playTheMan);
        tagsListExpected.add(playTheMan);

        tagExpected = new Tag(4,"BeStrong");
        tagActual = new Tag(5,"BeBest");

        tagsListActual = tagDAOJDBCTemplate.findAll();
    }

    @Test
    public void getAll_FindAllListOfTags_TagsListExpectedAreEqualWithTagListActual() {
        tagsListActual = tagDAOJDBCTemplate.findAll();
        Assert.assertEquals(tagsListExpected,tagsListActual);
    }

    @Test
    public void findTagById_FindTagByIdInMethodParameter_ActualTagIdAreEqualWithExpectedTagId() {
        tagExpected = tagsListActual.get(0);
        tagActual = tagDAOJDBCTemplate.findTagById(tagExpected.getId());
        Assert.assertEquals(tagExpected.getId(), tagActual.getId());
    }

    @Test
    public void addTag_AddTagWithTestData_TagMustBeContainInDatabaseAndInActualTagsList() {
        Tag addTag = tagActual;
        tagDAOJDBCTemplate.addTag(addTag);
        tagsListActual = tagDAOJDBCTemplate.findAll();
        Assert.assertTrue(tagsListActual.stream().anyMatch(tag -> tag.getName().equals(addTag.getName())));
    }

    @Test
    public void deleteTagById_FindAnDeleteTagById_TagMustBeRemovedFromDatabaseAndActualTagsList() {
        tagExpected = tagsListActual.get(0);
        tagDAOJDBCTemplate.deleteTagById(tagExpected.getId());
        tagsListActual = tagDAOJDBCTemplate.findAll();
        Assert.assertFalse(tagsListActual.stream()
                .anyMatch(tag -> tag.getName().equals(tagExpected.getName())));
    }

    @After
    public void destroy() {
        tagDAOJDBCTemplate.deleteAll();
    }
}