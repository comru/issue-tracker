package com.company.tracker.web.project;

import com.company.tracker.entity.Project;
import com.haulmont.cuba.client.sys.cache.ClientCacheManager;
import com.haulmont.cuba.client.sys.cache.DynamicAttributesCacheStrategy;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCacheService;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.config.PermissionConfig;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.UUID;

public class ProjectEdit extends AbstractEditor<Project> {
    private static final String CREATE_CATEGORY_ACTION = "createCategory";
    private static final String UPDATE_CATEGORY_ACTION = "updateCategory";

    @Named("fieldGroup.category")
    private LookupPickerField categoryField;
    @Inject
    private Metadata metadata;
    @Inject
    protected DynamicAttributesCacheService dynamicAttributesCacheService;
    @Inject
    protected PermissionConfig permissionConfig;
    @Inject
    protected ClientCacheManager clientCacheManager;
    @Inject
    private CollectionDatasource<Category, UUID> categoriesDs;

    @Override
    public void init(Map<String, Object> params) {
        categoryField.addAction(new CreateCategoryAction());
        categoryField.addValueChangeListener(e -> {
            Category category = (Category) e.getValue();
            if (category == null) {
                categoryField.removeAction(UPDATE_CATEGORY_ACTION);
                categoryField.addAction(new CreateCategoryAction());
            } else {
                categoryField.removeAction(CREATE_CATEGORY_ACTION);
                categoryField.addAction(new UpdateCategoryAction());
            }
        });
    }

    private class CreateCategoryAction extends PickerField.StandardAction {
        public CreateCategoryAction() {
            super(CREATE_CATEGORY_ACTION, categoryField);
            setIcon("icons/create.png");
        }

        @Override
        public void actionPerform(Component component) {
            Project project = getItem();
            if (project == null) {
                return;
            }
            Category newCategory = metadata.create(Category.class);
            newCategory.setEntityType("tracker$Issue");
            String shortName = project.getShortName();
            newCategory.setName((StringUtils.isBlank(shortName) ? "UNKNOWN" : shortName.toUpperCase()) + "_ISSUE");
            AbstractEditor categoryEditor = openEditor("sys$Category.edit", newCategory, WindowManager.OpenType.THIS_TAB);
            categoryEditor.addCloseListener(actionId -> {
                Entity committedCategory = categoryEditor.getItem();
                if (Window.COMMIT_ACTION_ID.equals(actionId) && committedCategory instanceof Category) {
                    categoryField.setValue(committedCategory);
                    applyCategoryChanges();
                    categoriesDs.refresh();
                }
            });
        }
    }

    private class UpdateCategoryAction extends PickerField.StandardAction {
        public UpdateCategoryAction() {
            super(UPDATE_CATEGORY_ACTION, categoryField);
            setIcon("icons/edit.png");
        }

        @Override
        public void actionPerform(Component component) {
            Project project = getItem();
            if (project == null || project.getCategory() == null) {
                return;
            }
            Category currentCategory = project.getCategory();
            AbstractEditor categoryEditor = openEditor("sys$Category.edit", currentCategory, WindowManager.OpenType.THIS_TAB);
            categoryEditor.addCloseListener(actionId -> {
                if (WINDOW_COMMIT.equals(actionId)) {
                    applyCategoryChanges();
                }
            });
        }
    }

    private void applyCategoryChanges() {
        dynamicAttributesCacheService.loadCache();
        clientCacheManager.refreshCached(DynamicAttributesCacheStrategy.NAME);
        permissionConfig.clearConfigCache();
        showNotification(getMessage("notification.changesApplied"), NotificationType.HUMANIZED);
    }
}