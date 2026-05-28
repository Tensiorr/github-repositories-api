package com.tensiorr.github_repositories_api;

record GitHubBranchDto(String name, Commit commit) {
    record Commit(String sha) {}
}
