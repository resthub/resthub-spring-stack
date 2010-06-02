package org.resthub.roundtable.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import org.resthub.core.model.Resource;

/**
 * Answer.
 * @author Nicolas Carlier
 */
@Entity
@Table(name = "answer")
@Access(AccessType.FIELD)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Answer extends Resource {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(name = "order_num", nullable = false)
    private Integer order;

    @Column(name = "body", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String body;

    @XmlTransient
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "answer", fetch = FetchType.LAZY)
    private Set<Vote> votes = new HashSet<Vote>();

    /**
     * Default constructor.
     */
    public Answer() {
        super();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }
}
