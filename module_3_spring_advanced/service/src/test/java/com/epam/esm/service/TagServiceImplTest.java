package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.jpa.impl.TagRepositoryJPA;
import com.epam.esm.service.impl.ShopTagService;
import com.epam.esm.service.validator.TagValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TagServiceImplTest {

    private TagRepositoryJPA tagRepository;
    private List<Tag> tags;
    private Tag tag;
    private TagService tagService;
    private TagValidator tagValidator;

    @Before
    public void init() {
        tags = new ArrayList<>();
        tags.add(new Tag(1L, "AllGreat"));
        tags.add(new Tag(2L, "LiveIsWonderful"));
        tags.add(new Tag(3L, "PlayTheMan"));
        tag = new Tag(4L, "BeStrong");

        tagValidator = mock(TagValidator.class);
        tagRepository = mock(TagRepositoryJPA.class);
        //tagService = new ShopTagService(tagRepository, converter);
    }

    @Test
    public void findAll() {
        when(tagRepository.findAll(anyInt(), anyInt())).thenReturn(tags);

        /*List<Tag> tagsActual = tagService.findAll(1, 5).stream().map(
        ServiceSupporter::convertTagPojoToTag)
                .collect(Collectors.toList());
        assertEquals(tagsActual, tags);*/
    }

    @Test
    public void find() {
        Long getFirst = 2l;
        tag = tags.get(getFirst.intValue());
        when(tagRepository.findById(anyInt())).thenReturn(tags.get(0));

        assertEquals(tag, tags.get(getFirst.intValue()));
    }

    @Test
    public void delete() {
        Long tagId = 2L;
        Long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst.intValue());
            assertEquals(tagId.intValue(), id);
            tags.remove(tagId.intValue());
            return null;
        }).when(tagRepository).findById(anyInt());

        List<Tag> expectedTags = tags;

        tagService.delete(tagId);

        assertEquals(expectedTags, tags);
    }

    @Test
    public void create() {
        Long tagId = 2L;
        Long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument(getFirst.intValue());
            assertEquals(tagId.intValue(), id);
            tags.add(tag);
            return null;
        }).when(tagRepository).create(any(Tag.class));

        List<Tag> expectedTags = tags;

        tagService.setTagValidator(tagValidator);
        tagService.delete(tagId);

        assertEquals(expectedTags, tags);
    }
}