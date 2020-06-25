package com.epam.esm.service;

import com.epam.esm.dao.Impl.TagDAOJDBCTemplate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.service.Impl.TagServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {

    private TagDAOJDBCTemplate tagDAOJDBCTemplate;
    private List<Tag> tags;
    private Tag tag;
    private TagService tagService;

    @Before
    public void init() {
        tags = new ArrayList<>();
        tags.add(new Tag(1, "AllGreat"));
        tags.add(new Tag(2, "LiveIsWonderful"));
        tags.add(new Tag(3, "PlayTheMan"));
        tag = new Tag(4, "BeStrong");
        tagDAOJDBCTemplate = mock(TagDAOJDBCTemplate.class);
    }

    @Test
    public void findAll() {
        when(tagDAOJDBCTemplate.findAll()).thenReturn(tags);
        tagService = new TagServiceImpl(tagDAOJDBCTemplate);
        List<Tag> tagsActual = tagService.findAll();
        Assert.assertEquals(tagsActual, tags);
    }

    @Test
    public void find() throws TagNotFoundException {
        Integer tagId = 1;
        Integer getFirst = 0;

        when(tagDAOJDBCTemplate.findTagById(anyInt())).thenReturn(tags.get(0));

        tagService = new TagServiceImpl(tagDAOJDBCTemplate);
        Tag tagActual = tagService.find(tagId);

        Assert.assertEquals(tagActual, tags.get(getFirst));
    }

    @Test
    public void delete() throws TagNotFoundException {
        Integer tagId = 2;
        Integer getFirst = 0;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst);
            assertEquals(tagId, id);
            tags.remove(tagId);
            return null;
        }).when(tagDAOJDBCTemplate).deleteTagById(anyInt());

        List<Tag> expectedTags = tags;

        tagService = new TagServiceImpl(tagDAOJDBCTemplate);
        tagService.delete(tagId);

        assertEquals(expectedTags, tags);
    }

    @Test
    public void create() throws TagNotFoundException {
        Integer tagId = 2;
        Integer getFirst = 0;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst);
            assertEquals(tagId, id);
            tags.add(tag);
            return null;
        }).when(tagDAOJDBCTemplate).addTag(any(Tag.class));

        List<Tag> expectedTags = tags;

        tagService = new TagServiceImpl(tagDAOJDBCTemplate);
        tagService.delete(tagId);

        assertEquals(expectedTags, tags);
    }
}