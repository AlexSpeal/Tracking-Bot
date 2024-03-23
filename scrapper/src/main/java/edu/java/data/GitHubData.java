package edu.java.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubData(@JsonProperty int numberBranches,
                         @JsonProperty int numberPullRequests,
                         @JsonProperty int branchesHash,
                         @JsonProperty int pullRequestsHash) {
}
