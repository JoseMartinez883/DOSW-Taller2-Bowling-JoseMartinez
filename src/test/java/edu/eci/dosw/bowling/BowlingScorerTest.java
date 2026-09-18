package edu.eci.dosw.bowling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BowlingScorerTest {

    private void rollMany(BowlingGame game, int times, int pins) {
        for (int i = 0; i < times; i++) game.roll(pins);
    }
    private void rollPerfectGame(BowlingGame game) {
        for (int i = 0; i < 12; i++) game.roll(10);
    }

    private void rollAllSpares(BowlingGame game, int lastBonus) {
        for (int i = 0; i < 10; i++) { game.roll(5); game.roll(5); }
        game.roll(lastBonus);
    }

    @Test
    @DisplayName("B1: Juego con todos los tiros a 0 -> score == 0")
    public void shouldScoreZeroWhenAllRollsAreZero() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 0);

        BowlingScorer scorer = new BowlingScorer();
        assertEquals(0, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B2: Juego sin strikes ni spares -> suma normal")
    public void shouldCalculateScoreWithoutBonuses() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 3);

        BowlingScorer scorer = new BowlingScorer();
        assertEquals(60, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B3: Un spare en frame 1 suma el bono del siguiente tiro")
    public void shouldCalculateScoreWithOneSpare() {
        BowlingGame game = new BowlingGame();
        game.roll(5); game.roll(5); game.roll(3);
        rollMany(game, 17, 0);

        BowlingScorer scorer = new BowlingScorer();
        assertEquals(16, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B4: Un strike en frame 1 suma el bono de los siguientes dos tiros")
    public void shouldCalculateScoreWithOneStrike() {
        BowlingGame game = new BowlingGame();
        game.roll(10); game.roll(4); game.roll(3);
        rollMany(game, 16, 0);

        BowlingScorer scorer = new BowlingScorer();
        assertEquals(24, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B5: Dos strikes consecutivos")
    public void shouldCalculateScoreWithConsecutiveStrikes() {
        BowlingGame game = new BowlingGame();
        game.roll(10); game.roll(10); game.roll(5); game.roll(2);
        rollMany(game, 14, 0);

        BowlingScorer scorer = new BowlingScorer();
        assertEquals(49, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B6: Juego de puros spares + ultimo tiro = 5")
    public void shouldScore150WhenAllSparesAndLastRoll5() {
        BowlingGame game = new BowlingGame();
        rollAllSpares(game, 5);

        BowlingScorer scorer = new BowlingScorer();
        assertEquals(150, scorer.calculate(game.getFrames()));
    }

    @Test
    @DisplayName("B7: Juego perfecto - 12 strikes - score debe ser 300")
    public void shouldScore300OnPerfectGame() {
        BowlingGame game = new BowlingGame();
        rollPerfectGame(game);

        BowlingScorer scorer = new BowlingScorer();
        assertEquals(300, scorer.calculate(game.getFrames()));
    }
}
