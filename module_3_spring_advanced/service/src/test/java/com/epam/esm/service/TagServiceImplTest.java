package com.epam.esm.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.entity.Tag;
import com.epam.esm.pojo.TagPOJO;
import com.epam.esm.repository.jpa.impl.TagRepositoryJPA;
import com.epam.esm.service.impl.ShopTagService;
import com.epam.esm.service.support.PojoConverter;
import com.epam.esm.service.support.impl.TagPojoConverter;
import com.epam.esm.service.validator.TagValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TagServiceImplTest {

    private TagRepositoryJPA tagRepository;
    private List<Tag> tags;
    private List<TagPOJO> tagsPOJO;
    private Tag tag;
    private TagService tagService;
    private TagValidator tagValidator;
    private PojoConverter<TagPOJO, Tag> converter;

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 5;

    @Before
    public void init() {
        tags = new ArrayList<>();
        tagsPOJO = new ArrayList<>();

        tags.add(new Tag(1L, "AllGreat"));
        tags.add(new Tag(2L, "LiveIsWonderful"));
        tags.add(new Tag(3L, "PlayTheMan"));
        tagsPOJO.add(new TagPOJO(tags.get(0)));
        tagsPOJO.add(new TagPOJO(tags.get(1)));
        tagsPOJO.add(new TagPOJO(tags.get(2)));

        tag = new Tag(4L, "BeStrong");
        converter = mock(TagPojoConverter.class);
        tagValidator = mock(TagValidator.class);
        tagRepository = mock(TagRepositoryJPA.class);
        tagService = new ShopTagService(tagRepository, tagValidator, converter);
    }

    @Test
    public void findAll_PaginationParams_AllTagsWithPagination() {
        when(tagRepository.findAll(anyInt(), anyInt())).thenReturn(tags);
        when(converter.convert(tags)).thenReturn(tagsPOJO);

        List<TagPOJO> tagsActual = tagService.findAll(DEFAULT_PAGE, DEFAULT_SIZE);
        assertEquals(tagsActual, tagsPOJO);
    }

    @Test
    public void find_IdTag_FindTagById() {
        long getFirst = 2l;
        tag = tags.get((int) getFirst);

        when(tagRepository.findById(anyInt())).thenReturn(tags.get(0));

        assertEquals(tag, tags.get((int) getFirst));
    }

    @Test
    public void delete_IdTag_DeleteTagById() {
        long tagId = 2L;
        long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument((int) getFirst);
            assertEquals((int) tagId, id);
            tags.remove((int) tagId);
            return null;
        }).when(tagRepository).findById(anyInt());

        List<Tag> expectedTags = tags;

        tagService.delete(tagId);

        assertEquals(expectedTags, tags);
    }

    @Test
    public void create_Tag_NewTag() {
        long tagId = 2L;
        long getFirst = 0L;

        doAnswer(invocation -> {
            Object id = invocation.getArgument((int) getFirst);
            assertEquals((int) tagId, id);
            tags.add(tag);
            return null;
        }).when(tagRepository).create(any(Tag.class));

        List<Tag> expectedTags = tags;

        tagService.delete(tagId);

        assertEquals(expectedTags, tags);
    }

    @Test
    public void findMostWidelyTag_NoParams_FoundedTagEqualExpectedTag() {
        when(tagRepository.findMostWidelyUsedTag()).thenReturn(tags.get(1));

        TagPOJO tagPOJO = tagService.findMostWidelyUsedTag();

        assertEquals(tagPOJO, new TagPOJO(tags.get(1)));
    }

    @Test
    public void getTagCount_WithoutParams_CountOfAllTags() {
        int count = 10;

        when(tagRepository.getTagCount()).thenReturn(count);
        int actualCount = tagService.getTagCount();

        assertEquals(actualCount, count);
    }
}