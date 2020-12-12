// Nicks Chess Catastrophe (at least so far!!)
// Version 1.02
// Features added since 1.01:
// -- Added another 2 rows of cols and buttons to make knights work (in theory)   
// -- fixed [col][row] inconsistencies.
// -- Removed knight only working between rows 2 and 7, which was no longer necessary due to the 2 additional cols and rows.
//

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.color.*;
import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;
import java.util.Scanner;
// add an unfreeze
public class printingchess extends JFrame {
    int buttonWidth,buttonHeight,currentRow,currentCol;
    final String whiteking = "\u2654";
    final String whitequeen = "\u2655";
    final String whiterook = "\u2656";
    final String whitebishop = "\u2657";
    final String whiteknight = "\u2658";
    final String whitepawn = "\u2659";
    final String blackking = "\u265A";
    final String blackqueen = "\u265B";
    final String blackrook = "\u265C";
    final String blackbishop = "\u265D";
    final String blackknight = "\u265E";
	final String blackpawn = "\u265F";
    final String blank = "  ";
    printingchess frame;
    GridLayout chessboard;
    JTextField loadgamefield;
    JButton loadgamebutton;
    JButton newgamebutton;
    JButton buttons[][];
    JLabel filestatus;
    ClickLoadListener loader = new ClickLoadListener();
    clickselectionaction selectionaction = new clickselectionaction();
    moveselector makeAMove = new moveselector();
    resetselector reset = new resetselector();
    nothingthing nothing = new nothingthing();
    String status;
    String errormessage;
    int wpawns[];
    int bpawns[];
    char whoseturn = 'W';
	int whiteturnnum = 1; 
    int blackturnnum = 1;
    public String[][] gridarray;
	int numrows = 8;
    int numcols = 8;
    int totalrows = 12;
    int totalcols = 12;
    int holdrow;
    int holdcol;
    public printingchess() {
        buttonWidth = 60;
        buttonHeight = 60;
        chessboard = new GridLayout(12, 12);
        status = "File Not Loaded";
        buttons = new JButton[totalcols][totalrows];
        wpawns = new int[10];
        bpawns = new int[10];

        gridarray = new String[totalcols][totalrows];
        for (int row=0; row<totalrows; row++)//re-dundant re-initialize
		{
		    for (int col=0; col<totalcols; col++)
			    {
			    gridarray[col][row]=blank;
			    }
        }
        for (int pawn = 2; pawn<=9; pawn++) {
            gridarray[pawn][3] = blackpawn; 
            gridarray[pawn][8] = whitepawn; 
            System.out.print(pawn);
        }
        gridarray[2][2] = blackrook;
        gridarray[3][2] = blackknight;
        gridarray[4][2] = blackbishop;
        gridarray[5][2] = blackqueen;
        gridarray[6][2] = blackking;
        gridarray[7][2] = blackbishop;
        gridarray[8][2] = blackknight;
        gridarray[9][2] = blackrook;
        gridarray[2][9] = whiterook;
        gridarray[3][9] = whiteknight;
        gridarray[4][9] = whitebishop;
        gridarray[5][9] = whitequeen;
        gridarray[6][9] = whiteking;
        gridarray[7][9] = whitebishop;
        gridarray[8][9] = whiteknight;
        gridarray[9][9] = whiterook;
    }

    public void turns() {
		if (whoseturn == 'W') {
            whiteturnnum++;
            whoseturn = 'B';
		} else {
            blackturnnum++;
            whoseturn = 'W';
		}
	} 

    public void settingButton(int col, int row, String contents)//sets contents of ONE button to match Life grid
    {
    System.out.println("  Col: " + col + "Row: " + row + " Piece: " + contents);
    buttons[col][row].setText(" "+contents+" ");
    buttons[col][row].putClientProperty("ROW",row);
    buttons[col][row].putClientProperty("COL",col);
    buttons[col][row].addActionListener(selectionaction);
    buttons[col][row].setBackground(null);
    //maps 2D to 1D
    };

