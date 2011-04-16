package org.resthub.roundtable.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.resthub.core.model.Resource;

/**
 * Vote Entity.
 * @author Nicolas Carlier
 */
@Entity
@XmlRootElement
@Table(name = "voter", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "poll_id"})})
@NamedQueries({
    @NamedQuery(name = "existsVoter", query = "select count(vr) from Voter as vr where name = :name and poll.id = :pid"),
    @NamedQuery(name = "findVoterByNameAndPoll", query = "from Voter where name = :name and poll = :poll")

})
public class Voter extends Resource {
	
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private Poll poll;
    private String name;
    private List<Vote> votes = new ArrayList<Vote>();

    /**
     * Default constructor.
     */
    public Voter() {
        super();
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    @JsonIgnore
    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "voter", fetch = FetchType.LAZY)
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
