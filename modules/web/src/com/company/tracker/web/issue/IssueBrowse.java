package com.company.tracker.web.issue;

import com.company.tracker.entity.Issue;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.User;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class IssueBrowse extends AbstractLookup {
    @Inject
    private Table<Issue> issuesTable;
    @Inject
    private ComponentsFactory componentsFactory;
    @Named("issuesTable.edit")
    private EditAction issuesTableEdit;

    @Override
    public void init(Map<String, Object> params) {
        issuesTable.addGeneratedColumn("description", entity -> {
            VBoxLayout contentLayout = componentsFactory.createComponent(VBoxLayout.class);
            contentLayout.setWidth("100%");
            contentLayout.setSpacing(true);

            HBoxLayout mainProperties = componentsFactory.createComponent(HBoxLayout.class);
            mainProperties.setWidth("100%");
            mainProperties.setSpacing(true);
            contentLayout.add(mainProperties);

            LinkButton issueNameLink = componentsFactory.createComponent(LinkButton.class);
            issueNameLink.setCaption(entity.getCaption());
            issueNameLink.setAlignment(Alignment.TOP_LEFT);
            issueNameLink.setAction(issuesTableEdit);
            mainProperties.add(issueNameLink);

            Label summaryLab = componentsFactory.createComponent(Label.class);
            summaryLab.setValue(entity.getSummary());
            summaryLab.setAlignment(Alignment.TOP_LEFT);
            mainProperties.add(summaryLab);
            mainProperties.expand(summaryLab);

            Label createdLab = componentsFactory.createComponent(Label.class);
            createdLab.setValue(entity.getCreated());
            createdLab.setDescription(String.format(getMessage("createdDescription"), entity.getCreatedBy(), entity.getCreateTs()));
            createdLab.setAlignment(Alignment.TOP_RIGHT);
            mainProperties.add(createdLab);

            Label updatedLab = componentsFactory.createComponent(Label.class);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            updatedLab.setValue(dateFormat.format(entity.getUpdateTs()));
            updatedLab.setDescription(String.format(getMessage("updatedDescription"), entity.getUpdatedBy(), entity.getUpdateTs()));
            updatedLab.setAlignment(Alignment.TOP_RIGHT);
            mainProperties.add(updatedLab);

            // advanced
            HBoxLayout advancedProperties = componentsFactory.createComponent(HBoxLayout.class);
            advancedProperties.setWidth("-1px");
            advancedProperties.setSpacing(true);
            contentLayout.add(advancedProperties);

            Label assigneeLab = componentsFactory.createComponent(Label.class);
            User assignee = entity.getAssignee();
            assigneeLab.setValue(assignee);
            assigneeLab.setDescription(getMessage("assignee") + ": " + assignee.getInstanceName());
            assigneeLab.setAlignment(Alignment.TOP_LEFT);
            advancedProperties.add(assigneeLab);

            createDynamicAttributes(entity, advancedProperties);

            return contentLayout;
        });
    }

    private void createDynamicAttributes(Issue issue, HBoxLayout advancedProperties) {
        Map<String, CategoryAttributeValue> dynamicAttributes = issue.getDynamicAttributes();
        if (dynamicAttributes == null) {
            return;
        }
        for (Map.Entry<String, CategoryAttributeValue> entry : dynamicAttributes.entrySet()) {
            Label dynamicAttrLab = componentsFactory.createComponent(Label.class);
            CategoryAttributeValue categoryAttributeValue = entry.getValue();
            CategoryAttribute categoryAttribute = categoryAttributeValue.getCategoryAttribute();
            String attrValue = convertToString(categoryAttributeValue);
            dynamicAttrLab.setValue(attrValue);
            dynamicAttrLab.setDescription(categoryAttribute.getName() + ": " + attrValue);
            dynamicAttrLab.setAlignment(Alignment.TOP_LEFT);
            advancedProperties.add(dynamicAttrLab);
        }
    }

    @Nullable
    private String convertToString(CategoryAttributeValue categoryAttributeValue) {
        Object value = categoryAttributeValue.getValue();
        if (value == null) {
            return null;
        } else if (value instanceof Entity) {
            return ((Entity) value).getInstanceName();
        }
        return value.toString();
    }
}