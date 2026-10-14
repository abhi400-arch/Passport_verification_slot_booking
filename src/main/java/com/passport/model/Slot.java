package com.passport.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Slot {
    private int slotId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int capacity;
    private int bookedCount;
    private String status;

    public Slot() {}

    public Slot(LocalDate slotDate, LocalTime startTime, LocalTime endTime, int capacity) {
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.status = "AVAILABLE";
    }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }
    public LocalDate getSlotDate() { return slotDate; }
    public void setSlotDate(LocalDate slotDate) { this.slotDate = slotDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getBookedCount() { return bookedCount; }
    public void setBookedCount(int bookedCount) { this.bookedCount = bookedCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean hasSpace() { return "AVAILABLE".equals(status) && bookedCount < capacity; }
}
