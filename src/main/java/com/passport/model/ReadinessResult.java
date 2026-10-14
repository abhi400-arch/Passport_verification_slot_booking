package com.passport.model;

import java.util.List;

public class ReadinessResult {
    private final int score;
    private final List<String> completed;
    private final List<String> missing;

    public ReadinessResult(int score, List<String> completed, List<String> missing) {
        this.score = score; this.completed = completed; this.missing = missing;
    }
    public int getScore() { return score; }
    public List<String> getCompleted() { return completed; }
    public List<String> getMissing() { return missing; }
}
