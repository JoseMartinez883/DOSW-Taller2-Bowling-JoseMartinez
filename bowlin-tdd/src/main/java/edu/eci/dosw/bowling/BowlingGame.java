package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de un juego de Bowling para un jugador.
 * Un juego tiene exactamente 10 frames.
 */
public class BowlingGame {

    private final List<Frame> frames;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    /** Registra pinos derribados. Lanza IllegalArgumentException si pines < 0 o > 10.
     *  Lanza IllegalStateException si el juego ya termino. */
    public void roll(int pins) {
        if (currentFrame >= 10) {
            throw new IllegalStateException("El juego ya termino");
        }

        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Pines inválidos");
        }

        if (frames.size() == currentFrame) {
            frames.add(new Frame());
        }
        Frame frame = frames.get(currentFrame);
        if (currentFrame < 9) {
            if (frame.getTotalPins() + pins > 10) {
                throw new IllegalArgumentException("No puede sumar más de 10 pines");
            }

            frame.addRoll(pins);
            if (frame.getRolls().size() == 1 && pins == 10) {
                frame.setType(FrameType.STRIKE);
                currentFrame++;
            }

            else if (frame.getRolls().size() == 2) {
                if (frame.getTotalPins() == 10) {
                    frame.setType(FrameType.SPARE);
                } else {
                    frame.setType(FrameType.NORMAL);
                }
                currentFrame++;
            }

        } else {
            frame.setType(FrameType.TENTH);
            frame.addRoll(pins);

            int rollsCount = frame.getRolls().size();

            if (rollsCount == 2 && frame.getRolls().get(0) + frame.getRolls().get(1) < 10) {
                currentFrame++;
            }

            else if (rollsCount == 3) {
                currentFrame++;
            }
        }
    }

    /** Puntaje total. Lanza IllegalStateException si el juego no esta completo. */
    public int score() {
        if (!isComplete()) {
            throw new IllegalStateException("El juego no está completo");
        }
        BowlingScorer scorer = new BowlingScorer();
        return scorer.calculate(frames);
    }

    /** true cuando los 10 frames han sido completados. */
    public boolean isComplete() {
        return currentFrame >= 10;
    }

    public List<Frame> getFrames() { return List.copyOf(frames); }

    public int getCurrentFrame(){
        return currentFrame + 1;
    }
}
