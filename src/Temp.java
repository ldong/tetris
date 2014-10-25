import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class Temp 
{
      public static void main(String[] args) throws IOException
      {
    	  

		     try
		     {
		    	   Scanner readName = new Scanner(System.in);
	    	       String fileName;  
	    	       boolean exist = false;
	    	       int[][] arr;
	    	       
	    	       File file;
		    	    do
		    	    {
		    	    	System.out.print("Please input the file name :");
		    	        System.out.println();
		    	        fileName = readName.nextLine();
		    	        file= new File(fileName);
				 		 if(file.isFile())
				 		 {
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
			 		
			 		int count = 0;
			 		
			 		
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
             			 	 //	if(fileScan.hasNextLine())
				 		      //  dummy =fileScan.nextLine();
             			 		
				 		    	     if(fileScan.hasNextInt())
	     			 		    	 {
				 		    	    	 LEVEL = fileScan.nextInt();
	                                     System.out.println("LEVEL "+ LEVEL);
	     			 		    	 }
	     			 		    	 if(fileScan.hasNextInt())
	     			 		    	 {
	     			 		    		 SCORE = fileScan.nextInt();
	                                     System.out.println("SCORE "+ SCORE);
	     			 		    	 }
	     			 		    	 if(fileScan.hasNextInt())
	     			 		    	 {
	     			 		    		 time  = fileScan.nextInt();
	     			 		    		 System.out.println("Time "+ time);
	     			 		    	  }
                    // 3rd line
				 		     if(fileScan.hasNextLine())
				 		         dummy =fileScan.nextLine();	 
     				 		 if(fileScan.hasNextInt())
     				 			 rows = fileScan.nextInt();

					 		 System.out.println(rows);

			 		//4th line
				 		     if(fileScan.hasNextLine())
				 		    	 dummy = fileScan.nextLine();
				 		     if(fileScan.hasNextInt())
				 		         cols = fileScan.nextInt();
				 		     
				 		     System.out.println(cols);

				    // assign array
				 		arr = new int[cols][rows];
			 		    
			 		    int tri =0;
			 		    int color =0;
			 		    
			 		    for(int i =0; i<cols; i++)
			 		    	 for(int j= 0; j< rows; j++)
			 		         {
			 		    		 if (tri<2)
				 		    	 {
			 		    			 if(fileScan.hasNextLine())
					 		    	 dummy = fileScan.nextLine();
     					 		     if(fileScan.hasNextInt())
					 		         arr[i][j] = fileScan.nextInt();
     					 		     tri=3;
				 		    	 }
			 		    		 else 
			 		    		 {
			 		    			 if(fileScan.hasNextLine())
					 		    	 dummy = fileScan.nextLine();
     					 		     if(fileScan.hasNextInt())
    					 		      color = fileScan.nextInt();

			 		    		 }
			 		    			 
					 		     System.out.println(i+" "+j+ " " +arr[i][j]+
					 		          "Color "+color);
			 		         }
			 		    
			 		    // starting read active model
			 		    if(fileScan.hasNextLine())
			 		    {
			 		    	dummy = fileScan.nextLine();
			 		    	String a = dummy.substring(dummy.length()-2,dummy.length()-1);
			 		    	activeRotationNumber = Integer.parseInt(a);
			 		    	System.out.println(activeRotationNumber);
			 		    }
			 		    
			 		    int[][] activePiece = new int[4][4];
			 		    int activePieceColor =0;
			 		    for(int i =0; i<4; i++)
			 		    	 for(int j= 0; j< 4; j++)
			 		         {
			 		    		 if (tri<2)
				 		    	 {
			 		    			 if(fileScan.hasNextLine())
					 		    	 dummy = fileScan.nextLine();
    					 		     if(fileScan.hasNextInt())
					 		         activePiece[i][j] = fileScan.nextInt();
    					 		     tri=3;
				 		    	 }
			 		    		 else 
			 		    		 {
			 		    			 if(fileScan.hasNextLine())
					 		    	 dummy = fileScan.nextLine();
    					 		     if(fileScan.hasNextInt())
   					 		       activePieceColor = fileScan.nextInt();

			 		    		 }
			 		    			 
					 		     System.out.println(i+" "+j+ " " +activePiece[i][j]+
					 		          "Color "+activePieceColor);
			 		         }
			 		    
			 		    
			 		    
			 		    // starting read active model
			 		    if(fileScan.hasNextLine())
			 		    {
			 		    	dummy = fileScan.nextLine();
			 		    	String a = dummy.substring(dummy.length()-2,dummy.length()-1);
			 		    	newRotationNumber = Integer.parseInt(a);
			 		    	System.out.println(newRotationNumber);
			 		    }
			 		    
			 		    int[][] newNextPiece = new int[4][4];
			 		    int newNextPieceColor =0;
			 		    for(int i =0; i<4; i++)
			 		    	 for(int j= 0; j< 4; j++)
			 		         {
			 		    		 if (tri<2)
				 		    	 {
			 		    			 if(fileScan.hasNextLine())
					 		    	 dummy = fileScan.nextLine();
    					 		     if(fileScan.hasNextInt())
    					 		      newNextPiece[i][j] = fileScan.nextInt();
    					 		     tri=3;
				 		    	 }
			 		    		 else 
			 		    		 {
			 		    			 if(fileScan.hasNextLine())
					 		    	 dummy = fileScan.nextLine();
    					 		     if(fileScan.hasNextInt())
    					 		     newNextPieceColor = fileScan.nextInt();

			 		    		 }
			 		    			 
					 		     System.out.println(i+" "+j+ " " +newNextPiece[i][j]+
					 		          "Color "+newNextPieceColor);
			 		         }
			 		    
			 		    
		     }// try
		      catch (IOException e)
		      {
		    	  //Catch exception if any
			       System.err.println("Error: " + e.getMessage());
			  }// catch
   } // public 
      
}
