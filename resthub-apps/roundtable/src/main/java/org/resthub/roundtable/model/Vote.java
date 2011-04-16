package org.resthub.roundtable.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.resthub.core.model.Resource;

/**
 * Vote Entity.
 * @author Nicolas Carlier
 */
@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"voter_id", "answer_id"})})
@NamedQueries({
    @NamedQuery(name = "existsVote", query = "select count(v) from Vote as v where voter.id = :vid and answer.id = :aid")
})
public class Vote extends Resource {
	
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private Answer answer;
    private Voter voter;
    private String value;

    /**
     * Default constructor.
     */
    public Vote() {
        super();
    }

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    @JsonIgnore
    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    @Column(name = "val", nullable = false)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    @JsonIgnore
    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }
}
