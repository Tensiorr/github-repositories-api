package com.tensiorr.github_repositories_api;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GitHubService {

    private final GitHubClient gitHubClient;

    public GitHubService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<RepoResponse> getRepos(String username) {
        return gitHubClient.getRepos(username)
                .stream()
                .filter(repo -> !repo.fork())
                .map(repo -> {
                    List<BranchInfo> branches = gitHubClient.getBranches(repo.owner().login(), repo.name())
                            .stream()
                            .map(branch -> new BranchInfo(branch.name(), branch.commit().sha()))
                            .toList();
                    return new RepoResponse(repo.name(), repo.owner().login(), branches);
                })
                .toList();
    }
}
