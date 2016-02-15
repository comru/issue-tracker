package com.company.tracker.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.Category;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;
import java.util.LinkedHashSet;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s (%s)|name,shortName")
@Table(name = "TRACKER_PROJECT")
@Entity(name = "tracker$Project")
public class Project extends StandardEntity {
    private static final long serialVersionUID = -5895145931828209970L;

    @Column(name = "SHORT_NAME", nullable = false, unique = true, length = 10)
    protected String shortName;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category;

    @JoinTable(name = "TRACKER_PROJECT_USER_LINK",
        joinColumns = @JoinColumn(name = "PROJECT_ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    protected LinkedHashSet<User> team;

    public void setTeam(LinkedHashSet<User> team) {
        this.team = team;
    }

    public LinkedHashSet<User> getTeam() {
        return team;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }


}