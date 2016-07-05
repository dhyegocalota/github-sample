package br.com.dhyegofernando.githubsample.models.github;

import com.google.gson.annotations.SerializedName;

public class Repository {
    public String id;

    public User user;

    public String name;

    @SerializedName("full_name")
    public String fullName;

    public String description;

    @SerializedName("fork")
    public boolean isFork;

    @SerializedName("forks_count")
    public int forksCount;

    @SerializedName("watchers_count")
    public int watchersCount;

    @SerializedName("open_issues_count")
    public int openIssuesCount;

    public String language;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;
}
