package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.controller.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.GroupRepository;
import org.udg.pds.springtodo.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserService userService;

    public GroupRepository crud() {
        return groupRepository;
    }

    public Collection<Group> getGroups(Long id) {
        return userService.getUser(id).getGroups();
    }

    public Group getGroup(Long userId, Long id) {
        Optional<Group> g = groupRepository.findById(id);
        if (g.isEmpty()) throw new ServiceException("Task does not exists");
        if (!g.get().getUser().getId().equals(userId))
            throw new ServiceException("User does not own this task");
        return g.get();
    }

    @Transactional
    public IdObject addGroup(String name, Long userId, String description) {
        try {
            User user = userService.getUser(userId);

            Group group = new Group(name, description);

            group.setUser(user);

            user.addGroup(group);

            groupRepository.save(group);
            return new IdObject(group.getId());
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }

    @Transactional
    public void addUsersToGroup(Long userId, Long groupId, Collection<Long> users) {
        Group g = this.getGroup(userId, groupId);

        if (g.getUser().getId() != userId)
            throw new ServiceException("This user is not the owner of the task");

        try {
            for (Long memberId : users) {
                User user = userService.getUser(memberId);
                g.addUser(user);
            }
        } catch (Exception ex) {
            // Very important: if you want that an exception reaches the EJB caller, you have to throw an ServiceException
            // We catch the normal exception and then transform it in a ServiceException
            throw new ServiceException(ex.getMessage());
        }
    }


    public Collection<User> getGroupMember(Long userId, Long id) {
        Group g = this.getGroup(userId, id);
        User u = g.getUser();

        if (!u.getId().equals(userId))
            throw new ServiceException("Logged user does not own the task");

        return g.getUsers();
    }
}
