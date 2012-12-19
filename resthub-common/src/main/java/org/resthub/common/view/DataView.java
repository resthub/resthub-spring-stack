package org.resthub.common.view;

public interface DataView {
    boolean hasView();
    Class<?> getView();
    Object getData();
}
