package com.epam.esm.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.support.ControllerParamNames;
import com.epam.esm.controller.support.DtoConverter;
import com.epam.esm.pojo.UserPOJO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserList {

    private CollectionModel<EntityModel<UserDTO>> users;

    private UserList() {
    }

    public static class UserListBuilder {

        private final List<UserPOJO> usersPOJO;
        private final DtoConverter<UserDTO, UserPOJO> converter;
        private int userCount = 0;
        private int page = ControllerParamNames.DEFAULT_PAGE;
        private int size = ControllerParamNames.DEFAULT_SIZE;

        public UserListBuilder(List<UserPOJO> users,
            DtoConverter<UserDTO, UserPOJO> converter) {
            this.usersPOJO = users;
            this.converter = converter;
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
            List<UserDTO> userDTO = converter.convert(this.usersPOJO);

            CollectionModel<EntityModel<UserDTO>> users = CollectionModel.of(
                userDTO
                    .stream()
                    .map(user -> user.getModel(page, size))
                    .collect(Collectors.toList())
            );

            if (userCount > page * size) {
                int nextPage = page + 1;
                users.add(linkTo(methodOn(UserController.class)
                    .findAll(nextPage, size)).withRel(ControllerParamNames.NEXT_PAGE_MODEL_PARAM));
            }

            users.add(linkTo(methodOn(UserController.class)
                .findAll(page, size)).withRel(ControllerParamNames.CURRENT_PAGE_MODEL_PARAM));

            if (page != 1) {
                int prevPage = page - 1;
                users.add(linkTo(methodOn(UserController.class)
                    .findAll(prevPage, size)).withRel(
                    ControllerParamNames.PREVIOUS_PAGE_MODEL_PARAM));
            }

            int lastPage;
            if (userCount > size) {
                lastPage = userCount / size;
                if (lastPage % size != 0) {
                    lastPage += 1;
                }
            }else {
                lastPage = 1;
            }

            users.add(linkTo(methodOn(UserController.class)
                .findAll(lastPage, size))
                .withRel(ControllerParamNames.LAST_PAGE_MODEL_PARAM));

            return users;
        }
    }
}
