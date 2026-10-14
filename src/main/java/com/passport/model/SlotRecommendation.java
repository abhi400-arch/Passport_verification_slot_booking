package com.passport.model;

public class SlotRecommendation {
    private final Slot slot;
    private final int score;
    private final String reason;

    public SlotRecommendation(Slot slot, int score, String reason) {
        this.slot = slot;
        this.score = score;
        this.reason = reason;
    }
    public Slot getSlot() { return slot; }
    public int getScore() { return score; }
    public String getReason() { return reason; }
}
