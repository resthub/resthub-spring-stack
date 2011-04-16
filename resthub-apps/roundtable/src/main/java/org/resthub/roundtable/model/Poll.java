package org.resthub.roundtable.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;
import org.resthub.core.model.Resource;

/**
 * Poll.
 * @author Nicolas Carlier
 */
@Indexed
@Entity
@Table(name = "poll")
@XmlRootElement
public class Poll extends Resource {
	
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    private String author;
    private String topic;
    private String body;
    private String illustration;
    private List<Answer> answers = new ArrayList<Answer>();
    private Set<Voter> voters = new HashSet<Voter>();
    private Date creationDate;
    private Date expirationDate;

    /**
     * Default constructor.
     */
    public Poll() {
        super();
    }

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @IndexedEmbedded
    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @NotEmpty
    @Size(max=50)
    @Column(name = "author", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @NotEmpty
    @Size(max=1000)
    @Column(name = "body", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Future
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration_date", nullable = false)
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Column(name = "illustration")
    public String getIllustration() {
    	return illustration;
    }

    public void setIllustration(String illustration) {
	this.illustration = illustration;
    }

    @NotEmpty
    @Size(max=100)
    @Column(name = "topic", nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @OneToMany(mappedBy = "poll", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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
