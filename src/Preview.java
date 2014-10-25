import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.*;

/**
 * JPanel for Preview next piece 
 * which also implements Save/ Load button options
 * @author Lin Dong 
 */
public class Preview extends JPanel 
{

    /*
     *  Scores and Level Labels
     */
	private BoardPanel bp;
    private Piece piece;
    private JLabel score, level;
    private int LEVEL = 1;
    private int SCORE = 0;
    private boolean[][][] model;
    private Dimension projection = new Dimension(10, 10);
    private JButton save, load, about, save2file, load2file, start, pause, reset;
    private JFileChooser fc;
    private JLabel showNext;
    
    private JButton cheat1, cheat2, cheat3, cheat4, cheat5, cheat6, cheat7;
    
    // for read and write vairbales
    private int cols, rows;
    
    public Preview()
    {
        //this.bp = bp;
        level = new JLabel("Level: "+ LEVEL);
        score = new JLabel("Score: "+ SCORE);
        save = new JButton("Save");
        load = new JButton("Load");
        about = new JButton("About Me");
        save2file =new JButton("Save to File");
        load2file = new JButton("Load from File");
        showNext = new JLabel("Show Next");
        
        
        
        
        start = new JButton("Start");
        pause = new JButton("Pause");
        reset = new JButton("Start-Over");
        

        
        
        // stop it and ask the user to choose
//        bp.stop();

        add(level);
        add(score);
     
       /*   for terminal use only, get file name from the prompt*/
       //   add(save);
       //   add(load);
        add(start);
        add(pause);
        add(reset);
        add(save2file);
        add(load2file);
        add(about);
        
       /* optional, give your best shot */
        
       //  showNext.setBounds(15, 10, 3, 6);
       //  add(showNext);
       //  this.setLayout(new BorderLayout());
       //  add(showNext, java.awt.BorderLayout.CENTER); 

        save.addActionListener(new ButtonListener(0));
        load.addActionListener(new ButtonListener(1));
        about.addActionListener(new ButtonListener(2));
        save2file.addActionListener(new ButtonListener(3));
        load2file.addActionListener(new ButtonListener(4));
        
        
        start.addActionListener(new ButtonListener(18));
        pause.addActionListener(new ButtonListener(19));
        reset.addActionListener(new ButtonListener(20));
        
        
        cheat1 = new JButton("LinePiece");
        cheat2 = new JButton("SquarePiece");
        cheat3 = new JButton("LeftPiece");
        cheat4 = new JButton("RightPiece");
        cheat5 = new JButton ("LeftZPiece");
        cheat6 = new JButton("RightZPiece");
        cheat7 = new JButton("TPiece");
        
        cheat1.setVisible(false);
        cheat2.setVisible(false);
        cheat3.setVisible(false);
        cheat4.setVisible(false);
        cheat5.setVisible(false);
        cheat6.setVisible(false);
        cheat7.setVisible(false);
        
        cheat1.addActionListener(new ButtonListener(10));
        cheat2.addActionListener(new ButtonListener(11));
        cheat3.addActionListener(new ButtonListener(12));
        cheat4.addActionListener(new ButtonListener(13));
        cheat5.addActionListener(new ButtonListener(14));
        cheat6.addActionListener(new ButtonListener(15));
        cheat7.addActionListener(new ButtonListener(16));
        
        add(cheat1);
        add(cheat2);
        add(cheat3);
        add(cheat4);
        add(cheat5);
        add(cheat6);
        add(cheat7);

        
        
    }
    
    
    
    
    
    
    public void setBoardPanel(BoardPanel bp)
    {
    	 this.bp = bp;
    }
    
    /**
     * make a copy of the tileDimension
     * @param tileDimension assign projection as tileDimension 
     */
    public void setDimension(Dimension tileDimension)
    {
    	 projection = tileDimension; 
    }
    
    /**
     * assign other piece from other place to this current piece
     * which insides of Preview 
     * @param piece a piece obj
     */
    public void setPiece(Piece piece)
    {
    	this.piece = piece;

    }
    
    public Piece getPiece()
    {
    	return piece;
    }
    
    
    /**
     * After doing the logical from BoardPanel
     * assign these number stats here and display it
     * on the preview Panel
     * @param LEVEL assign the LEVEL from boardPanel to preview
     *              and display it here
     * @param SCORE assign the SCORE from boardPanel to preview
     *              and display it here
     */
    
    public void setNumber(int LEVEL, int SCORE)
    {
   	  this.LEVEL = LEVEL;
   	  this.SCORE = SCORE;
   	  
   	  level.setText("Level: "+ LEVEL);
	  score.setText("Score: "+ SCORE);
    }
     
