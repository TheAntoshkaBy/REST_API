package com.epam.esm.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
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
    public void initTagTest(){
        tags = new ArrayList<>();

        tags.add(new Tag(1,"AllGreat"));
        tags.add(new Tag(2,"LiveIsWonderful"));
        tags.add(new Tag(3,"PlayTheMan"));

        expectedTag = new Tag(4,"BeStrong");
    }

    @Test
    public void findTag_TagId_TagWhichContainTransmittedId() throws TagNotFoundException {
        when(service.find(anyInt())).thenReturn(expectedTag);

        actualResponseEntity = tagController.findTag(13);
        expectedResponseEntity = new ResponseEntity<>(expectedTag, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity,actualResponseEntity);
    }

    @Test
    public void findAll_findAllCreatedTags_AllTags() {
        when(service.findAll()).thenReturn(tags);

        actualResponseEntity = tagController.findAll();
        expectedResponseEntity = new ResponseEntity<>(tags, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity,actualResponseEntity);
    }

    @Test
    public void addTag_Tag_TagListWhichContainsTransmittedTag() {
        doAnswer(invocation -> {
            Object tag = invocation.getArgument(0);
            assertEquals(any(Tag.class), tag);
            tags.add(expectedTag);
            return null;
        }).when(service).create(expectedTag);

        when(service.findAll()).thenReturn(tags);

        actualResponseEntity = tagController.addTag(any(Tag.class));
        expectedResponseEntity = new ResponseEntity<>(tags, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity,actualResponseEntity);
    }

    @Test
    public void deleteTag_TagId_TagWhichNotContainTransmittedId() throws TagNotFoundException {
        doAnswer(invocation -> {
            Object tagId = invocation.getArgument(0);
            assertEquals(0, tagId);
            tags.remove(0);
            return null;
        }).when(service).delete(0);

        when(service.findAll()).thenReturn(tags);

        actualResponseEntity = tagController.deleteTag(0);
        expectedResponseEntity = new ResponseEntity<>(tags, HttpStatus.OK);

        Assert.assertEquals(expectedResponseEntity,actualResponseEntity);
    }
}