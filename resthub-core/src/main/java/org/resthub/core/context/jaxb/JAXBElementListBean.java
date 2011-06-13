package org.resthub.core.context.jaxb;

import java.util.ArrayList;
import java.util.List;

public class JAXBElementListBean {

    private List<String> elements = new ArrayList<String>();

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }
}
