import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.sound.sampled.*;

public class Penta extends JFrame implements Runnable{
  private JTextField idField; 
  
  private JTextArea displayArea; 
  
  private JPanel boardPanel, mainPanel; 
  
  private Square board[][]; 
  
  private boolean gameEnds=false; 
  
  
  private Thread outputThread; 
  
  private char currentMark; 
  
  private Linked l1=new Linked(-1,-1,' '); 
  
  private Linked l2;
  
  private byte board2[][]=new byte[8][8];
  
  private Font SansSerif;
  
  private MiniMax mini=new MiniMax(board2);
  
  private RowCol square = new RowCol(-1,-1); 
  
  private final long infinity = 10000000; 
  
  private AudioInputStream audio; 
  
  private Clip buttonClickSound;
  
  public static void main(String[]args)
  {
	  new Penta();
  }

  public Penta()
  {
	  setTitle("Penta, Made By Khen Aharon");
	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  
	  displayArea=new JTextArea(4, 30);
	  displayArea.setEditable(false);
	  SansSerif = new Font ("SansSerif", Font.BOLD, 14);
	  displayArea.setFont(SansSerif);
	  displayArea.setText("Penta\nKhen Aharon");
	  this.add(new JScrollPane(displayArea), BorderLayout.SOUTH);
	  idField= new JTextField();
	  idField.setEditable(false);
	  this.add(idField, BorderLayout.NORTH); 
	  
	  boardPanel = new JPanel();
	  boardPanel.setBackground(Color.gray);											
	  boardPanel.setLayout(new GridLayout(8,8,3,3));
	  
	  board=new Square[8][8];
	  for(int row=0;row<board.length;row++) 
	  {
		  for(int column=0;column<board[row].length;column++)
		  {
			  board[row][column]= new Square(' ',row*8+column);
			  boardPanel.add(board[row][column]);
		  }
	  }
	  //////////////////////////////////////////////////////////////
	  
	  mainPanel=new JPanel();
	  mainPanel.add(boardPanel, BorderLayout.CENTER);
	  this.add(mainPanel, BorderLayout.CENTER);
	
	  /////////////////////

	  Border raisedbevel, loweredbevel,compound;
	  raisedbevel = BorderFactory.createRaisedBevelBorder();
	  loweredbevel = BorderFactory.createLoweredBevelBorder();
	  Border line = BorderFactory.createLineBorder(Color.BLACK);
	  //creates a frame.
	  compound = BorderFactory.createCompoundBorder(
				  raisedbevel, loweredbevel);
	  //Add an outline to frame.
	  compound = BorderFactory.createCompoundBorder(
				  line, compound);
	  //Add a title to the red-outlined frame.
	  
	  boardPanel.setBorder(compound);
	  setSize(400, 600);
	  setVisible(true);
	  
	 /* for(int g=30;g<96;g++)
		{
		
		board[mini.fiveInRowWins[g][0].line][mini.fiveInRowWins[g][0].colmn].setMark('B');
		board[mini.fiveInRowWins[g][1].line][mini.fiveInRowWins[g][1].colmn].setMark('B');
		board[mini.fiveInRowWins[g][2].line][mini.fiveInRowWins[g][2].colmn].setMark('B');
		board[mini.fiveInRowWins[g][3].line][mini.fiveInRowWins[g][3].colmn].setMark('B');
		board[mini.fiveInRowWins[g][4].line][mini.fiveInRowWins[g][4].colmn].setMark('B');
		try{
			Thread.sleep(500); // Sleep for sec
			}
			catch(InterruptedException e){}
		board[mini.fiveInRowWins[g][0].line][mini.fiveInRowWins[g][0].colmn].setMark(' ');
		board[mini.fiveInRowWins[g][1].line][mini.fiveInRowWins[g][1].colmn].setMark(' ');
		board[mini.fiveInRowWins[g][2].line][mini.fiveInRowWins[g][2].colmn].setMark(' ');
		board[mini.fiveInRowWins[g][3].line][mini.fiveInRowWins[g][3].colmn].setMark(' ');
		board[mini.fiveInRowWins[g][4].line][mini.fiveInRowWins[g][4].colmn].setMark(' ');
		}*/
		/*  for(int g=80;g<130;g++)
		{
		
		board[mini.fourInRowComb[g][0].line][mini.fourInRowComb[g][0].colmn].setMark('B');
		board[mini.fourInRowComb[g][1].line][mini.fourInRowComb[g][1].colmn].setMark('B');
		board[mini.fourInRowComb[g][2].line][mini.fourInRowComb[g][2].colmn].setMark('B');
		board[mini.fourInRowComb[g][3].line][mini.fourInRowComb[g][3].colmn].setMark('B');
		try{
			Thread.sleep(1000); // Sleep for sec
			}
			catch(InterruptedException e){}
		board[mini.fourInRowComb[g][0].line][mini.fourInRowComb[g][0].colmn].setMark(' ');
		board[mini.fourInRowComb[g][1].line][mini.fourInRowComb[g][1].colmn].setMark(' ');
		board[mini.fourInRowComb[g][2].line][mini.fourInRowComb[g][2].colmn].setMark(' ');
		board[mini.fourInRowComb[g][3].line][mini.fourInRowComb[g][3].colmn].setMark(' ');
		}
	  */
	  outputThread= new Thread(Penta.this);
	  outputThread.start();
  }
  
