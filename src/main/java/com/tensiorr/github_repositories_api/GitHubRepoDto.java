package com.tensiorr.github_repositories_api;

record GitHubRepoDto(String name, boolean fork, Owner owner) {
    record Owner(String login) {}
}
