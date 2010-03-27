package org.resthub.roundtable.domain.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import org.resthub.core.domain.model.Resource;

/**
 * Vote Entity.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"voter", "answer_id"})})
@NamedQueries({
    @NamedQuery(name = "existVote", query = "select count(v) from Vote as v where voter = :voter and poll.id = :pid")
})
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Vote extends Resource {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String voter;
    private String value;
    private Poll poll;
    private Answer answer;

    /**
     * Default constructor.
     */
    public Vote() {
        super();
    }

    /**
     * @return the voter
     */
    @Column(name = "voter", nullable = false)
    public String getVoter() {
        return voter;
    }

    /**
     * @param voter the voter to set
     */
    public void setVoter(String voter) {
        this.voter = voter;
    }

    /**
     * @return vote value
     */
    @Column(name = "val", nullable = false)
    public String getValue() {
        return value;
    }

    /**
     * @param value vote value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the poll
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    @XmlTransient
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
     * @return the answer
     */
    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    public Answer getAnswer() {
        return answer;
    }

    /**
     * @param answer the answer to set
     */
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