    /**
     * displays the piece
     */
     public void paintComponent(Graphics g)
     {
    	 super.paintComponent(g);

    	 /*
    	  * overloads the draw method in Piece
    	  * since only uses it in this method
    	  * I codes it here.
    	  */
    	 
    	 /*
    	  * Assign x, y to be 2/3 value of the projection
    	  * 
    	  */
        int x = (int)(projection.getWidth())*4/5 ;
        int y = (int)(projection.getHeight())*4/5 ;
    	 model = piece.getModel();
         g.setColor(piece.getColor());
         
         for (int i = 0; i < model.length; i++)
             for (int j = 0; j < model[0].length; j++)
                 if (model[piece.getRotationIndex()][i][j])
                     g.fill3DRect((5 + i ) * x, (15 + j) *y, x, y, true);
     }
     

     
     public void setCheat(boolean yes)
     {
    	 if(yes)
    	 {
	         cheat1.setVisible(true);
	         cheat2.setVisible(true);
	         cheat3.setVisible(true);
	         cheat4.setVisible(true);
	         cheat5.setVisible(true);
	         cheat6.setVisible(true);
	         cheat7.setVisible(true);
    	 }
    	 else
    	 {
    	        cheat1.setVisible(false);
    	        cheat2.setVisible(false);
    	        cheat3.setVisible(false);
    	        cheat4.setVisible(false);
    	        cheat5.setVisible(false);
    	        cheat6.setVisible(false);
    	        cheat7.setVisible(false);
    	 }    		 
         bp.requestFocus();

    	 
     }

