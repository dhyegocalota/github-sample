package br.com.dhyegofernando.githubsample.models.github;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("login")
    public String login;

    @SerializedName("name")
    public String name;

    @SerializedName("avatar_url")
    public String avatarUrl;

    @SerializedName("followers")
    public String followers;
}
