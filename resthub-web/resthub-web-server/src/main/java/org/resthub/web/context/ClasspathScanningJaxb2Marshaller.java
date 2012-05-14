package org.resthub.web.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class ClasspathScanningJaxb2Marshaller extends Jaxb2Marshaller {
    private static final Logger log = LoggerFactory.getLogger(ClasspathScanningJaxb2Marshaller.class);

    private List<String> basePackages;

    public List<String> getBasePackages() { return basePackages; }

    public void setBasePackages(List<String> basePackages) { this.basePackages = basePackages; }

    @PostConstruct
    public void init() throws Exception {
        setClassesToBeBound(getXmlRootElementClasses());
    }

    private Class[] getXmlRootElementClasses() throws Exception {
        ClassPathScanningCandidateComponentProvider scanner =
            new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(XmlRootElement.class));

        List<Class> classes = new ArrayList<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> definitions = scanner.findCandidateComponents(basePackage);
            for (BeanDefinition definition : definitions) {
                String className = definition.getBeanClassName();
                log.info("Found class: {}", className);
                classes.add(Class.forName(className));
            }
        }

        return classes.toArray(new Class[0]);
    }
    
    
}