   /*  public void settingButtons()//sets contents of ALL buttonS to match Life grid
    {
    for (int row=2;row<=numrows+1; row++){
        for (int col=2;col<=numcols+1; col++) {
            settingButton(col,row,gridarray[col][row]);
            System.out.println(gridarray[col][row] + "gridarray stuff");//debug
            }
        System.out.println();//debug
        }
    } */

    public void settingButtons()//sets contents of ALL buttonS to match Life grid
    {
    for (int row=0;row<=numrows+3; row++){
        for (int col=0;col<=numcols+3; col++) {
            settingButton(col,row,gridarray[col][row]);
            System.out.println(gridarray[col][row] + "gridarray stuff");//debug
            }
        System.out.println();//debug
        }
    }

    public void freezeboard() {
        for (int row=2;row<=numrows+1; row++){
            for (int col=2;col<=numcols+1; col++) {
            buttons[col][row].addActionListener(nothing);
            }
        }
    } 

    public void selectmove(int col, int row, String piece) {
        //freezeboard();
        if ((piece == whitepawn) || (piece == blackpawn)) {
            if (piece == whitepawn) {    
                if (gridarray[col][row-1] == blank) {
                    buttons[col][row-1].addActionListener(makeAMove);
                    buttons[col][row-1].setBackground(Color.CYAN);
                }
                if (gridarray[col-1][row-1] != blank) {
                    buttons[col-1][row-1].addActionListener(makeAMove);
                    buttons[col-1][row-1].setBackground(Color.CYAN);
                }
                if (gridarray[col+1][row-1] != blank) {
                    buttons[col+1][row-1].addActionListener(makeAMove);
                    buttons[col+1][row-1].setBackground(Color.CYAN);
                } 
                if ((wpawns[col] == 0) && (gridarray[col][row-2] == blank)) {
                        buttons[col][row-2].addActionListener(makeAMove);
                        buttons[col][row-2].setBackground(Color.CYAN);
                        wpawns[col] = 1;
                    }
            } else {
                if (gridarray[col][row+1] == blank) {
                    buttons[col][row+1].addActionListener(makeAMove);
                    buttons[col][row+1].setBackground(Color.CYAN);
                }
                if (gridarray[col+1][row+1] != blank) {
                    buttons[col-1][row+1].addActionListener(makeAMove);
                    buttons[col-1][row+1].setBackground(Color.CYAN);
                }
                if (gridarray[col+1][row-1] != blank) {
                    buttons[col+1][row+1].addActionListener(makeAMove);
                    buttons[col+1][row+1].setBackground(Color.CYAN);
                } 
                
                if ((bpawns[col] == 0) && (gridarray[col][row+2] == blank)) {
                    buttons[col][row+2].addActionListener(makeAMove);
                    buttons[col][row+2].setBackground(Color.CYAN);
                    bpawns[col] = 1;
                    }
                }
            buttons[col][row].addActionListener(reset);
		 } else if ((piece == whiteknight)||(piece == blackknight)) {
            buttons[col-1][row+2].addActionListener(makeAMove);
            buttons[col-1][row+2].setBackground(Color.CYAN);
            buttons[col+1][row+2].addActionListener(makeAMove);
            buttons[col+1][row+2].setBackground(Color.CYAN); 
            buttons[col+2][row+1].addActionListener(makeAMove);
            buttons[col+2][row+1].setBackground(Color.CYAN);
            buttons[col-2][row+1].addActionListener(makeAMove);
            buttons[col-2][row+1].setBackground(Color.CYAN);
            buttons[col+2][row-1].addActionListener(makeAMove);
            buttons[col+2][row-1].setBackground(Color.CYAN);
            buttons[col-2][row-1].addActionListener(makeAMove);
            buttons[col-2][row-1].setBackground(Color.CYAN);
            buttons[col+1][row-2].addActionListener(makeAMove);
            buttons[col+1][row-2].setBackground(Color.CYAN); 
            buttons[col-1][row-2].addActionListener(makeAMove);
            buttons[col-1][row-2].setBackground(Color.CYAN); 
            buttons[col][row].addActionListener(reset);
             //else if (row)

		} else if ((piece == whiteking)||(piece == blackking)) {
            for (int rows=-1; rows<=1; rows++) {
                for (int cols=-1; cols<=1; cols++) {
                   buttons[col+cols][row+rows].addActionListener(makeAMove);
                   buttons[col+cols][row+rows].setBackground(Color.CYAN);  
                }
                buttons[col][row].addActionListener(reset);
            }
            
		} else if ((piece == whitequeen)||(piece == blackqueen)) {
            for (int rows = 2; rows<=9; rows++) {
                buttons[col][rows].addActionListener(makeAMove);
                buttons[col][rows].setBackground(Color.CYAN);

            }
            for (int cols = 2; cols<=9; cols++) {
                buttons[cols][row].addActionListener(makeAMove);
                buttons[cols][row].setBackground(Color.CYAN);
            }
            int val=1;
            while (row-val > 0 && col-val > 0) {
                buttons[col-val][row-val].addActionListener(makeAMove);
                buttons[col-val][row-val].setBackground(Color.CYAN);
                buttons[col+val][row-val].addActionListener(makeAMove); 
                buttons[col+val][row-val].setBackground(Color.CYAN);
                buttons[col-val][row+val].addActionListener(makeAMove);
                buttons[col-val][row+val].setBackground(Color.CYAN);
                buttons[col+val][row+val].addActionListener(makeAMove);
                buttons[col+val][row+val].setBackground(Color.CYAN);
                val++;
            }
            buttons[col][row].addActionListener(reset);
        } else if ((piece == whitebishop)||(piece == blackbishop)) {
            //if () {
                //there is a piece in the way of the bishop
            //}
            int val=1;
            while (val<9) {
                if ((row-val > 0) && (col-val > 0)) {
                    buttons[col-val][row-val].addActionListener(makeAMove);
                    buttons[col-val][row-val].setBackground(Color.CYAN);
                }
                if((col+val < 11) && (row-val > 0)) {
                    buttons[col+val][row-val].addActionListener(makeAMove); 
                    buttons[col+val][row-val].setBackground(Color.CYAN);
                }
                if ((col-val > 0)&&(row+val < 11)) {
                    buttons[col-val][row+val].addActionListener(makeAMove);
                    buttons[col-val][row+val].setBackground(Color.CYAN);
                }
                if ((col+val < 11)&&(row+val < 11)) {
                    buttons[col+val][row+val].addActionListener(makeAMove);
                    buttons[col+val][row+val].setBackground(Color.CYAN);
                }  
                val++;  
            }
                
            
            

            buttons[col][row].addActionListener(reset);
        } else if ((piece == whiterook)||(piece == blackrook)) {
            for (int rows = 2; rows<=9; rows++) {
                buttons[col][rows].addActionListener(makeAMove);
                buttons[col][rows].setBackground(Color.CYAN);
            }
            for (int cols = 2; cols<=9; cols++) {
                buttons[cols][row].addActionListener(makeAMove);
                buttons[cols][row].setBackground(Color.CYAN);
            }
            buttons[col][row].addActionListener(reset);  
        }
    } 


