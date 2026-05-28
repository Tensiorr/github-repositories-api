package com.tensiorr.github_repositories_api;

record GitHubRepoDto(String name, boolean fork, Owner owner) {
    public record Owner(String login) {}
}
