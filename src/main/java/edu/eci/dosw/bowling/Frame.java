package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private List<Integer> rolls = new ArrayList<>();
    private FrameType type = FrameType.NORMAL;

    public void addRoll(int pins) {
        rolls.add(pins);
    }

    public List<Integer> getRolls() {
        return rolls;
    }

    public int getTotalPins() {
        return rolls.stream().mapToInt(Integer::intValue).sum();
    }

    public void setType(FrameType type) {
        this.type = type;
    }

    public FrameType getType() {
        return type;
    }
}
