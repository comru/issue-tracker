package com.company.tracker.web.user;

import com.haulmont.cuba.gui.app.security.user.browse.UserLookup;

import java.util.Map;

public class ExtUserLookup extends UserLookup {
    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        getDialogParams().setWidth(800).setHeight(600);
    }
}