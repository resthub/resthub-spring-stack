/*
 * Copyright (c) 2010 nunux.org. All rights reserved.
 */
package org.resthub.roundtable.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.resthub.core.domain.model.Resource;

/**
 * Poll.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Entity
@Table(name = "poll")
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Poll extends Resource {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String author;
    private String topic;
    private String body;
    private Type type;
    private List<Answer> answers = new ArrayList<Answer>();
    private Set<Vote> votes = new HashSet<Vote>();
    private Date expirationDate;

    /**
     * Default constructor.
     */
    public Poll() {
        super();
    }

    public enum Type {
        SINGLE,
        MULTI
    }

    /**
     * @return the text
     */
    @Column(name = "body", nullable = false)
    public String getBody() {
        return this.body;
    }

    /**
     * @param text the text to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the type
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public Type getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * @return the answers
     */
    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Answer> getAnswers() {
        return answers;
    }

    /**
     * @param answers the answers to set
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    /**
     * @return the votes
     */
    @XmlTransient
    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<Vote> getVotes() {
        return votes;
    }

    /**
     * @param votes the votes to set
     */
    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    /**
     * @return the author
     */
    @Column(name = "author", nullable = false)
    public String getAuthor() {
        return this.author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the expiration date
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_date", nullable = false)
    public Date getExpirationDate() {
        return this.expirationDate;
    }

    /**
     * @param expirationDate the expiration date to set
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the topic
     */
    @Column(name = "topic", nullable = false)
    public String getTopic() {
        return this.topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Poll: {#").append(this.getId());
        result.append(", topic=").append(this.topic);
        result.append(", author=").append(this.author);
        result.append("}");
        return result.toString();
    }
}
