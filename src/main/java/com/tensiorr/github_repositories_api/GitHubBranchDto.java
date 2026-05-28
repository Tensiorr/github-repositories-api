package com.tensiorr.github_repositories_api;

public record GitHubBranchDto(String name, Commit commit) {
    public record Commit(String sha) {}
}
