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

    @Test
    @DisplayName("B8: Llamar score() antes de terminar lanza IllegalStateException")
    public void shouldThrowExceptionIfScoreCalledBeforeGameEnds() {
        BowlingGame game = new BowlingGame();
        game.roll(10); // Juego incompleto

        assertThrows(IllegalStateException.class, () -> game.score());
    }

    // Test module C
    @Test
    @DisplayName("C1: isComplete() al inicio del juego es false")
    public void shouldReturnFalseAtStartOfGame() {
        BowlingGame game = new BowlingGame();
        assertFalse(game.isComplete());
    }

    @Test
    @DisplayName("C2: isComplete() después de 9 frames es false")
    public void shouldReturnFalseAfter9Frames() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 0);
        assertFalse(game.isComplete());
    }

    @Test
    @DisplayName("C3: 10 frames normales completos (sin bonos) es true")
    public void shouldReturnTrueAfter10NormalFrames() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 0);
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C4: Spare en frame 10 + 1 tiro bonus es true")
    public void shouldReturnTrueAfterSpareAndBonusInTenthFrame() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 0);
        game.roll(5);
        game.roll(5);
        assertFalse(game.isComplete());

        game.roll(5);
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C5: Strike en frame 10 + 2 tiros bonus es true")
    public void shouldReturnTrueAfterStrikeAndTwoBonusesInTenthFrame() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 18, 0);
        game.roll(10);
        assertFalse(game.isComplete());

        game.roll(4);
        assertFalse(game.isComplete());

        game.roll(3);
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C6: Juego perfecto (12 strikes) es true al final")
    public void shouldReturnTrueAfterPerfectGame() {
        BowlingGame game = new BowlingGame();
        rollPerfectGame(game);
        assertTrue(game.isComplete());
    }
}