    public void moves(int col, int row, int tempcol, int temprow, String contents) {
        String hold = gridarray[tempcol][temprow];
        System.out.println(hold);
        gridarray[col][row] = hold;
        gridarray[tempcol][temprow] = blank;
        turns(); 
        settingButtons();
    }



    public class ClickLoadListener implements ActionListener//load from file
    {
    public void actionPerformed(ActionEvent event)
        {
        //System.out.println("Loading Life from:");
        try{ //matches below: }catch(IOException e)
        String inFileName = loadgamefield.getText();
        File inputFile = new File(inFileName);
        Scanner inFile = new Scanner(inputFile);
        //System.out.println(inFileName);
        status = "File Loaded";
        filestatus.setText(status);
        //readWorldFile(inFile);
        inFile.close();
        }catch(IOException e)
        {
        System.err.print("IO Exception:");
        System.err.println(e);
        status="Invalid File Name or File Unreadable. Please Enter Valid File Name.";
        }
    settingButtons();
            }
    }


    public class clickselectionaction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JButton holdButton=(JButton)event.getSource();//get pointer to button object (source of the event)
            int row=(int)holdButton.getClientProperty("ROW");
            int col=(int)holdButton.getClientProperty("COL");
            if (whoseturn == 'B') {
                if ((gridarray[col][row] == blackbishop) || (gridarray[col][row] == blackking) || (gridarray[col][row] == blackrook) || (gridarray[col][row] == blackqueen) || (gridarray[col][row] == blackpawn) || (gridarray[col][row] == blackknight)) {
                    selectmove(col, row, gridarray[col][row]);
                    buttons[col][row].setBackground(Color.BLUE);
                    buttons[col][row].addActionListener(reset);
                    holdrow = row;
                    holdcol = col;
                } else {
                    errormessage = "Please select a piece of your own color.";
                }
            } else if (whoseturn == 'W') {
                System.out.println("Whoseturn- " + whoseturn + "   spot- " + gridarray[col][row]);
                if ((gridarray[col][row] == whiterook) || (gridarray[col][row] == whitepawn) || (gridarray[col][row] == whitebishop) || (gridarray[col][row] == whiteking) || (gridarray[col][row] == whitequeen) || (gridarray[col][row] == whiteknight)){
                    selectmove(col, row, gridarray[col][row]);
                    buttons[col][row].setBackground(Color.BLUE);
                    buttons[col][row].addActionListener(reset);
                    holdrow = row;
                    holdcol = col;
                } else {
                    errormessage = "Please select a piece of your own color.";
                }
            } else {
                System.out.println("This is an empty space. Please select a piece,");
            }
            
        }
    } 

    public class resetselector implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JButton holdButton=(JButton)event.getSource();//get pointer to button object (source of the event)
            int row=(int)holdButton.getClientProperty("ROW");
            int col=(int)holdButton.getClientProperty("COL");
            buttons[col][row].addActionListener(selectionaction);
            for (int cols = 2; cols<=numrows+1; cols++) {
                for (int rows = 2; rows<=numrows+1; rows++) {
                    buttons[col][row].setBackground(Color.WHITE);
                    buttons[col][row].addActionListener(selectionaction);
                }
            }  
        }
    }

    public class moveselector implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JButton holdButton=(JButton)event.getSource();//get pointer to button object (source of the event)
            int row=(int)holdButton.getClientProperty("ROW");
            int col=(int)holdButton.getClientProperty("COL");
            moves(col, row, holdcol, holdrow, gridarray[col][row]);
        }
    }


    public class nothingthing implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.out.println("Board Frozen!!!");
        }
    }

    public void addComponentsToPane(final Container pane) {
        final JPanel chesspanel = new JPanel();
        // Add Buttons to Grid       
        for (int row=0; row<=11; row++) {
            for (int col=0; col<=11; col++) {
                buttons[col][row] = new JButton(gridarray[col][row]);
            }
        }
        
        for (int row=2;row<=numrows+1; row++)
	    {
	        for (int col=2;col<=numcols+1; col++)
		    {//convert (row,col) to ((row-1)*maxCols+(col-1)) to convert 2D array into 1D array
            buttons[col][row].setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		    chesspanel.add(buttons[col][row]);
		    }
	    }
        pane.add(chesspanel, BorderLayout.NORTH);
        //chesspanel.setPreferredSize(new Dimension((int)(buttonWidth * 24)+20,120));
        chesspanel.setPreferredSize(new Dimension(475,600));
        chesspanel.setVisible(true);

        final JPanel settingspanel = new JPanel();
        newgamebutton = new JButton("New Game");
        loadgamebutton = new JButton("Load Game");
        loadgamebutton.addActionListener(loader);
        loadgamefield = new JTextField("Enter File Name");
        filestatus = new JLabel(status);
        settingspanel.add(newgamebutton);
        settingspanel.add(loadgamebutton);
        settingspanel.add(loadgamefield);
        settingspanel.setPreferredSize(new Dimension(475, 100));
        settingspanel.setVisible(true);
        pane.add(settingspanel, BorderLayout.SOUTH);
       
        
    }

    public void run() {
        frame = new printingchess();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(475, 800));
        addComponentsToPane(frame.getContentPane()); 
        settingButtons();
    }

    public static void main(String[] args) {
        printingchess chessstuff = new printingchess();
        chessstuff.run();
        
    }
}

