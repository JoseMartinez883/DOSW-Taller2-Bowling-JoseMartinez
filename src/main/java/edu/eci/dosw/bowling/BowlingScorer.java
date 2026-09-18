package edu.eci.dosw.bowling;

import java.util.List;
import java.util.stream.IntStream;

public class BowlingScorer {

    public int calculate(List<Frame> frames) {
        return IntStream.range(0, 10)
                .map(i -> {
                    Frame frame = frames.get(i);
                    if (frame.getType() == FrameType.STRIKE) {
                        return 10 + getNextRolls(frames, i, 2);
                    } else if (frame.getType() == FrameType.SPARE) {
                        return 10 + getNextRolls(frames, i, 1);
                    } else {
                        return frame.getTotalPins();
                    }
                })
                .sum();
    }

    private int getNextRolls(List<Frame> frames, int currentFrameIndex, int limit) {
        return frames.stream()
                .skip(currentFrameIndex + 1)
                .flatMap(f -> f.getRolls().stream())
                .limit(limit)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
