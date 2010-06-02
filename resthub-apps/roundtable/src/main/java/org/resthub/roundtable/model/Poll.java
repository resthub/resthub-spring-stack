/*
 * Copyright (c) 2010 nunux.org. All rights reserved.
 */
package org.resthub.roundtable.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import org.resthub.core.model.Resource;

/**
 * Poll.
 * @author Nicolas Carlier
 */
@Indexed
@Entity
@Table(name = "poll")
@Access(AccessType.FIELD)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Poll extends Resource {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @Column(name = "author", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String author;

    @Column(name = "topic", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String topic;

    @Column(name = "body", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String body;

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @IndexedEmbedded
    private List<Answer> answers = new ArrayList<Answer>();

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Voter> voters = new HashSet<Voter>();
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    /**
     * Default constructor.
     */
    public Poll() {
        super();
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Set<Voter> getVoters() {
        return voters;
    }

    public void setVoters(Set<Voter> voters) {
        this.voters = voters;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Poll: {#").append(this.getId());
        result.append(", topic=").append(this.topic);
        result.append(", author=").append(this.author);
        result.append("}");
        return result.toString();
    }

}
