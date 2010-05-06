package org.resthub.roundtable.domain.model;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
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
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"voter_id", "answer_id"})})
@NamedQueries({
    @NamedQuery(name = "existsVote", query = "select count(v) from Vote as v where voter.id = :vid and answer.id = :aid")
})
@Access(AccessType.FIELD)
@XmlAccessorType(XmlAccessType.FIELD)
public class Vote extends Resource {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    private Voter voter;

    @Column(name = "val", nullable = false)
    private String value;
    

    /**
     * Default constructor.
     */
    public Vote() {
        super();
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }
}
