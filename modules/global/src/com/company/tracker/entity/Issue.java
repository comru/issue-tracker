package com.company.tracker.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.Categorized;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.security.entity.User;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.Listeners;

@Listeners("tracker_IssueListener")
@NamePattern("#getCaption|project,number")
@Table(name = "TRACKER_ISSUE")
@Entity(name = "tracker$Issue")
public class Issue extends StandardEntity implements Categorized {
    private static final long serialVersionUID = -4088994452625855713L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROJECT_ID")
    protected Project project;

    @Column(name = "NUMBER_", nullable = false)
    protected Long number;

    @Column(name = "SUMMARY", nullable = false, length = 1000)
    protected String summary;

    @Lob
    @Column(name = "DESCRIPTION")
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ASSIGNEE_ID")
    protected User assignee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CREATED_ID")
    protected User created;

    public void setCreated(User created) {
        this.created = created;
    }

    public User getCreated() {
        return created;
    }


    public void setProject(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public User getAssignee() {
        return assignee;
    }

    @MetaProperty
    @Override
    public Category getCategory() {
        return project == null ? null : project.getCategory();
    }

    @Override
    public void setCategory(Category category) {
        //empty implementation
    }

    public String getCaption() {
        return project == null ? String.valueOf(number) : project.getShortName() + "-" + number;
    }
}