package org.resthub.web.view;

import org.resthub.common.view.DataView;

public class PojoView implements DataView {

    private final Object data;
    private final Class<?> view;

    public PojoView(Object data, Class<?> view) {
        this.data = data;
        this.view = view;
    }

    @Override
    public boolean hasView() {
        return true;
    }

    @Override
    public Class<?> getView() {
        return view;
    }

    @Override
    public Object getData() {
        return data;
    }
}
