package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name="usergroup")
public class Group implements Serializable {

    public Group() {
    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user")
    private User user;

    @Column(name = "fk_user", insertable = false, updatable = false)
    private Long userId;

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<User> users = new ArrayList<>();

    @JsonView(Views.Private.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addUser(User user) {
        users.add(user);
    }


    @JsonView(Views.Complete.class)
    public Collection<User> getUsers() {
        users.size();
        return users;
    }

    @JsonView(Views.Private.class)
    public String getName() {
        return name;
    }

    @JsonView(Views.Private.class)
    public String getDescription() {
        return description;
    }

    @JsonView(Views.Complete.class)
    public long getUserId() {
        return userId;
    }

}
