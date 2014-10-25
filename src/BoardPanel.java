import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

//import javax.activation.FileDataSource;
import javax.swing.JButton;
//import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.Random;
import java.util.Scanner;

/**
 * The game board. Will start the game, and respond to user input.
 * 
 * @version 2.0
 * @author Eric Fruchter
 * @author Sundeep Ghuman
 * @author Lin Dong
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel{

    /*
     * The playing field.
     */
    private PlayingField field;

    
    /*
     *  Scores and Level Labels
     */
//    private JLabel score, level;
    
    /*
     * The active piece the player controls
     * and nextPiece will be the next Piece which drops 
     */
    private Piece active, nextPiece;

    
    /*
     * JPanel for Preview next piece 
     * and for save and load 
     */
    private Preview preview;
    
    /*
     * Game timer
     */
    private final Timer timer;

    /*
     * The default pixel dimension per tile.
     */
    private Dimension tileDimension = new Dimension(20, 20);

    /*
     * Generate a random number
     */
    private Random rand = new Random();

    /*
     * Jbutton for save and load
     */
    private JButton save, load;

    
    /*
     * These variables define current level, scores
     * also have a time variable that going to increase the time
     * every time that level goes up
     */
    private int LEVEL = 1;       // LEVEL of the Tetris
    private int SCORE = 0;       // current score of the game
    private int baseScore = 100;  // base Score
    private int time = 500;
    private int cols, rows;
    /*
     * isFalse is a variable sets for pause/ resume whenever P is pressed
     */
    boolean isFalse = true;             // isFalse for Pause P

    /**
     * Creates a game board and starts the game.
     * 
     * @param cols number of columns in the game (width)
     * @param rows number of rows in the game (height)
     * @param preview initialization of preview 
     */
    public BoardPanel(int cols, int rows, Preview preview) {

        setPreferredSize(new Dimension(cols * tileDimension.width, rows * tileDimension.height));

        setFocusable(true);
        addKeyListener(new KeyController());

        this.cols = cols;
        this.rows = rows;
        field = new PlayingField(cols, rows);

        timer = new Timer(time, new FallListener());
        this.preview = preview;
        
        preview.setBoardPanel(this);
        
        preview.setColsnRows(cols, rows);
        generateNewPiece(); //create the next piece
        
        save = new JButton("Save(Works)");
        load = new JButton("Load(But lost the focus)");
        //add(save);
        //add(load);
        save.addActionListener(new ButtonListener(0));
        load.addActionListener(new ButtonListener(1));
    }


    /**
     * Start the Tetris game.
     */
    public void start()
    {
        timer.start();
        
    }

    /**
     *  Stop the Teris game
     */
    public void stop()
    {
    	timer.stop();
    }
    
    /**
     * Assign the nextPiece to current active piece
     */
    private void assginPiece()
    {
    	// assign nextPiece to activePiece
    	 active = nextPiece;
    }
    /**
     * Set active to a new, random piece.
     */
    private void generateNewPiece() {
        
    	switch (rand.nextInt(6))
    	//switch(0)
        {
			//start the pieces off with negative y values so they enter
			//starting at the bottom of the piece rather than the top
            case 0:
                nextPiece = new LinePiece(field, field.WIDTH / 2, -2);
                break;
            case 1:
				nextPiece = new SquarePiece(field, field.WIDTH / 2, -2);
				break;
            case 2:
				nextPiece = new LeftPiece(field, field.WIDTH / 2, -2);
				break;
            case 3:
				nextPiece = new RightPiece(field, field.WIDTH / 2, -2);
				break;
            case 4:
				nextPiece = new LeftZPiece(field, field.WIDTH / 2, -2);
				break;
            case 5:
				nextPiece = new RightZPiece(field, field.WIDTH / 2, -2);
				break;
            case 6:
				nextPiece = new TPiece(field, field.WIDTH / 2, -2);
				break;
          }
			
    	preview.setPiece(nextPiece); 			
    	   
      // run this to initialize active piece
        if(active == null)	
        {
        	active = nextPiece;
        	generateNewPiece();
        }
           

    }
    
    public void setTime(int time)
    {
    	this.time = time + LEVEL * 25;
    }
    
    
    /**
     * Check for scores, which includes the functionality of checkForClears
     * also assign new piece if needs
     */
    public void checkScores()
    {
        assginPiece();      // after Settle, assign a new Piece to active Piece
        //repaint();
        while(field.checkForClears())
        {        
        	// increase the score
        	SCORE = SCORE + baseScore * LEVEL;
	        
        	/* increase the time and Level
         	 * whenever SCORE increases 1000
         	 * and baseScore increase 100
         	 * and time increase Level * 25 ms
        	 */
	        if(SCORE % 1000 == 0)
	        {
	        	LEVEL++;
	        	baseScore += 100;
	        	setTime(time);
	        }
//	
//	        level.setText("Level: "+ LEVEL);
//	        score.setText("Score: "+ SCORE);
        }
        preview.setNumber(LEVEL, SCORE);
    }
    
    
	 /**
	  * Getter Level
	  */
     public int getLevel()
     {
    	 return LEVEL;
     }
     
	 /**
	  * Getter score
	  */
     public int getScore()
     {
    	 return SCORE;
     }
    
    /**
     * Move the current piece down and checks for a win/loss or placement
     * situation. If the piece fails to move down anymore (attemptMove returns false),
     * then the piece settles.
     */
    private void moveDownActive() {

        /* Move the piece down */
        if (!active.attemptMove(active.getX(), active.getY() + 1))
        {
            active.settlePiece();
            checkScores();        // check if its scoring?
            generateNewPiece();
        }

		/* Check if the game is over */
        if (field.isGameOver()) {
            timer.stop();
            int choice = JOptionPane.showConfirmDialog(this, "Play Again?", "You Lost it!", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) //restart game
            {
				field.resetBoard();
				timer.restart();
			}
			else //No option
			{
				System.exit(0);
			}
        }
    }
    

    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        tileDimension = new Dimension(getWidth()/field.WIDTH,getHeight()/field.HEIGHT);
        //Dimension nextDimension = new Dimension((getWidth()+5)/field.WIDTH,(getHeight()+5)/field.HEIGHT);

		/* Pass the graphics object and the current tileDimension */
        field.drawBoard(g, tileDimension);	// The field knows how many rows
											// and cols of tiles it has, therefore it
											// only needs to know the dimension
											// of each tile
        
        active.draw(g, tileDimension);
        
        preview.setDimension(tileDimension);
        preview.repaint();
    }

    /**
     * The timer action. Each time step, move the piece down.
     */
    private class FallListener implements ActionListener
    {    
		public void actionPerformed(ActionEvent event)
		{
			if (active != null)
				moveDownActive();

			repaint();
		}
	}


    /**
     * Handle keystrokes.
     * 
     * @author eric
     * 
     */
    private class KeyController extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent key) {
            if (active != null) {

                int oldX = active.getX();
                int oldY = active.getY();
                switch (key.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        active.attemptMove(oldX + 1, oldY); //move right
                        break;
                    case KeyEvent.VK_LEFT:
                        active.attemptMove(oldX - 1, oldY); //move left
                        break;
                    case KeyEvent.VK_SPACE:
                        active.attemptRotation(); //rotate
                        break;
                    case KeyEvent.VK_UP:
                        active.attemptRotation(); //rotate
                        break;
                    case KeyEvent.VK_DOWN:
                        moveDownActive(); //move down
                        break;
                    case KeyEvent.VK_ESCAPE: //quit game
                        System.exit(0);
                        break;
                    case KeyEvent.VK_P:
                        if(isFalse==true)
                        	timer.stop();
                        else
                        	timer.start();
                    	isFalse = !isFalse;
                        break;
                    case KeyEvent.VK_S:
						try {
							save2File();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	break;
                    case KeyEvent.VK_L:
						try {
							load();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    	break;
                    	
                    case KeyEvent.VK_Z:
					try {
						preview.cheats();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                        break;
                    case KeyEvent.VK_X:
                        preview.setCheat(true);
                        break;
                    case KeyEvent.VK_C:
                        preview.setCheat(false);
                        break;
                }            
             repaint();
            }
        }
    }
    
    /**
     * save all data to file
     * @throws IOException
     */
	 public void save2File() throws IOException
    {
		 
		 
		 try
		 {			 
			 
			  stop();

			 //********//
			  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			  System.out.print("Please enter the file name to create : ");
			  String file_name = in.readLine();
			  File file = new File(file_name);
			  boolean exist = file.createNewFile();
			  if (!exist)
			  {
			  System.out.println("File already exists." +
			  		             "Please make a new named file or delete the old file.");
			  System.exit(0);
			  }
			  else
			  {
				  FileWriter fstream = new FileWriter(file_name);
				  BufferedWriter out = new BufferedWriter(fstream);
		
				  
			       
		       // get color array
				  
			     Color[][] arr = field.getColorArray();
			     			     
			     
			    out.write("Tetris");
			    out.newLine();

			    out.write(Integer.toString(LEVEL));
			    out.write(",");
			    out.write(Integer.toString(SCORE));
			    out.write(",");
			    out.write(Integer.toString(baseScore));
			    out.write(",");
			    out.write(Integer.toString(time));
			    out.write(",");
			    out.newLine();
			    
			    

			    
			    // write rows and cols
			    out.write(Integer.toString(rows));
	    	    out.write(",");
			    out.newLine();
			    out.write(Integer.toString(cols));
	    	    out.write(",");
			    out.newLine();
		      
			       for(int i=0; i< cols; i++)
			    	   for(int j=0; j< rows; j++)
			    	   {
			    	       out.write(Integer.toString(i));
			    	       out.write(",");
			    	       out.write(Integer.toString(j));
			    	       out.write(",");
//			    	       out.newLine();

                          // add color
			    	       if(arr[i][j]==null)
			    	    	      out.write("0");
			    	       else if(arr[i][j]==Color.blue)
			    	    	      out.write("1");
			    	       else if(arr[i][j]== Color.green)
			    	    	      out.write("2");
			    	       else if(arr[i][j]== Color.CYAN)
			    	    	      out.write("3");
			    	       else if(arr[i][j]== Color.RED)
			    	    	      out.write("4");
			    	       else if(arr[i][j]== Color.pink)
			    	    	      out.write("5");
			    	       else if(arr[i][j]== Color.YELLOW)
			    	    	      out.write("6");
			    	       else if(arr[i][j]== Color.orange)
			    	    	      out.write("7");
			    	       else 
			    	              out.write("1000");
			    	       
			    	       out.write(",");
			    	       out.newLine();

			    	   }
			       out.write(""+active.getClass().getName());			
	    	       out.write(",");
			       out.write(""+active.getX());
			       out.write(",");
			       out.write(""+active.getY());
			       out.write(",");
			       out.write(""+active.getRotationIndex());			
	    	       out.write(",");
			       out.newLine();

    			   
               // no more needs for this 
//			       for(int i=0; i< 4; i++)
//			    	   for(int j=0; j< 4; j++)
//			    	   {
//			    		   
//			    		   if(active.getModel()[active.getRotationIndex()][i][j])
//			    		   {
//			    			   
//			    			   out.write(""+i);
//			    			   out.write(",");
//			    			   out.write(""+j);
//			    			   out.write(",");
//			    			   out.write("1");
//			    			   out.write(",");
//			    			   out.newLine();
//			    		   }
//			    		   else
//			    		   {
//			    			   out.write(""+i);
//			    			   out.write(",");
//			    			   out.write(""+j);
//			    			   out.write(",");
//			    			   out.write("0");
//			    			   out.write(",");
//			    			   out.newLine();			    	   		       
//			                }
//			    	   }
			               out.write(""+nextPiece.getClass().getName());			
	    	               out.write(",");
					       out.write(""+nextPiece.getX());
					       out.write(",");
					       out.write(""+nextPiece.getY());
					       out.write(",");
					       out.write(""+nextPiece.getRotationIndex());
					       out.write(",");
					       out.newLine();

		               // no more needs for this  
//					       for(int i=0; i< 4; i++)
//					    	   for(int j=0; j< 4; j++)
//					    	   {
//					    		   
//					    		   if(nextPiece.getModel()[nextPiece.getRotationIndex()][i][j])
//					    		   {
//					    			   out.write(""+i);
//					    			   out.write(",");
//					    			   out.write(""+j);
//					    			   out.write(",");
//					    			   out.write("1");
//					    			   out.write(",");
//					    			   out.newLine();
//					    		   }
//					    		   else
//					    		   {
//					    			   out.write(""+i);
//					    			   out.write(",");
//					    			   out.write(""+j);
//					    			   out.write(",");
//					    			   out.write("0");
//					    			   out.write(",");
//					    			   out.newLine();			    	   		       
//					                }
//					    	   }
//			       
			       
				  // flush n exit it when its done
				   out.flush();
			       out.close();
				  System.out.println("File created successfully.");
			  }
			  
		}
	     	//Catch exception if any
       catch (IOException e)
       {
            System.err.println("Error: " + e.getMessage());
       }
       finally
       {
    	   requestFocusInWindow();
    	   start();
    	   
       }
    }
	 /**********************/

	 public void load() throws IOException
	 {
		  
         stop();
	     try
	     {
	    	   Scanner readName = new Scanner(System.in);
    	       String fileName;  
    	       boolean exist = false;
    	       int[][] arr;
    	       
    	       File file;
	    	    do
	    	    {
	    	    	System.out.print("Please input a vaild file name :");
	    	        fileName = readName.nextLine();
	    	        System.out.println();
	    	        file= new File(fileName);
			 		 if(file.isFile())
			 		 {
			 			 System.out.print("Loading...");
			 			 System.out.println(" file's loaded");

			 			 exist = true;
			 		 }
			 		 

	    	    }
	    	    while(exist==false);
	    	    
		 		Scanner fileScan = new Scanner(file);

		 		int LEVEL = 0;
		 		int SCORE =0;
		 		int time = 0;
		 		int rows= 0;
		 		int cols=0;
		 		int activeRotationNumber =0;
		 		int newRotationNumber =0;
		 		
		 		
		 		
		 		String line="";
		 		String dummy ="";
		 		fileScan.useDelimiter(",");
		 		
		 		// first Line
	 			line = fileScan.nextLine();
 		    	if((line.equals("Tetris"))==false)
 		    	{
 		    	     System.out.println("Nah, wrong Format, exiting now!");
 		    	     System.exit(0);
 		    	}
		 		    	
	    	   // second line
 		    	     if(fileScan.hasNextInt())
	 		    	 {
 		    	    	 LEVEL = fileScan.nextInt();
                         //System.out.println("LEVEL "+ LEVEL);
	 		    	 }
	 		    	 if(fileScan.hasNextInt())
	 		    	 {
	 		    		 SCORE = fileScan.nextInt();
                         //System.out.println("SCORE "+ SCORE);
	 		    	 }
	 		    	 if(fileScan.hasNextInt())
	 		    	 {
	 		    		 time  = fileScan.nextInt();
	 		    		 //System.out.println("Time "+ time);
	 		    	  }
	 		    	 
                // 3rd line
			 		     if(fileScan.hasNextLine())
			 		         dummy =fileScan.nextLine();	 
 				 		 if(fileScan.hasNextInt())
 				 			 rows = fileScan.nextInt();

				 		// System.out.println(rows);

		 		//4th line
			 		     if(fileScan.hasNextLine())
			 		    	 dummy = fileScan.nextLine();
			 		     if(fileScan.hasNextInt())
			 		         cols = fileScan.nextInt();
			 		     
			 		    fileScan.nextLine();
			 		    // System.out.println(cols);

			       // assign array
			 		arr = new int[cols][rows];
			 		Color[][] tempBoardColorArr= new Color [cols][rows];
//			 		
//		 		    int tri =0;
//		 		    int color =0;
//		 		    
		 		    
		 		    for(int i =0; i<cols; i++)
		 		    {
		 		    	for(int j= 0; j< rows; j++)
	 		    	    {
	 		    		 
		 		    		if(fileScan.hasNextLine())
		 		    			dummy = fileScan.nextLine();
		 		    		
		 		    		String[] parts = dummy.split(",");
		 		    		//System.out.println(dummy);
		 		    		int a = Integer.parseInt(parts[parts.length-1]);
		 		    		 
		 		    		  tempBoardColorArr[i][j] = num2Color(a);
		 		    		//  field.setTileColor(i,j,tempBoardColorArr[i][j]);
		 		    		 
		 		    	 }
		 		    	
	                }
//		 		    for(int i =0; i<cols; i++)
//		 		    	 for(int j= 0; j< rows; j++)
//		 		         {
//		 		    		 if (tri<2)
//			 		    	 {
//		 		    			 if(fileScan.hasNextLine())
//				 		    	 dummy = fileScan.nextLine();
//                                 System.out.println("Dumm1y is ->" +dummy);
// 					 		     if(fileScan.hasNextInt())
//				 		         arr[i][j] = fileScan.nextInt();
// 					 		     tri=3;
//			 		    	 }
//		 		    		 else 
//		 		    		 {
//		 		    			 if(fileScan.hasNextLine())
//				 		    	 dummy = fileScan.nextLine();
//		 		    			 System.out.println("Dummy2 is ->" +dummy);
//
// 					 		     if(fileScan.hasNextInt())
//					 		      color = fileScan.nextInt();
//
//		 		    		 }
//		 		    		 
//		 		    		  tempBoardColorArr[i][j] = num2Color(arr[i][j]);
//		 		    		  field.setTileColor(i,j,tempBoardColorArr[i][j]);

//				 		     System.out.println(i+" "+j+ " " +arr[i][j]+
//				 		         "Color # "+color+ " and Color" + " "+tempBoardColorArr[i][j]);
//		 		         }
	    			  field.setColorArr(tempBoardColorArr);
		    		//  System.out.println("Reassign the board");
		 		    // repaint();
		    		  
		 		    // starting reading active name 
		 		    String name;
		 		    Piece activeProj = null;
		 		    if(fileScan.hasNextLine())
		 		    {
		 		    	dummy = fileScan.nextLine();
		 		    	String[] info = dummy.split(",");
		 		    	name = info[0];
		 		    	int active_X = Integer.parseInt(info[1]);
		 		    	int active_Y = Integer.parseInt(info[2]);
		 		    	activeRotationNumber = Integer.parseInt(info[3]);
		 		    	
		 		    	
//		 		    	name = fileScan.next();
//		 		    	System.out.println("Name________-->" +name);
//		 		    	
//		 		    	int activeXX = fileScan.nextInt();
//		 		    	System.out.println(activeXX);
////		 		    	dummy = fileScan.next();
//		 		    	
//		 		    	int activeYY = fileScan.nextInt();
//		 		    	System.out.println(activeYY);
//
////		 		    	dummy = fileScan.next();
////		 		    	System.out.println("Dummy is "+ dummy);
//
//				 		activeRotationNumber = fileScan.nextInt();
////		 		    	System.out.println("RotationNumber ->"+ activeRN);
//		 		    	
////		 		    	dummy = fileScan.next(); // line break
////		 		    	System.out.println("Dummy is "+ dummy);
//		 	
//		 		    	
		 		    	if(name.equals("LeftPiece"))
	                        activeProj = new LeftPiece(field, active_X, active_Y);
		 		    	else if(name.equals("LeftZPiece"))
	                        activeProj = new LeftZPiece(field, active_X, active_Y);
		 		    	else if(name.equals("LinePiece"))
	                        activeProj = new LinePiece(field, active_X, active_Y);
		 		    	else if(name.equals("RightPiece"))
	                        activeProj = new RightPiece(field, active_X, active_Y);
		 		    	else if(name.equals("RightZPiece"))
	                        activeProj = new RightZPiece(field, active_X, active_Y);
		 		    	else if(name.equals("SquarePiece"))
	                        activeProj = new SquarePiece(field, active_X, active_Y);
		 		    	else if(name.equals("TPiece"))
	                        activeProj = new TPiece(field, active_X, active_Y);

		 		    		
		 		    	active = activeProj;
		 		    }
		 		    
		 		    
		 		    // starting reading names 
		 		    //String name;		 		 
		 		    Piece nextPieceProj = null;
		 		    if(fileScan.hasNextLine())
		 		    {
		 		    	
		 		   	dummy = fileScan.nextLine();
	 		    	String[] info = dummy.split(",");
	 		    	name = info[0];
	 		    	int active_X = Integer.parseInt(info[1]);
	 		    	int active_Y = Integer.parseInt(info[2]);
	 		    	newRotationNumber = Integer.parseInt(info[3]);

		 		    	
//		 		    	//dummy = fileScan.nextLine();
//		 		    	//System.out.println(dummy);
//		 		    	name = fileScan.next();
//                        System.out.println(name);
//                        
//		 		    	int activeXX = fileScan.nextInt();
////		 		    	System.out.println(activeXX);
////		 		    	dummy = fileScan.next();
//		 		    	int activeYY = fileScan.nextInt();
////		 		    	System.out.println(activeYY);
//
////		 		    	dummy = fileScan.next();
////		 		    	System.out.println("Dummy is "+ dummy);
//				 	    
//		 		    	newRotationNumber = fileScan.nextInt();
////		 		    	System.out.println("RotationNumber ->"+ activeRN);
//		 		    	
//		 		    	if(name.equals("LeftPiece"))
//		 		    		nextPieceProj = new LeftPiece(field, activeXX, activeYY);
//		 		    	else if(name.equals("LeftZPiece"))
//		 		    		nextPieceProj = new LeftZPiece(field, activeXX, activeYY);
//		 		    	else if(name.equals("LinePiece"))
//		 		    		nextPieceProj = new LinePiece(field, activeXX, activeYY);
//		 		    	else if(name.equals("RightPiece"))
//		 		    		nextPieceProj = new RightPiece(field, activeXX, activeYY);
//		 		    	else if(name.equals("RightZPiece"))
//		 		    		nextPieceProj = new RightZPiece(field, activeXX, activeYY);
//		 		    	else if(name.equals("SquarePiece"))
//		 		    		nextPieceProj = new SquarePiece(field, activeXX, activeYY);
//		 		    	else 
//		 		    		nextPieceProj = new TPiece(field, activeXX, activeYY);
//		 		    	
	 		    	if(name.equals("LeftPiece"))
	 		    		nextPieceProj = new LeftPiece(field, active_X, active_Y);
	 		    	else if(name.equals("LeftZPiece"))
	 		    		nextPieceProj = new LeftZPiece(field, active_X, active_Y);
	 		    	else if(name.equals("LinePiece"))
	 		    		nextPieceProj = new LinePiece(field, active_X, active_Y);
	 		    	else if(name.equals("RightPiece"))
	 		    		nextPieceProj = new RightPiece(field, active_X, active_Y);
	 		    	else if(name.equals("RightZPiece"))
	 		    		nextPieceProj = new RightZPiece(field, active_X, active_Y);
	 		    	else if(name.equals("SquarePiece"))
	 		    		nextPieceProj = new SquarePiece(field, active_X, active_Y);
	 		    	else 
	 		    		nextPieceProj = new TPiece(field, active_X, active_Y);

	 		    	    nextPiece = nextPieceProj;
	 		    	    preview.setPiece(nextPiece);
		 		    }
		 		    
		 		    
//		 		    
//		 		    // starting read active model
//			 		boolean[][] tempPiece1= new boolean[4][4];
//                    int activeX = 0;
//                    int activeY = 0;
//                  
//		
//		        	 if(fileScan.hasNextLine())
//			    	 dummy = fileScan.nextLine();
//				     if(fileScan.hasNextInt())
//			         activeX = fileScan.nextInt();
//				     if(fileScan.hasNextInt())
//				     activeY= fileScan.nextInt();
//				     if(fileScan.hasNextInt())
//				      activeRotationNumber = fileScan.nextInt();
//				     
				     
				     

//		           // starting assign activePiece back
//		 		    int[][] activePiece = new int[4][4];
//		 		    int activePieceColor =0;
//		 		    for(int i =0; i<4; i++)
//		 		    	 for(int j= 0; j< 4; j++)
//		 		         {
//		 		    		 if (tri<2)
//			 		    	 {
//		 		    			 if(fileScan.hasNextLine())
//				 		    	 dummy = fileScan.nextLine();
//					 		     if(fileScan.hasNextInt())
//				 		         activePiece[i][j] = fileScan.nextInt();
//					 		     tri=3;
//			 		    	 }
//		 		    		 else 
//		 		    		 {
//		 		    			 if(fileScan.hasNextLine())
//				 		    	 dummy = fileScan.nextLine();
//					 		     if(fileScan.hasNextInt())
//					 		       activePieceColor = fileScan.nextInt();
//
//		 		    		 }
//		 		    			 
//				 		 //    System.out.println(i+" "+j+ " " +activePiece[i][j]+
//				 		 //         "Color "+activePieceColor);
//		 		         }
//		 		    
//		 		    
//		 		    
//		 		    // starting read newPiece model
//			 		boolean[][] tempPiece2= new boolean[4][4];
//                    int newPieceX = 0;
//                    int newPieceY = 0;
//                  
//		
//		        	 if(fileScan.hasNextLine())
//			    	 dummy = fileScan.nextLine();
//				     if(fileScan.hasNextInt())
//			         newPieceX = fileScan.nextInt();
//				     if(fileScan.hasNextInt())
//				     newPieceY= fileScan.nextInt();
//				     if(fileScan.hasNextInt())
//				      newRotationNumber = fileScan.nextInt();
//		     
//		     
//		 		    int[][] newNextPiece = new int[4][4];
//		 		    int newNextPieceColor =0;
//		 		    for(int i =0; i<4; i++)
//		 		    	 for(int j= 0; j< 4; j++)
//		 		         {
//		 		    		 if (tri<2)
//			 		    	 {
//		 		    			 if(fileScan.hasNextLine())
//				 		    	 dummy = fileScan.nextLine();
//					 		     if(fileScan.hasNextInt())
//					 		      newNextPiece[i][j] = fileScan.nextInt();
//					 		     tri=3;
//			 		    	 }
//		 		    		 else 
//		 		    		 {
//		 		    			 if(fileScan.hasNextLine())
//				 		    	 dummy = fileScan.nextLine();
//					 		     if(fileScan.hasNextInt())
//					 		     newNextPieceColor = fileScan.nextInt();
//
//		 		    		 }
//		 		    		 
//				 		    // System.out.println(i+" "+j+ " " +newNextPiece[i][j]+
//				 		    //      "Color "+newNextPieceColor);
//		 		         }
//		 		    
		 		    
		 		// assign Values
				this.LEVEL = LEVEL;
				this.SCORE = SCORE;
				this.time = time;
				
				this.rows = rows;
				this.cols= cols;
				active.setRotationNum(activeRotationNumber);
				nextPiece.setRotationNum(newRotationNumber);

				//reset the Score;
				
				preview.setNumber(LEVEL, SCORE);
				
			//	active.setXY(activeX, activeY);
			//	nextPiece.setXY(newPieceX,newPieceY);
		 		    
	     }// try
	      catch (IOException e)
	      {
	    	  //Catch exception if any
		       System.err.println("Error: " + e.getMessage());
		  }// catch
	     finally
	     {
	    	 requestFocusInWindow();
	    	 start();
	     }
	    
} // end of load()  
	 
	 
	 /**
	  * return either true or false
	  * respectively to 1 or 0
	  * @param a
	  * @return true(1) or false(0) 
	  */
	 public boolean num2boo(int a)
	 {
		 boolean t = false;
		 
		 if(a==1)
		    t = true;
		 
		 return t;
	 }
	 
	 /**
	  * convert the index of color into Color
	  * which has been listed as below
	  * @param num the indexing of the color
	  * 
	  */
	public Color num2Color(int num)
	{
		  switch(num)
		  {
			  case 0:
				   return null;
			  case 1:
				   return Color.blue;
			  case 2:
			       return Color.green;
			  case 3:
				   return Color.CYAN;
			  case 4:    
				   return Color.RED;
			  case 5:
				   return Color.pink;
			  case 6:
				   return Color.YELLOW;
			  case 7:
				   return Color.orange;
			  case 1000:
		            System.out.println("Error");
		            return null;
	       }
		return null;
	}
    
	/**
     * ButtonListener which handles the save/load buttons
     */
    private class ButtonListener implements ActionListener 
    {
 	    private int choice=0;
 	   
 	    /**
 	     * constructor that takes one parameter
 	     * and function respects to its choice
 	     * @param the objects copy the passed in parameter
 	     */
 	    public ButtonListener(int choice)
 	    {
 	    	this.choice = choice;
 	    }
 	    
 	    /**
 	     * ActionPerformer functions regards to the choice 
 	     * and records the score and prints out the outcomes
 	     */
 	    public void actionPerformed(ActionEvent e) 
 	    {
 	    	boolean oneTime = true;
 	    	
 	    	//save 
 	    	if(choice == 0 ) 
 	    	{
 	    		try
 	    		{
 	    			if(oneTime)
					save2File();
 	    			oneTime =false;
				}
 	    		catch (IOException e1) 
 	    		{
					System.out.println("Error");					
				}
 	    	}
 	    	//load
 	    	else if(choice==1)
 	    	{
 	    		try 
 	    		{
 	    			if(oneTime)
					load();
 	    			oneTime = false;
				}
 	    		catch (IOException e1) 
 	    		{
					System.out.println("Error");					
				}
 	    	}
 	    }
    }

    
    
	  public boolean save2(File file, String pathName)
	  {
		  
			 try
			 {			 
				 
				  stop();

				 //********//
				  BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				  
				  //System.out.print("Please enter the file name to create : ");
				  //String file_name = in.readLine();
//				  String file_name =  FILENAME;
//				  File file = new File(pathName, file_name);
				  //System.out.println(file.getName() + " Path: + "+ file.getAbsolutePath());
				  
//				  boolean exist = file.createNewFile();
//				  
//				  if (!exist)
//				 {
//					 
//		   	    		JOptionPane.showMessageDialog(this,"File already exists." +
//				  		              "Please make a new named file or mannully delete the old file.");
//				      return false;
//				  }
//				  else
//				  {
//					  FileWriter fstream = new FileWriter(file.getName());
				       FileWriter fstream = new FileWriter(file);
					  //System.out.println(file.getName() + " FILE GET NAME &Path: + "+ file.getAbsolutePath());
					  BufferedWriter out = new BufferedWriter(fstream);
			
					  
				       
			       // get color array
					  
				     Color[][] arr = field.getColorArray();
				     			     
				     
				    out.write("Tetris");
				    out.newLine();

				    out.write(Integer.toString(LEVEL));
				    out.write(",");
				    out.write(Integer.toString(SCORE));
				    out.write(",");
				    out.write(Integer.toString(baseScore));
				    out.write(",");
				    out.write(Integer.toString(time));
				    out.write(",");
				    out.newLine();
				    
				    

				    
				    // write rows and cols
				    out.write(Integer.toString(rows));
		    	    out.write(",");
				    out.newLine();
				    out.write(Integer.toString(cols));
		    	    out.write(",");
				    out.newLine();
			      
				       for(int i=0; i< cols; i++)
				    	   for(int j=0; j< rows; j++)
				    	   {
				    	       out.write(Integer.toString(i));
				    	       out.write(",");
				    	       out.write(Integer.toString(j));
				    	       out.write(",");
//				    	       out.newLine();

	                          // add color
				    	       if(arr[i][j]==null)
				    	    	      out.write("0");
				    	       else if(arr[i][j]==Color.blue)
				    	    	      out.write("1");
				    	       else if(arr[i][j]== Color.green)
				    	    	      out.write("2");
				    	       else if(arr[i][j]== Color.CYAN)
				    	    	      out.write("3");
				    	       else if(arr[i][j]== Color.RED)
				    	    	      out.write("4");
				    	       else if(arr[i][j]== Color.pink)
				    	    	      out.write("5");
				    	       else if(arr[i][j]== Color.YELLOW)
				    	    	      out.write("6");
				    	       else if(arr[i][j]== Color.orange)
				    	    	      out.write("7");
				    	       else 
				    	              out.write("1000");
				    	       
				    	       out.write(",");
				    	       out.newLine();

				    	   }
				       out.write(""+active.getClass().getName());			
		    	       out.write(",");
				       out.write(""+active.getX());
				       out.write(",");
				       out.write(""+active.getY());
				       out.write(",");
				       out.write(""+active.getRotationIndex());			
		    	       out.write(",");
				       out.newLine();

	    			   
	               // no more needs for this 
//				       for(int i=0; i< 4; i++)
//				    	   for(int j=0; j< 4; j++)
//				    	   {
//				    		   
//				    		   if(active.getModel()[active.getRotationIndex()][i][j])
//				    		   {
//				    			   
//				    			   out.write(""+i);
//				    			   out.write(",");
//				    			   out.write(""+j);
//				    			   out.write(",");
//				    			   out.write("1");
//				    			   out.write(",");
//				    			   out.newLine();
//				    		   }
//				    		   else
//				    		   {
//				    			   out.write(""+i);
//				    			   out.write(",");
//				    			   out.write(""+j);
//				    			   out.write(",");
//				    			   out.write("0");
//				    			   out.write(",");
//				    			   out.newLine();			    	   		       
//				                }
//				    	   }
				               out.write(""+nextPiece.getClass().getName());			
		    	               out.write(",");
						       out.write(""+nextPiece.getX());
						       out.write(",");
						       out.write(""+nextPiece.getY());
						       out.write(",");
						       out.write(""+nextPiece.getRotationIndex());
						       out.write(",");
						       out.newLine();

			               // no more needs for this  
//						       for(int i=0; i< 4; i++)
//						    	   for(int j=0; j< 4; j++)
//						    	   {
//						    		   
//						    		   if(nextPiece.getModel()[nextPiece.getRotationIndex()][i][j])
//						    		   {
//						    			   out.write(""+i);
//						    			   out.write(",");
//						    			   out.write(""+j);
//						    			   out.write(",");
//						    			   out.write("1");
//						    			   out.write(",");
//						    			   out.newLine();
//						    		   }
//						    		   else
//						    		   {
//						    			   out.write(""+i);
//						    			   out.write(",");
//						    			   out.write(""+j);
//						    			   out.write(",");
//						    			   out.write("0");
//						    			   out.write(",");
//						    			   out.newLine();			    	   		       
//						                }
//						    	   }
//				       
				       
					  // flush n exit it when its done
					   out.flush();
				       out.close();
					   JOptionPane.showMessageDialog(this,"File created successfully.");
					   return true;
//				  }
				  
			}
		     	//Catch exception if any
	       catch (IOException e)
	       {
	            System.err.println("Error: " + e.getMessage());
	       }
	       finally
	       {
	    	   requestFocusInWindow();
	    	   start();
	    	   
	       }
			return true;
	  }
    

		 public boolean load2(File file) throws IOException
		 {
			  
	         stop();
		     try
		     {
		    	   Scanner readName = new Scanner(System.in);
	    	       //String fileName;  
	    	       boolean exist = false;
	    	       int[][] arr;
	    	       
		    	
		    	    
			 		Scanner fileScan = new Scanner(file);

			 		int LEVEL = 0;
			 		int SCORE =0;
			 		int time = 0;
			 		int rows= 0;
			 		int cols=0;
			 		int activeRotationNumber =0;
			 		int newRotationNumber =0;
			 		
			 		
			 		
			 		String line="";
			 		String dummy ="";
			 		fileScan.useDelimiter(",");
			 		
			 		// first Line
		 			line = fileScan.nextLine();
	 		    	if((line.equals("Tetris"))==false)
	 		    	{
						  
						  
//						  int DOO =  JOptionPane.showConfirmDialog(this,"Nah, wrong Format, please check the file again",
//			  		              "\n Thank you!", JOptionPane.YES_NO_OPTION);

//			  		              "Click Yes to re-choose, No to omit it", JOptionPane.YES_NO_OPTION);
//						   JOptionPane.showMessageDialog(bp,"Please get the right data!! Thank you");

					   JOptionPane.showMessageDialog(this, "Nah, wrong Format, please check the file! Thank you");

//		   	            if (DOO == JOptionPane.YES_OPTION) //over writes
//		   	            {
//			   				return false;
//		   	            }
		   	                             // its kind trivia, since it return false regardless whatsoever.
		   	           return false;
	 		    	}
			 		    	
		    	   // second line
	 		    	     if(fileScan.hasNextInt())
		 		    	 {
	 		    	    	 LEVEL = fileScan.nextInt();
	                         //System.out.println("LEVEL "+ LEVEL);
		 		    	 }
		 		    	 if(fileScan.hasNextInt())
		 		    	 {
		 		    		 SCORE = fileScan.nextInt();
	                         //System.out.println("SCORE "+ SCORE);
		 		    	 }
		 		    	 if(fileScan.hasNextInt())
		 		    	 {
		 		    		 time  = fileScan.nextInt();
		 		    		 //System.out.println("Time "+ time);
		 		    	  }
		 		    	 
	                // 3rd line
				 		     if(fileScan.hasNextLine())
				 		         dummy =fileScan.nextLine();	 
	 				 		 if(fileScan.hasNextInt())
	 				 			 rows = fileScan.nextInt();

					 		// System.out.println(rows);

			 		//4th line
				 		     if(fileScan.hasNextLine())
				 		    	 dummy = fileScan.nextLine();
				 		     if(fileScan.hasNextInt())
				 		         cols = fileScan.nextInt();
				 		     
				 		    fileScan.nextLine();
				 		    // System.out.println(cols);

				       // assign array
				 		arr = new int[cols][rows];
				 		Color[][] tempBoardColorArr= new Color [cols][rows];
			 		    
			 		    for(int i =0; i<cols; i++)
			 		    {
			 		    	for(int j= 0; j< rows; j++)
		 		    	    {
		 		    		 
			 		    		if(fileScan.hasNextLine())
			 		    			dummy = fileScan.nextLine();
			 		    		
			 		    		String[] parts = dummy.split(",");
			 		    		//System.out.println(dummy);
			 		    		int a = Integer.parseInt(parts[parts.length-1]);
			 		    		 
			 		    		  tempBoardColorArr[i][j] = num2Color(a);
			 		    		//  field.setTileColor(i,j,tempBoardColorArr[i][j]);
			 		    		 
			 		    	 }
			 		    	
		                }
		    			  field.setColorArr(tempBoardColorArr);
			    		  
			 		    // starting reading active name 
			 		    String name;
			 		    Piece activeProj = null;
			 		    if(fileScan.hasNextLine())
			 		    {
			 		    	dummy = fileScan.nextLine();
			 		    	String[] info = dummy.split(",");
			 		    	name = info[0];
			 		    	int active_X = Integer.parseInt(info[1]);
			 		    	int active_Y = Integer.parseInt(info[2]);
			 		    	activeRotationNumber = Integer.parseInt(info[3]);
			 		    	
			 		    	if(name.equals("LeftPiece"))
		                        activeProj = new LeftPiece(field, active_X, active_Y);
			 		    	else if(name.equals("LeftZPiece"))
		                        activeProj = new LeftZPiece(field, active_X, active_Y);
			 		    	else if(name.equals("LinePiece"))
		                        activeProj = new LinePiece(field, active_X, active_Y);
			 		    	else if(name.equals("RightPiece"))
		                        activeProj = new RightPiece(field, active_X, active_Y);
			 		    	else if(name.equals("RightZPiece"))
		                        activeProj = new RightZPiece(field, active_X, active_Y);
			 		    	else if(name.equals("SquarePiece"))
		                        activeProj = new SquarePiece(field, active_X, active_Y);
			 		    	else if(name.equals("TPiece"))
		                        activeProj = new TPiece(field, active_X, active_Y);

			 		    		
			 		    	active = activeProj;
			 		    }
			 		    
			 		    
			 		    // starting reading names 
			 		    //String name;		 		 
			 		    Piece nextPieceProj = null;
			 		    if(fileScan.hasNextLine())
			 		    {
			 		    	
			 		   	dummy = fileScan.nextLine();
		 		    	String[] info = dummy.split(",");
		 		    	name = info[0];
		 		    	int active_X = Integer.parseInt(info[1]);
		 		    	int active_Y = Integer.parseInt(info[2]);
		 		    	newRotationNumber = Integer.parseInt(info[3]);

		 		    	if(name.equals("LeftPiece"))
		 		    		nextPieceProj = new LeftPiece(field, active_X, active_Y);
		 		    	else if(name.equals("LeftZPiece"))
		 		    		nextPieceProj = new LeftZPiece(field, active_X, active_Y);
		 		    	else if(name.equals("LinePiece"))
		 		    		nextPieceProj = new LinePiece(field, active_X, active_Y);
		 		    	else if(name.equals("RightPiece"))
		 		    		nextPieceProj = new RightPiece(field, active_X, active_Y);
		 		    	else if(name.equals("RightZPiece"))
		 		    		nextPieceProj = new RightZPiece(field, active_X, active_Y);
		 		    	else if(name.equals("SquarePiece"))
		 		    		nextPieceProj = new SquarePiece(field, active_X, active_Y);
		 		    	else 
		 		    		nextPieceProj = new TPiece(field, active_X, active_Y);

		 		    	    nextPiece = nextPieceProj;
		 		    	    preview.setPiece(nextPiece);
			 		    }
			 		    
			 		    
			 		// assign Values
					this.LEVEL = LEVEL;
					this.SCORE = SCORE;
					this.time = time;
					
					this.rows = rows;
					this.cols= cols;
					active.setRotationNum(activeRotationNumber);
					nextPiece.setRotationNum(newRotationNumber);

					//reset the Score;
					
					preview.setNumber(LEVEL, SCORE);
					
				//	active.setXY(activeX, activeY);
				//	nextPiece.setXY(newPieceX,newPieceY);
			 		    
		     }// try
		      catch (IOException e)
		      {
		    	  //Catch exception if any
			       System.err.println("Error: " + e.getMessage());
			  }// catch
		     finally
		     {
		    	 requestFocusInWindow();
		    	 start();
		     }
            return true;		    
	} // public 
	  
		 
		 public void requestFocus()
		 {
			 requestFocusInWindow();
		 }
     /**
      * set columns and rows
      * @param col columns
      * @param row rows
      */
     public void setColsnRows(int col, int row)
     {
   	  cols =col;
   	  rows = row;
     }
     
     public void restart()
     {
    	   generateNewPiece();
    	   active =  nextPiece;
    	   generateNewPiece();
           field.resetBoard();
		   timer.restart(); 
           requestFocus();
     }
     
     public void setNumber(int LEVEL, int SCORE)
     {
    	  this.LEVEL = LEVEL;
    	  this.SCORE = SCORE;
    	  
     }
     
     public void cheats(int choice)
     {
//     	switch (rand.nextInt(6))
     	switch(choice)
         {
 			//start the pieces off with negative y values so they enter
 			//starting at the bottom of the piece rather than the top
             case 0:
                 nextPiece = new LinePiece(field, field.WIDTH / 2, -2);
                 break;
             case 1:
 				nextPiece = new SquarePiece(field, field.WIDTH / 2, -2);
 				break;
             case 2:
 				nextPiece = new LeftPiece(field, field.WIDTH / 2, -2);
 				break;
             case 3:
 				nextPiece = new RightPiece(field, field.WIDTH / 2, -2);
 				break;
             case 4:
 				nextPiece = new LeftZPiece(field, field.WIDTH / 2, -2);
 				break;
             case 5:
 				nextPiece = new RightZPiece(field, field.WIDTH / 2, -2);
 				break;
             case 6:
 				nextPiece = new TPiece(field, field.WIDTH / 2, -2);
 				break;
           }
 			
     	preview.setPiece(nextPiece); 			
     	repaint();
	         	

     }
     
     
     
}
