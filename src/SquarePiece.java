import java.awt.Color;
import java.awt.Point;

/**
 * A Tetris piece model for the square piece
 * 
 * @version 2.0
 * @author Eric
 * @author Sundeep
 */
public class SquarePiece extends Piece{

	/*
     * The rotation model of the piece
     * If the tile does exist in that position
     * (for that rotation), value = true
     * else false
     * The model is inverted when visualizing as a grid:
     * [rotationIndex][x offset][y offset]
     */
    private final static boolean[][] rotation0 = new boolean[][] {
                {false, false, false, false},
                {true, true, false, false},
                {true, true, false, false},
                {false, false, false, false}             
        };
    
    private final static boolean[][][] model = {rotation0, rotation0, rotation0, rotation0};
        
    public SquarePiece(PlayingField field, int x, int y)
    {
        super(field, x, y, model, Color.YELLOW);
    }
}
