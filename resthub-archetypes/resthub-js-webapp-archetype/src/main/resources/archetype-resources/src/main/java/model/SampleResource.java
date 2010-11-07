#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model;


import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.resthub.core.model.Resource;

@Entity
@XmlRootElement
public class SampleResource extends Resource {

    private String name;

    public SampleResource() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

   
}
