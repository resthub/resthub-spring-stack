package org.resthub.roundtable.domain.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.HashSet;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.resthub.core.domain.model.Resource;

/**
 * Answer.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Entity
@Table(name = "answer")
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Answer extends Resource {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private Poll poll;
    private Integer order;
    private String body;
    private Set<Vote> votes = new HashSet<Vote>();

    /**
     * Default constructor.
     */
    public Answer() {
        super();
    }

    /**
     * @return the order
     */
    @Column(name = "order_num", nullable = false)
    public Integer getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * @return the poll
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    public Poll getPoll() {
        return poll;
    }

    /**
     * @param poll the poll to set
     */
    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    /**
     * @return the body
     */
    @Column(name = "body", nullable = false)
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the votes
     */
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "answer", fetch = FetchType.LAZY)
    @XmlTransient
    public Set<Vote> getVotes() {
        return votes;
    }

    /**
     * @param votes the votes to set
     */
    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }
}
