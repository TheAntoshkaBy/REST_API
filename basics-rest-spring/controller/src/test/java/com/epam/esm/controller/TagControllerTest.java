package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.exception.tag.TagNotFoundException;
import com.epam.esm.service.TagService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TagControllerTest {

    private TagService service;
    private TagController tagController;
    private List<Tag> tags;
    private Tag expectedTag;
    private ResponseEntity<?> actualResponseEntity;
    private ResponseEntity<?> expectedResponseEntity;

    @Before
    public void init() {
        service = mock(TagService.class);
        tagController = new TagController(service);
    }

    @Before
    public void initTagTest() {
        tags = new ArrayList<>();

        tags.add(new Tag(1, "AllGreat"));
        tags.add(new Tag(2, "LiveIsWonderful"));
        tags.add(new Tag(3, "PlayTheMan"));

        expectedTag = new Tag(4, "BeStrong");
    }

    @Test
    public void findTag_TagId_TagWhichContainTransmittedId() throws ServiceException {
        int tagId = 4;

        when(service.find(anyInt())).thenReturn(expectedTag);

        actualResponseEntity = tagController.findTag(tagId);
        expectedResponseEntity = new ResponseEntity<>(expectedTag, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void findAll_findAllCreatedTags_AllTags() {
        when(service.findAll()).thenReturn(tags);

        actualResponseEntity = tagController.findAll();
        expectedResponseEntity = new ResponseEntity<>(tags, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void addTag_Tag_TagListWhichContainsTransmittedTag() {
        int getFirst = 0;

        doAnswer(invocation -> {
            Object tag = invocation.getArgument(getFirst);
            assertEquals(any(Tag.class), tag);
            tags.add(expectedTag);
            return null;
        }).when(service).create(expectedTag);

        when(service.findAll()).thenReturn(tags);

        actualResponseEntity = tagController.addTag(any(Tag.class));
        expectedResponseEntity = new ResponseEntity<>(tags, HttpStatus.CREATED);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }

    @Test
    public void deleteTag_TagId_TagWhichNotContainTransmittedId() throws TagNotFoundException {
        int getFirst = 0;

        doAnswer(invocation -> {
            Object tagId = invocation.getArgument(getFirst);
            assertEquals(getFirst, tagId);
            tags.remove(getFirst);
            return null;
        }).when(service).delete(getFirst);

        when(service.findAll()).thenReturn(tags);

        actualResponseEntity = tagController.deleteTag(getFirst);
        expectedResponseEntity = new ResponseEntity<>(tags, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity, actualResponseEntity);
    }
}