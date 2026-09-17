package edu.eci.dosw.bowling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BowlingGameTest {

    // helper to module A
    private void rollMany(BowlingGame game, int times, int pins) {
        for (int i = 0; i < times; i++) {
            game.roll(pins);
        }
    }

    @Test
    @DisplayName("A1: roll(0) es válido - No lanza excepción")
    public void shouldAcceptRollZero() {
        BowlingGame bowlingGame = new BowlingGame();
        assertDoesNotThrow(() -> bowlingGame.roll(0));
    }

    @Test
    @DisplayName("A2: roll(-1) - Lanza IllegalArgumentException")
    public void shouldNotAcceptRollMinusOne(){
        BowlingGame bowlingGame = new BowlingGame();
        assertThrows(IllegalArgumentException.class,() -> bowlingGame.roll(-1) );
    }

    @Test
    @DisplayName("A3 roll(11) - lanza IllegalArgumentException")
    public void shouldNotAcceptedRollGreaterThanTen(){
        BowlingGame bowlingGame = new BowlingGame();
        assertThrows(IllegalArgumentException.class, () -> bowlingGame.roll(11));
    }

    @Test
    @DisplayName("A4: 2 tiros en un frame que sumen más de 10 - Lanza IllegalArgumentException")
    public void shouldNotAcceptTwoRollsInSameFrameTotalGreaterThanTen() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(6);
        assertThrows(IllegalArgumentException.class, () -> bowlingGame.roll(7));
    }

    @Test
    @DisplayName("A5: roll(x) tras terminar el juego - Lanza IllegalStateException")
    public void shouldNotAcceptRollWhenGameIsFinished() {
        BowlingGame bowlingGame = new BowlingGame();
        rollMany(bowlingGame, 20, 0);

        assertThrows(IllegalStateException.class, () -> bowlingGame.roll(2));
    }

    @Test
    @DisplayName("A6: roll(10) marca el frame como STRIKE")
    public void shouldMarkFrameAsStrikeWhenRollIsTen() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(10);
        List<Frame> frames = bowlingGame.getFrames();
        Frame firstFrame = frames.getFirst();

        assertEquals(FrameType.STRIKE, firstFrame.getType());
        assertEquals(2, bowlingGame.getCurrentFrame());
    }

    @Test
    @DisplayName("A7: roll(5) + roll(5) marca el frame como SPARE")
    public void shouldMarkActualFrameAsSpare() {
        BowlingGame bowlingGame = new BowlingGame();
        bowlingGame.roll(5);
        bowlingGame.roll(5);

        List<Frame> frames = bowlingGame.getFrames();
        Frame firstFrame = frames.getFirst();
        assertEquals(FrameType.SPARE, firstFrame.getType());
    }

    @Test
    @DisplayName("A8: Frame 10 con strike acepta hasta 3 tiros sin error")
    public void shouldAcceptThreeRollsInTenthFrameIfStrike() {
        BowlingGame bowlingGame = new BowlingGame();
        rollMany(bowlingGame, 9, 10);

        assertDoesNotThrow(() -> {
            bowlingGame.roll(10);
            bowlingGame.roll(10);
            bowlingGame.roll(10);
        });
    }

    // helpers to module B
    private void rollPerfectGame(BowlingGame game) {
        for (int i = 0; i < 12; i++) {
            game.roll(10);
        }
    }
    private void rollAllSpares(BowlingGame game, int lastBonus) {
        for (int i = 0; i < 10; i++) {
            game.roll(5);
            game.roll(5);
        }
        game.roll(lastBonus);
    }

    @Test
    @DisplayName("B1: Juego con todos los tiros a 0 -> score == 0")
    public void shouldScoreZeroWhenAllRollsAreZero() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 0);
        assertEquals(0, game.score());
    }
    @Test
    @DisplayName("B2: Juego sin strikes ni spares -> suma normal")
    public void shouldCalculateScoreWithoutBonuses() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 3);
        assertEquals(60, game.score());
    }
    @Test
    @DisplayName("B3: Un spare en frame 1 suma el bono del siguiente tiro")
    public void shouldCalculateScoreWithOneSpare() {
        BowlingGame game = new BowlingGame();
        game.roll(5);
        game.roll(5);
        game.roll(3);
        rollMany(game, 17, 0);

        assertEquals(16, game.score());
    }
    @Test
    @DisplayName("B4: Un strike en frame 1 suma el bono de los siguientes dos tiros")
    public void shouldCalculateScoreWithOneStrike() {
        BowlingGame game = new BowlingGame();
        game.roll(10);
        game.roll(4);
        game.roll(3);
        rollMany(game, 16, 0);

        assertEquals(24, game.score());
    }
    @Test
    @DisplayName("B5: Dos strikes consecutivos")
    public void shouldCalculateScoreWithConsecutiveStrikes() {
        BowlingGame game = new BowlingGame();
        game.roll(10);
        game.roll(10);
        game.roll(5);
        game.roll(2);
        rollMany(game, 14, 0);

        assertEquals(49, game.score());
    }
    @Test
    @DisplayName("B6: Juego de puros spares + ultimo tiro = 5")
    public void shouldScore150WhenAllSparesAndLastRoll5() {
        BowlingGame game = new BowlingGame();
        rollAllSpares(game, 5);
        assertEquals(150, game.score());
    }
    @Test
    @DisplayName("B7: Juego perfecto - 12 strikes - score debe ser 300")
    public void shouldScore300OnPerfectGame() {
        BowlingGame game = new BowlingGame();
        rollPerfectGame(game);
        assertEquals(300, game.score());
    }
    @Test
    @DisplayName("B8: Llamar score() antes de terminar lanza IllegalStateException")
    public void shouldThrowExceptionIfScoreCalledBeforeGameEnds() {
        BowlingGame game = new BowlingGame();
        game.roll(10); // Juego incompleto

        assertThrows(IllegalStateException.class, () -> game.score());
    }


}
