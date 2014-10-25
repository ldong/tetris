import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
/**
 * Actually it represents a playingField 
 * that gonna use for Tetris
 * @author Lin Dong 
 */
public class PlayingField {

    /*
     * Game board. Colors are settled pieces, nulls are empty spots.
     * Each element in the array corresponds to a 2-D grid of "tiles"
     */
    private Color[][] board;
    
    private Color emptyTile = Color.GRAY;


    public final int WIDTH, HEIGHT;   //can be public because constants can't be changed

    
    
    /**
     * Create a playing field.
     * 
     * @param cols number of columns in the field (width)
     * @param rows number of rows in the width (height)
     */
    public PlayingField(int cols, int rows) {

		WIDTH = cols;
		HEIGHT = rows;
		
        board = new Color[WIDTH][HEIGHT]; //board arrange by x,y / width,height
        
    }

    public Color[][] getColorArray()
    {
    	Color[][] newBoard = new Color[WIDTH][HEIGHT];
    	for(int i = 0; i<WIDTH; i++)
    		for(int j=0; j<HEIGHT; j++)
    		{
    			 newBoard[i][j] = board[i][j]; 
    		   //  System.out.println(newBoard[i][j]);

    		 }
        return newBoard;

    }

     
    /**
     * Checks if the tile at position is free.
     * 
     * @param x the x position to check
     * @param y the y position to check
     * @return true if piece is legal and vacant to move to.
     */
    public boolean isTileVacant(int x, int y) {
        if (x < 0 || x >= board.length || y >= board[0].length)
            return false;

        if (y < 0) //this implementation allows pieces to exist above the board
            return true;

        return board[x][y] == null; //if a slot is null, then it is considered empty
    }

    /**
     * Set the tile at a given position. Tiles wont be set if they are off-sceen
     * 
     * @param x the x position of the tile
     * @param y the y position of the tile
     * @param color color to set tile to.
     */
    public void setTileColor(int x, int y, Color color) {
        if (x >= 0 && x < board.length && y >= 0 && y < board[0].length)
            board[x][y] = color;
    }

    /**
     * Check the top row for a loss condition.
     * 
     * @return true if loss detected, false otherwise.
     */
    public boolean isGameOver() {
        for (int x = 0; x < board.length; x++) {
            if (board[x][0] != null) //check top row
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Clear the board.
     */
    public void resetBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
            	board[i][j] = null;
            }
        }
    }
    
    
    /**
     * All the rows should be scanned for a clear condition. When a row is 
     * completely filled with colors, the row is cleared. All the colored tiles
     * above that cleared row are then moved down by one row. This method will 
     * detect and clear all the rows that are completely filled.
     * @return either able to be cleared or not
     */
    public boolean checkForClears()
    {
    	boolean isFull = false;
    	int wCounter =0;
    	int currentH= 0;
    	
    	for(int h = HEIGHT-1; h >= 0; h--)   // h stands for height
    	{
    		wCounter = 0;
    		
    		while((wCounter < WIDTH) && ((board[wCounter][h]) != null))
    		{	
    			// counts how many actually blocks are filled 
    	           wCounter++;
    		}
    		
        	if(wCounter == WIDTH)
        	{
        		isFull = true;
        	    currentH = h;
        	}
        	
    	}
	     if(isFull)
    	 {
        	 for(int y = currentH; y > 0; y--)
    			for(int w = 0; w < WIDTH; w++)
    		    {
        			board[w][y] = board[w][y-1];
    		    }
         }
        	   
    	return isFull;
     	    
    }
    /**
     * set Color array
     * @param arr the passed in Color array
     */
    public void setColorArr(Color[][] arr)
    {
    	board = arr;
    }
    /**
     * Draw the game board
     * 
     * @param g Graphics context to draw on (from BoardPanel)
     * @param tileDim the dimensions of an individual tile
     */
    public void drawBoard(Graphics g, Dimension tileDim) {

		boolean drawGrid = false;

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {

																					//fix later
                if (board[x][y] == null)
                {
                    g.setColor(emptyTile);
                    if (drawGrid)
						g.draw3DRect(x * tileDim.width, y * tileDim.height, tileDim.width, tileDim.height, true);
                    else
						g.drawRect(x * tileDim.width, y * tileDim.height, tileDim.width, tileDim.height);
                }
				else
                {
                    g.setColor(board[x][y]);
                    g.fill3DRect(x * tileDim.width, y * tileDim.height, tileDim.width, tileDim.height, true);
                }
            }
        }
    }
}
