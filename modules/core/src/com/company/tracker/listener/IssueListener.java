package com.company.tracker.listener;

import com.company.tracker.entity.Issue;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.eclipse.persistence.internal.descriptors.changetracking.AttributeChangeListener;
import org.eclipse.persistence.internal.sessions.ObjectChangeSet;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.beans.PropertyChangeListener;
import java.util.List;

@Component("tracker_IssueListener")
public class IssueListener implements BeforeInsertEntityListener<Issue>, BeforeUpdateEntityListener<Issue> {
    @Inject
    protected UniqueNumbersAPI uniqueNumbersAPI;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected Persistence persistence;

    @Override
    public void onBeforeInsert(Issue entity) {
        if (entity.getNumber() == null) {
            checkProjectAndSetNumber(entity);
        }
    }

    @Override
    public void onBeforeUpdate(Issue issue) {
        if (isProjectChange(issue)) {
            checkProjectAndSetNumber(issue);
        }
    }

    private boolean isProjectChange(@Nonnull Issue currIssue) {
        PropertyChangeListener propertyChangeListener = currIssue._persistence_getPropertyChangeListener();
        if (!(propertyChangeListener instanceof AttributeChangeListener)) {
            return false;
        }
        ObjectChangeSet objectChangeSet = ((AttributeChangeListener) propertyChangeListener).getObjectChangeSet();
        if (objectChangeSet == null) {
            return false;
        }
        List<String> changedAttributeNames = objectChangeSet.getChangedAttributeNames();
        return changedAttributeNames.contains("project");
    }

    private void checkProjectAndSetNumber(Issue entity) {
        Preconditions.checkNotNullArgument(entity.getProject(), "Project is null. Issue: " + entity.getInstanceName());
        Preconditions.checkNotNullArgument(entity.getProject().getShortName(), "Project name is null: " + entity.getInstanceName());
        String sequenceName = "seq_" + entity.getProject().getShortName() + "_issue";
        entity.setNumber(uniqueNumbersAPI.getNextNumber(sequenceName));
    }
}