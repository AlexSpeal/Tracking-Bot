package edu.java.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StackOverflowData(@JsonProperty boolean isAnswered, @JsonProperty int answerCount) {
}