  public void run()
  {
	  int loca;//location
      currentMark='A';
      char tempMark=' ';
    
    while(!gameEnds)
    { 
    	if(currentMark=='B')
    	{
    		idField.setText("Computer player is playing \""+currentMark+"\"");
    		mini.artificialIntelligence('B',square, 1, -infinity, infinity, 3);
    			
    		loca=square.line*8+square.colmn;
    		
    		if(mini.UpdateLogicBoard(loca, 'B', l1, 0))
    			
    		{
    			System.out.println("comp "+loca/8+" "+loca%8);
    			while (l1.getTail() != null)
    				
                {
                  l2=l1.removeFirst();
                  board[l2.getRow()][l2.getCol()].setMark(l2.getMark());
                  
                }
    			tempMark=mini.Winner();
    			
    			if(tempMark=='B')
    			{
    				idField.setBackground(Color.green);
    				idField.setText("Computer player won!");
    				displayArea.setBackground(Color.green);
    			    displayArea.setText("Computer player won!");
    				gameEnds=true;
    			}
    			else
    			{
    				currentMark='A';
    			}
    		}
    	}
    	else
    	{
    		idField.setText("Human player is playing \""+currentMark+"\"");
    	}
    	try{Thread.sleep(100);}catch(Exception e){};
    	
    }
    if(currentMark=='A')
	{
    	idField.setBackground(Color.white);
    	idField.setText("Human player won!");
    	displayArea.setBackground(Color.white);
	    displayArea.setText("Human player won!");
	}
  }
  
  public class Square extends JPanel{
    private char mark;
    private int location;
    public Square(char squareMark, int squareLocation)
    {
      mark=squareMark;
      location=squareLocation;
      addMouseListener(
                        new MouseAdapter()
                        {
                          public void mouseReleased(MouseEvent e)
                          {
                        	  char winMark;
                              if(currentMark=='A')
                              {
	                        	  if(mini.UpdateLogicBoard(location, 'A', l1,0))
	                        		  
	                              {
	                        		while (l1.getTail() != null)
	                                {
	                        			System.out.println("me "+location/8+" "+location%8);
	                                  	l2=l1.removeFirst();
	                                    board[l2.getRow()][l2.getCol()].setMark(l2.getMark());
	                                   
	                                }
	                        		winMark=mini.Winner();
	                        		
	                      			if(winMark=='A')
	                      			{
	                      				gameEnds=true;
	                      			}
	                      			else
	                      			{
		                              currentMark='B';
	                      			}
	                              }
                              }
                          }
                        }
                       );
    }

  
    public Dimension getPreferredSize() 
    {
      return new Dimension(40, 40);
    }

    public Dimension getMinimumSize()
    {
      return getPreferredSize();
    }

    public void setMark(char newMark)
    {
      mark=newMark;
      repaint();
      try{
    	  if(currentMark=='A')
    		  audio=AudioSystem.getAudioInputStream(Penta.class.getResource("coindrop.wav"));
    	  else
    		  audio=AudioSystem.getAudioInputStream(Penta.class.getResource("coindrop2.wav"));
    	  //Get an AudioInputStream from a file
    	  buttonClickSound = AudioSystem.getClip();
    	  //Get a Clip object that can play the sound
    	  buttonClickSound.open(audio);
    	  //Load the audio input into the clip
      }catch(Exception excep){};
      buttonClickSound.start();
      
    }

    public int getSquareLocation()
    {
      return location;
    }

    public void paintComponent(Graphics g2)
    {
    	Graphics2D g=(Graphics2D)g2;//for rendering hints
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	super.paintComponent(g);//drawing the inherited JPanel before everything.
    	
    	Image image;
    	try
    	{
    		image = ImageIO.read(new File("bo4.jpg"));//src
    		g.drawImage(image, 0, 0, null);
        }catch(IOException ex){System.err.println("IOException");}

    	int col=this.location%8, row=this.location/8;
    	Color color=Color.cyan;
    	for(int i=0;i<6;i++)
    		color=color.darker();
    	g2.setColor(color);
    	g.setStroke(new BasicStroke(4));
    	int x=5,y=5;
    	if(row==0)//frame of board
    	{
	    	Line2D l=new Line2D.Float(new Point(0,0),new Point(39,0));
	    	g.draw(l);
	    	y++;
    	}
    	if(row==7)
    	{
	    	Line2D l=new Line2D.Float(new Point(0,39),new Point(39,39));
	    	g.draw(l);
	    	y--;
    	}
    	if(col==0)
    	{
	    	Line2D l=new Line2D.Float(new Point(0,0),new Point(0,39));
	    	g.draw(l);
	    	x++;
    	}
    	if(col==7)
    	{
	    	Line2D l=new Line2D.Float(new Point(39,0),new Point(39,39));
	    	g.draw(l);
	    	x--;
    	}
    	if(mark=='B')
    	{
    		try
        	{
    			image = ImageIO.read(new File("circle1.gif"));//src
    			g.drawImage(image, x,y, null);
    			//img,0,0,wid,hei,null
        	}catch(IOException ex){System.err.println("IOException");}
    	}
    	else if(mark=='A')
    	{
    		try
        	{
    			image = ImageIO.read(new File("circle2.gif"));//src
    			g.drawImage(image, x,y, null);
        	}catch(IOException ex){System.err.println("IOException");}
    	}
    	//and if mark==' ' do nothing.
    }
  }

}
