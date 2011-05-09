package org.resthub.core.tools;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author bmeurant <Baptiste Meurant>
 */
@XmlRootElement
public class BeanDetail {

	private String beanName;
	private String beanType;
	private boolean lazyInit;
	private boolean abstractBean;
	private boolean singleton;
	private boolean prototype;
	private boolean proxied;
	private List<String> aliases = new ArrayList<String>();
	private Object bean;
	private String description;
	private String scope;

	@XmlElement(required = true, name = "name")
	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@XmlElement(required = true, name = "type")
	public String getBeanType() {
		return this.beanType;
	}

	@XmlElement(required = true)
	public boolean isPrototype() {
		return this.prototype;
	}

	@XmlElement(required = true)
	public boolean isSingleton() {
		return this.singleton;
	}

	public void setBeanType(String beanType) {
		this.beanType = beanType;
	}

	@XmlElement(name = "alias", required = false)
	@XmlElementWrapper(name = "aliases")
	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public void setPrototype(boolean prototype) {
		this.prototype = prototype;
	}

	@XmlTransient
	public Object getBean() {
		return bean;
	}

	@XmlElement(required = true)
	public String getDescription() {
		String result = "";
		
		if ((this.description == null) || (this.description.isEmpty())) {
		    if (this.bean != null) {
                result = this.bean.toString();
            }
		}
		else {
		    result = this.description;
		}
		
		return result;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public void setProxied(boolean proxied) {
		this.proxied = proxied;
	}

	@XmlElement(required = true)
	public boolean isProxied() {
		return proxied;
	}

	@XmlElement(required=true)
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@XmlElement(required=true)
	public boolean isLazyInit() {
		return lazyInit;
	}

	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	@XmlElement(required=true)
	public boolean isAbstract() {
		return abstractBean;
	}

	public void setAbstract(boolean abstractBean) {
		this.abstractBean = abstractBean;
	}

	@Override
	public String toString() {
		return "BeanDetails [name: " + getBeanName() + ", aliases: "
				+ getAliases() + ", type: " + getBeanType() + ", prototype: "
				+ isPrototype() + ", singleton: " + isSingleton() + "]";
	}

}
