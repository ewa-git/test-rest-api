package pl.juniorjavaproject.testrestapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = Tweet.TABLE_NAME)
public class Tweet {
    public static final String TABLE_NAME = "tweets";

    public Tweet() {
    }

    public Tweet(Long id, LocalDateTime createdOn, LocalDateTime updatedOn, String tweetTitle, String tweetText, User user) {
        this.id = id;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.tweetTitle = tweetTitle;
        this.tweetText = tweetText;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(nullable = false)
    private String tweetTitle;

    @Column(nullable = false)
    private String tweetText;

    @ManyToOne
    private User user;

    @PrePersist
    public void setCreatedOn() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    public void setUpdatedOn() {
        updatedOn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getTweetTitle() {
        return tweetTitle;
    }

    public void setTweetTitle(String tweetTitle) {
        this.tweetTitle = tweetTitle;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", tweetTitle='" + tweetTitle + '\'' +
                ", tweetText='" + tweetText + '\'' +
                ", user=" + user +
                '}';
    }
}
