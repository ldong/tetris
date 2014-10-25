import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

/**
 * A Tetris piece. Can be rotated, moved, and settled on to a board.
 * 
 * @version 2.0
 * @author Eric
 * @author Sundeep
 */
public abstract class Piece {

    //Color of the piece
    private final Color color;

    /*
     * The rotation model of the piece
     * If the tile does exist in that position
     * (for that rotation), value = true
     * else false
     * The model is inverted when visualizing as a grid:
     * [rotationIndex][x offset][y offset]
     */
    private final boolean[][][] model;

    /*
     * The playing field to interact with
     */
    private final PlayingField field;

    /*
     * The current rotation the piece is in
     */
    private int rotationIndex = 0;

    /*
     * tile position of the top left model tile
     */
    private int x, y;

    /**
     * Make a piece.
     * 
     * @param field
     *            the playing field this piece is on.
     * @param x
     *            the initial x position of the top left of the piece.
     * @param y
     *            the initial x position of the top left of the piece.
     * @param model
     *            a 4-length array of 2D boolean arrays that map out the piece.
     *            Each 2D array describes the shape of the piece in one of its 4
     *            rotations.
     * @param color
     *            The desired color.
     */
    public Piece(PlayingField field, int x, int y, boolean[][][] model, Color color)
    {
		this.field = field;
		this.x = x;
		this.y = y;
        this.color = color;
        this.model = model;
    }

    /**
     * Attempt to move a piece to a given location, with it's current rotation.
     * If able to move, go ahead and move the piece. If not, only return false
     * 
     * @param newX the new x position to move to
     * @param newY the new y position to move to
     * @return true if move was successful, false if blocked.
     */
    public boolean attemptMove(int newX, int newY) {
        boolean collision = false;

		//by adding && !collision, loops stops on 1st collision and doesn't waste time continuing
        for (int i = 0; i < model[rotationIndex].length && !collision; i++) {
            for (int j = 0; j < model[rotationIndex][0].length && !collision; j++) {
                if (model[rotationIndex][i][j] && !field.isTileVacant(newX + i, newY + j))
				{
                    collision = true;
                }
            }
        }

        if (!collision) //if the piece didn't collide when trying to move it
        {
            x = newX;
            y = newY;
        }

        return !collision;
    }

    /**
     * Attempt to move to the next rotation index of the piece.
     * If able to, rotate. If not, return false
     * 
     * @return true if the rotation was successful.
     */
    public boolean attemptRotation() {
        int oldRot = rotationIndex;
        rotationIndex = (rotationIndex + 1) % 4; 	//hardcoded for understanding,
													//technically is model.length
        if (attemptMove(x, y)) //is the new rotation possible?
        {
			return true;
		}
		else
		{
			rotationIndex = oldRot; //restore previous rotation
			return false;
		}
    }

    /**
     * Map a piece to its given board. This will fill in the tile colors on the
     * board. Make sure to trash the piece after this, or it will collide with
     * the tiles it just colored!
     */
    public void settlePiece() {
        for (int i = 0; i < model[rotationIndex].length; i++)
            for (int j = 0; j < model[rotationIndex][0].length; j++)
                if (model[rotationIndex][i][j])
                    field.setTileColor(x + i, y + j, color);
        

    }

    /**
     * Assign num to rotationIndex
     * @param num index number
     */
     public void setRotationNum(int num)
     {
    	 rotationIndex =num;
     }
    /**
     * Draw the piece as a set of colored rectangles, assuming a certain
     * tileDim;
     * 
     * @param g
     *            graphics context.
     * @param tileDimension
     *            the pixel-wise dimension of a tile.
     */
    public void draw(Graphics g, Dimension tileDimension) {
        g.setColor(color);
        for (int i = 0; i < model.length; i++)
            for (int j = 0; j < model[0].length; j++)
                if (model[rotationIndex][i][j])
                    g.fill3DRect((x+i) * tileDimension.width, (y + j) * tileDimension.height,
                            tileDimension.width, tileDimension.height, true);
    }

    
    public int getRotationIndex()
    {
    	return rotationIndex;
    }
    
    public boolean[][][] getModel()
    {
    	return model;
    }
	/**
	 * @return the color of the piece
	 */
    public Color getColor() {
        return color;
    }

    
    /**
     * @return the x position of the piece
     */
    public int getX() {
        return x;
    }
    
    /**
     * @return the x position of the piece
     */
    public int getY() {
        return y;
    }
    
    public void setXY(int x, int y)
    {
    	this.x = x;
    	this.y = y;
    }
}
