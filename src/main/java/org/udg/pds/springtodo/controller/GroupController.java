package org.udg.pds.springtodo.controller;


import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.controller.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.service.GroupService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RequestMapping(path="/groups")
@RestController
public class GroupController extends BaseController{
    @Autowired
    GroupService groupService;

    @GetMapping(path="/{id}")
    public Group getGroup(HttpSession session, @PathVariable("id") Long id) {
        Long userId = getLoggedUser(session);

        return groupService.getGroup(userId, id);
    }

    @GetMapping
    @JsonView(Views.Private.class)
    public Collection<Group> listAllGroups(HttpSession session) {
        Long userId = getLoggedUser(session);

        return groupService.getGroups(userId);
    }

    @PostMapping(consumes = "application/json")
    public IdObject addGroup(HttpSession session, @Valid @RequestBody GroupController.R_Group group) {

        Long userId = getLoggedUser(session);

        return groupService.addGroup(group.name, userId, group.description);
    }

    @DeleteMapping(path="/{id}")
    public String deleteGroup(HttpSession session, @PathVariable("id") Long groupId) {
        getLoggedUser(session);
        groupService.crud().deleteById(groupId);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/{id}/tags")
    public Collection<User> getGroupMember(HttpSession session, @PathVariable("id") Long groupId) {

        Long userId = getLoggedUser(session);
        return groupService.getGroupMember(userId, groupId);
    }

    static class R_Group {

        @NotNull
        public String name;

        @NotNull
        public String description;
    }
}
