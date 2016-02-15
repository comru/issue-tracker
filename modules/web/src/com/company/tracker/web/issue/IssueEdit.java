package com.company.tracker.web.issue;

import com.company.tracker.entity.Issue;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.RuntimePropsDatasource;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class IssueEdit extends AbstractEditor<Issue> {
    @Inject
    private UserSession userSession;
    @Inject
    private RuntimePropsDatasource runtimePropsDs;
    @Named("fieldGroup.project")
    private PickerField projectField;
    @Inject
    private RuntimePropertiesFrame runtimePropertiesFrame;
    @Inject
    private VBoxLayout descriptionBox;
    @Inject
    private RichTextArea descriptionField;
    @Inject
    private Label descriptionLab;
    @Inject
    private TextField summaryField;
    @Inject
    private Button toggleViewEditDescrBtn;

    @Override
    public void init(Map<String, Object> params) {
        projectField.addValueChangeListener(e -> runtimePropsDs.refresh());

        runtimePropertiesFrame.setCategoryFieldEditable(false);
        runtimePropertiesFrame.setCategoryFieldVisible(false);
    }

    @Override
    protected void postInit() {
        Issue item = getItem();
        toggleViewEditDescr(!PersistenceHelper.isNew(item));
        summaryField.requestFocus();
    }

    @Override
    protected void initNewItem(Issue item) {
        super.initNewItem(item);
        if (item.getCreated() == null) {
            item.setCreated(userSession.getCurrentOrSubstitutedUser());
        }
    }

    public void editDescription() {
        toggleViewEditDescr(summaryField.isVisible());
    }

    private void toggleViewEditDescr(boolean toView) {
        if (toView) {
            summaryField.setVisible(false);
            descriptionField.setVisible(false);
            descriptionLab.setVisible(true);

            String value = "<h2><b>" + StringUtils.defaultString(summaryField.getValue()) + "</b></h2><br>" +
                    StringUtils.defaultString(descriptionField.getValue());
            descriptionLab.setValue(value);
            toggleViewEditDescrBtn.setCaption(getMessage("editDescription"));

            descriptionBox.expand(descriptionLab);
        } else {
            summaryField.setVisible(true);
            descriptionField.setVisible(true);
            descriptionLab.setVisible(false);
            toggleViewEditDescrBtn.setCaption(getMessage("viewDescription"));

            descriptionBox.expand(descriptionField);
        }
    }
}