     public void cheats() throws IOException
     {
        int num1 = 1, num2 =1000;
        String input1 = JOptionPane.showInputDialog("The Level number ->");
		if(input1.equals(JOptionPane.YES_OPTION))
   			 num1=Integer.parseInt(input1);
		else if (input1 == null)
			  num1 = 1;
   		
   		String input2 = JOptionPane.showInputDialog("The Score number ->");
		if(input2.equals(JOptionPane.YES_OPTION))
   		     num2=Integer.parseInt(input2);
		else if (input2 == null)
			  num2 = 0;
		
   		JOptionPane.showMessageDialog(bp, "Enjoy !!");
        bp.setNumber(num1, num2);
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
   	    @SuppressWarnings("static-access")
		public void actionPerformed(ActionEvent e) 
   	    {
   	    	boolean oneTime = true;

   	    	
   	    	
   	    	
   	    	
   	    	
   	    	// get different pieces
   	    	if(choice >= 10 && choice <18 )
   	    	{
   	    	     	if(choice == 10)
		   	    	{ bp.cheats(0);   }
		   	    	else if(choice ==11)
		   	    	{ bp.cheats(1);   }
		   	    	else if(choice ==12)
		   	    	{ bp.cheats(2);   }
		   	    	else if(choice ==13)
		   	    	{ bp.cheats(3);   }
		   	    	else if(choice ==14)
		   	    	{ bp.cheats(4);   }
		   	    	else if(choice ==15)
		   	    	{ bp.cheats(5);   }
		   	    	else if(choice ==16)
		   	    	{ bp.cheats(6);   }
		   	    	else if(choice ==17)
		   	    	{ bp.cheats(7);   }
   	       	     bp.requestFocus();

   	    	}

   	    	// start game
   	    	else if(choice ==18)
   	    	{   
   	    		bp.setFocusable(true);
   	    		bp.requestFocus();
   	    		bp.start();   

   	    		
   	    	}
   	    	
   	    	// pause game 
   	    	else if(choice ==19)
   	    	{
   	    		bp.stop();
   	    		bp.setFocusable(false);
   	    		
   	    	}
   	    	
   	    	//restart game
   	    	else if(choice ==20)
   	    	{ 
   	    		bp.setFocusable(true);
   	    		bp.requestFocus();
   	    		bp.restart(); 
   	        }
   	    	

   	    	
   	    	
   	    	// cheat the score
//   	    	else if(choice ==22)
//   	    	{   	
//   	    		
//   	    		String input1 = JOptionPane.showInputDialog("The Level number ->");
//   	    		int num1=Integer.parseInt(input1);
//   	    		String input2 = JOptionPane.showInputDialog("The Score number ->");
//   	    		int num2=Integer.parseInt(input2);
//   	    		JOptionPane.showMessageDialog(bp, "Enjoy !!");
//                bp.setNumber(num1, num2);
//   	     	  
//   	    		
//   	    	}
   	    	
   	    	//save 
   	    	else if(choice == 0 ) 
   	    	{
   	    		try
   	    		{
   	    			if(oneTime)
  					bp.save2File();
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
  					bp.load();
   	    			oneTime = false;
  				}
   	    		catch (IOException e1) 
   	    		{
  					System.out.println("Error");					
  				}
   	    	}
   	    	else if(choice==2)
   	    	{
   	    		bp.stop();
   	    		
   	    		JOptionPane.showMessageDialog(about,"Author: Lin Dong\n" +
   	            		"It's an open sourced java-based software, just enjoy it!\n"
   	            		+"April 23rd, 2012 @ GaTech\n", "About Me", 1);
//   	            		+"Thank to my TAs, Hongbo Tian, and etc");
   	    		
				int DOO =  JOptionPane.showConfirmDialog(bp,"Do you want to see someting fancy?",
	  		              "DARE YOU!!", JOptionPane.YES_NO_OPTION);
   	    		
   	            
   	            if (DOO == JOptionPane.YES_OPTION) 
   	            {
   	            	//reveal the cheats 
   	   	    		JOptionPane.showMessageDialog(bp,"Oops."+
   	   	    				" These are not meant to be." +
   	   	    				"\nCheats \n Z -> Customize Scores. " +
   	   	    				"\n X -> Show Controller. \n C -> Hide Controller. \n ENJOY!");

   				}
   				else //No option
   				{
   					//System.exit(0);
   				}
   	            
   	            /*
   	    		JOptionPane.showConfirmDialog(about, "Author: Lin Dong\n" +
   	            		"It's an open sourced java freeware, free and enjoy it!"
   	            		+"April 23rd@ GaTech\n" );
   	            		*/
   	    		bp.requestFocus();
   	    		bp.start();

   	    	}
   	    	
   	    	else if(choice ==3)
   	    	{ 
   	    		bp.stop();
   	    		fc = new JFileChooser();
   	    		// set default directory as the same folder 
   	    		fc.setCurrentDirectory(null);
   	 		    int result = fc.showSaveDialog(bp.getParent());
//   	 		    System.out.println("a - >"+fc.getName());
//   	 		    fc.setSelectedFile(new File(fc.getName()));
//   	 		    System.out.println(fc.getName());

   	 		    	if (result == JFileChooser.APPROVE_OPTION) 
	   			    {
//		   			     System.out.println("Shit works");
		   			     
		   				  File file =fc.getSelectedFile();
		   				
						  boolean exist =false;
						  
						 try {
							exist = file.createNewFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						  
						 if (!exist)
						 {
							 
//				   	    		JOptionPane.showMessageDialog(bp,"File already exists." +
//						  		              "Please make a new named file or mannully delete the old file.");
							  int DOO =  JOptionPane.showConfirmDialog(bp,"File already exists." +
					  		              "Please make a new named file or mannully delete the old file.","Click Yes to overwrite", JOptionPane.YES_NO_OPTION);
				   	    		
				   	            
				   	            if (DOO == JOptionPane.YES_OPTION) //over writes
				   	            {
				   	            	// delete it and then over writes
				   	            	fc.getSelectedFile().delete();
					   				bp.save2(file, fc.getSelectedFile().getParentFile().getAbsolutePath());

				   				}
				   				else //No option
				   				{
				   					//System.exit(0);
				   				}
				   	            
				   	            
				   	    		
				   	    		
						  }
						  else
						  {
//		   				    bp.save2(file.getName(), fc.getSelectedFile().getParentFile().getAbsolutePath());
			   				bp.save2(file, fc.getSelectedFile().getParentFile().getAbsolutePath());

						  }
		    	    	  //bp.requestFocusInWindow();
		
		   				  
	   		     	}
   	 		    	
	   			    else if (result == fc.CANCEL_OPTION) 
	   			    {
	   				   
	   			    }
	   			 bp.start();
   	    	}
   	    	
   	    	/*********************************/
   	    	else if(choice ==4)
   	    	{   	   	   
   	    		bp.stop();
   	    	    fc = new JFileChooser();
   	    	  
	    		// set default directory as the same folder 
	    		fc.setCurrentDirectory(null);
	 		    int result = fc.showOpenDialog(bp);
//	 		    		showSaveDialog(bp.getParent());
//	 		    System.out.println("a - >"+fc.getName());
//	 		    fc.setSelectedFile(new File(fc.getName()));
//	 		    System.out.println(fc.getName());

	 		     if (result == JFileChooser.APPROVE_OPTION) 
   			     {
//	   			     System.out.println("Shit works");
	   			     
	   				  File file =fc.getSelectedFile();
	
					  boolean exist = true;
					 if (exist)
					 {
						 System.out.println("!exist");
//			   	    		JOptionPane.showMessageDialog(bp,"File already exists." +
//					  		              "Please make a new named file or mannully delete the old file.");
						  JOptionPane.showMessageDialog(bp,"Loading now....\nPlease wait");
			   	    	  
			   	           
//			   	            if (DOO == JOptionPane.YES_OPTION) //over writes
//			   	            {
			   	            	// delete it and then over writes
//			   	            	fc.getSelectedFile().delete();
//				   				bp.save2(file, fc.getSelectedFile().getParentFile().getAbsolutePath());

//			   				}
//			   				else //No option
//			   				{
//			   					//System.exit(0);
//			   				}
			   	            
			   	            try {
								bp.load2(file);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			   	    		
			   	    		
					  }
					 
//					  else
//					  {
//	   				    bp.save2(file.getName(), fc.getSelectedFile().getParentFile().getAbsolutePath());
//		   				bp.save2(file, fc.getSelectedFile().getParentFile().getAbsolutePath());

//					  }
	    	    	  //bp.requestFocusInWindow();
	
	   				  
   		     	}
	 		    	
   			    else if (result == fc.CANCEL_OPTION) 
   			    {
   				   
   			    }
   		     	 bp.start();
   	    	}
   	    }
   	    
      }
      
}
