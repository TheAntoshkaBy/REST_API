package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.support.ControllerSupporter;
import com.epam.esm.pojo.UserPOJO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserList {

    @JsonIgnore
    private final static String NEXT_PAGE_MODEL_PARAM = "next";
    @JsonIgnore
    private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";
    @JsonIgnore
    private final static String CURRENT_PAGE_MODEL_PARAM = "current";
    private CollectionModel<EntityModel<UserDTO>> users;

    private UserList() {
    }

    public static class UserListBuilder {

        private final static String NEXT_PAGE_MODEL_PARAM = "next";
        private final static String PREVIOUS_PAGE_MODEL_PARAM = "previous";
        private final static String CURRENT_PAGE_MODEL_PARAM = "current";
        private final List<UserPOJO> usersPOJO;
        private List<UserDTO> userDTO;
        private int userCount = 0;
        private int page = 1;
        private int size = 5;
        private CollectionModel<EntityModel<UserDTO>> users;

        public UserListBuilder(List<UserPOJO> users) {
            this.usersPOJO = users;
        }

        public UserListBuilder page(int page) {
            this.page = page;
            return this;
        }

        public UserListBuilder size(int size) {
            this.size = size;
            return this;
        }

        public UserListBuilder resultCount(int resultCount) {
            this.userCount = resultCount;
            return this;
        }

        public UserList build() {
            UserList userList = new UserList();
            CollectionModel<EntityModel<UserDTO>> usersListModel = buildModelWithPagination();
            userList.setUsers(usersListModel);
            return userList;
        }

        private CollectionModel<EntityModel<UserDTO>> buildModelWithPagination() {
            this.userDTO = ControllerSupporter.userPojoListToUserDtoList(this.usersPOJO);

            this.users = CollectionModel.of(
                userDTO
                    .stream()
                    .map(UserDTO::getModel)
                    .collect(Collectors.toList())
            );

            if (userCount > page * size) {
                int nextPage = page + 1;
                this.users.add(linkTo(methodOn(UserController.class)
                    .findAll(nextPage, size)).withRel(NEXT_PAGE_MODEL_PARAM));
            }

            this.users.add(linkTo(methodOn(UserController.class)
                .findAll(page, size)).withRel(CURRENT_PAGE_MODEL_PARAM));

            if (page != 1) {
                int prevPage = page - 1;
                this.users.add(linkTo(methodOn(UserController.class)
                    .findAll(prevPage, size)).withRel(PREVIOUS_PAGE_MODEL_PARAM));
            }

            return this.users;
        }
    }
}
