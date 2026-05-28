package com.tensiorr.github_repositories_api;

import java.util.List;

public record RepoResponse(String repositoryName, String ownerLogin, List<BranchInfo> branches) {}
