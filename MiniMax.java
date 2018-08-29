
public class MiniMax
{
	private byte board[][]; 
	private final long infinity = 10000000; 
	private final int maxFiveSeq = 96; 
	private final int maxFourSeq = 130; 
	protected RowCol fivePentaWinsSeq[][]; 
	protected RowCol fourPentaSeq[][];
	private int rowIndex=0, colIndex=0; 
	final long heuristicMat[][]={ {0,-4,-16,-64,-128,-10000000},
									{4,0,0,0,0,0},
									{16,0,0,0,0,0},
									{64,0,0,0,0,0},
									{128,0,0,0,0,0},
									{10000000,0,0,0,0,0}};
	private final int maximumMoves = 64; 
	protected long depthCounter=0;
	public MiniMax(byte board[][])
	{
		this.board=board;
		fivePentaWinsSeq=new RowCol[maxFiveSeq][5];
		fourPentaSeq=new RowCol[maxFourSeq][4];
		addMainDiag4();
		addSecDiag4();
		addRows4();
		addColumns4();
                rowIndex=0;
                colIndex=0;
		addMainDiag5();
		addSecDiag5();
		addRows5();
		addColumns5();
		
		initializeLogicBoard();
		System.out.println("ALL FIVE COMBINATIONS:");
		for(int i=0;i<maxFiveSeq;i++)
		{
			System.out.print(fivePentaWinsSeq[i][0].line+" "+fivePentaWinsSeq[i][0].colmn+"  ");
		    System.out.print(fivePentaWinsSeq[i][1].line+" "+fivePentaWinsSeq[i][1].colmn+"  ");
		    System.out.print(fivePentaWinsSeq[i][2].line+" "+fivePentaWinsSeq[i][2].colmn+"  ");
		    System.out.print(fivePentaWinsSeq[i][3].line+" "+fivePentaWinsSeq[i][3].colmn+"  ");
		    System.out.println(fivePentaWinsSeq[i][4].line+" "+fivePentaWinsSeq[i][4].colmn+"  ");
		}
		System.out.println("\nALL FOUR COMBINATIONS:");
		for(int i=0;i<maxFourSeq;i++)
		{
			System.out.print(fourPentaSeq[i][0].line+" "+fourPentaSeq[i][0].colmn+"  ");
		    System.out.print(fourPentaSeq[i][1].line+" "+fourPentaSeq[i][1].colmn+"  ");
		    System.out.print(fourPentaSeq[i][2].line+" "+fourPentaSeq[i][2].colmn+"  ");
		    System.out.println(fourPentaSeq[i][3].line+" "+fourPentaSeq[i][3].colmn+"  ");
		}
	}
	
	private char otherPlayer(char player)
	{
		if(player=='B')
			return 'A';
		else
			return 'B';
	}
	private long evaluate(char player)
	{
		long heuristic = 0; 
		for(int i=0; i<maxFiveSeq; i++)
		{
			int thisPlayer=0;
			int otherPlayer=0;
			for(int j=0;j<5; j++)
			{
				byte piece = board[fivePentaWinsSeq[i][j].line][fivePentaWinsSeq[i][j].colmn];
				if(piece==player)
					thisPlayer++;
				if(piece==otherPlayer(player))
					otherPlayer++;
			}
			heuristic += heuristicMat[thisPlayer][otherPlayer];
		}
		
		return heuristic;
	}
	
	//'B',square, 1, -infinity, infinity, c, 4)
	public long artificialIntelligence(char player, RowCol sqr, int MoveNumber, long alpha, long beta, int n)
	{
		depthCounter++;
		System.out.println("Depth counter: "+depthCounter);
		RowCol bestSqr = new RowCol(-1,-1);
		int moves = 0;
		int i;
        Linked L=new Linked(-1,-1,' ');
        Linked L2;

		Heuristic heuristics[]= new Heuristic[maximumMoves];
		for(i=maximumMoves-1; i>=0; i--)
		{
			if(UpdateLogicBoard(i,player,L,1))
			{
				long heuristic;
				int j;
				heuristic = evaluate(player);
				while (L.getTail() != null)
	            {
	            	L2=L.removeFirst();
	            	if(L2.getMark()==' ')
	            	{
	                   board[L2.getRow()][L2.getCol()] = 0;
	            	}
	            	else
	            	{
	            		board[L2.getRow()][L2.getCol()] =(byte)L2.getMark();
	            	}
	            }//******on exit the board restored. *******
				
				heuristics[moves]=new Heuristic();
				for(j=moves-1; j>=0 && heuristics[j].heuristic < heuristic; j--)
					
				{
					heuristics[j+1].heuristic=heuristics[j].heuristic;
					heuristics[j+1].square=heuristics[j].square;
				}//exit when j=-1
				heuristics[j+1].heuristic=heuristic;
				heuristics[j+1].square=new RowCol(i/8,i%8);
				moves++;
			}
		}
		//after the for we have heuristic array sorted from higher to lower heuristics.
		if(player=='A')
		{
			for(i=0;i<moves;i++)
			{
				heuristics[i].heuristic=-heuristics[i].heuristic;
			}
		}
		bestSqr.line  = heuristics[0].square.line;
		bestSqr.colmn = heuristics[0].square.colmn;
		
		for(i=0; i<moves; i++)
		{
			Linked L3=new Linked(-1,-1,' ');
			
			long score;
			RowCol tempHeuSqur = heuristics[i].square;
			char w;
            UpdateLogicBoard(tempHeuSqur.line*8+tempHeuSqur.colmn,player, L3,1);
			w=Winner();
			if(w=='B')
			{
				score = infinity;
			}
			else if(w=='A')
			{
				score = -infinity;
			}
			else if(w=='C')
			{
				score = 0; 
			}
			else if(n==0)
				
			{
				score = heuristics[i].heuristic;
			}
			else
			{
				score = artificialIntelligence(otherPlayer(player), sqr, MoveNumber + 1, alpha, beta, n-1);
				
			}
			while (L3.getTail() != null)//RESTORING THE BOARD
            {
            	L2=L3.removeFirst();
            	if(L2.getMark()==' ')
            	{
                   board[L2.getRow()][L2.getCol()] = 0;
            	}
            	else
            	{
            		board[L2.getRow()][L2.getCol()] =(byte)L2.getMark();
            	}
            }
                        
			if(player=='B')//
			{
				// MAX PLAYER
				if(score >= beta)//s >= infinity
				{
					//min player won't take
					sqr.line = tempHeuSqur.line;
					sqr.colmn = tempHeuSqur.colmn;
					//System.out.println("BETA PRUNING!!!!!!!!: beta is " + beta + " score is : " + score + " N===== " + n + " i === " + i);
					return score;
				}
				else if(score > alpha) // s > -infinity	
				{
					alpha = score;
					System.out.println("alpha is: " + alpha);
					bestSqr.line = tempHeuSqur.line;
					bestSqr.colmn = tempHeuSqur.colmn;
				}
			}
			else//
			{
				//MIN PLAYER
				if(score <= alpha) // s <= -infinity
				{
					//max player won't take
					sqr.line = tempHeuSqur.line;
					sqr.colmn = tempHeuSqur.colmn;
					//System.out.println("ALPHA PRUNING!!!!!!!!: alfa is " + alfa + " score is : " + score + " N===== " + n + " i === " + i);
					return score;
				}
				else if(score<beta) // s < infinity
				{
					beta = score;
					bestSqr.line = tempHeuSqur.line;
					bestSqr.colmn = tempHeuSqur.colmn;
				}
			}
		}
		sqr.line = bestSqr.line;
		sqr.colmn = bestSqr.colmn;

		if(player=='B')
			return alpha;
		else
			return beta;
	}
	//////////////////////////////////////////
	
	//////////////////////////////////

	public boolean UpdateLogicBoard(int location, char player, Linked l, int status)
	  {
	    boolean moveDone= false;
	    char playerMark='C';
	    if(!isOccupied(location))
	    {
	      board[location/8][location%8]=(byte)player;
	      char coin1,coin2,coin3,coin4;
	      
	      for( int i=0;i<maxFourSeq;i++)
		  {
	    	  coin1=(char)board[fourPentaSeq[i][0].line][fourPentaSeq[i][0].colmn];
		      coin2=(char)board[fourPentaSeq[i][1].line][fourPentaSeq[i][1].colmn];
		      coin3=(char)board[fourPentaSeq[i][2].line][fourPentaSeq[i][2].colmn];
		      coin4=(char)board[fourPentaSeq[i][3].line][fourPentaSeq[i][3].colmn];
		      playerMark = coin1;
			 if(playerMark == player &&
				otherPlayer(playerMark) == coin2 &&
				otherPlayer(playerMark) == coin3 &&
			    playerMark==coin4 &&
			    ((location/8==fourPentaSeq[i][0].line&&fourPentaSeq[i][0].colmn==location%8)||
			    (location/8==fourPentaSeq[i][3].line && fourPentaSeq[i][3].colmn==location%8)))
                         {
				             if(status==1)
				             {
	                             l.addNode(fourPentaSeq[i][1].line, fourPentaSeq[i][1].colmn, otherPlayer(playerMark));
	                             l.addNode(fourPentaSeq[i][2].line, fourPentaSeq[i][2].colmn, otherPlayer(playerMark));
	                             l.addNode(location/8, location%8, ' ');
				             }
				             else
				             {
				            	 l.addNode(fourPentaSeq[i][1].line, fourPentaSeq[i][1].colmn, ' ');
	                             l.addNode(fourPentaSeq[i][2].line, fourPentaSeq[i][2].colmn, ' ');
	                             l.addNode(location/8, location%8, player);
				             }
                             board[fourPentaSeq[i][1].line][fourPentaSeq[i][1].colmn] = 0;
                             board[fourPentaSeq[i][2].line][fourPentaSeq[i][2].colmn] = 0;
                             moveDone=true;
                         }
		  }
          if (!moveDone)
          {
        	  if(status==1)
        	  {
                  l.addNode(location/8, location%8, ' ');
        	  }
        	  else
        	  {
        		  l.addNode(location/8, location%8, player);
        	  }
              moveDone = true;
          }
	    }
        return moveDone;
	  }

	  public boolean isOccupied(int location)
	  {
	    if(board[location/8][location%8]=='B'||board[location/8][location%8]=='A')
	    {
	      return true;
	    }
	    else
	    {
	      return false;
	    }
	  }
	/////////////////////////////////////
	//////////////////////////////////

	private void initializeLogicBoard()
	{
		for(int i=0;i<64;i++)
		{
			board[i/8][i%8]=0;
		}
	}
	
	private void addMainDiag4()
	  {
	    int i, j, lineOffset, columnOffset;
	    
	    for(i=0;i<(8-4+1)*(8-4+1);i++)
	    {
	      lineOffset=i/5;
	      columnOffset=i%5;
	      colIndex=0;
	      for(j=0;j<16;j++)
	      {
	        if(j/4==j%4)
	        {
	        	fourPentaSeq[rowIndex][colIndex]=new RowCol(lineOffset+j/4,columnOffset+j%4);
	        	colIndex++;
	        }
	      }
	      rowIndex++;
	    }
	  }

	private void addSecDiag4()
	  {
	    
	    int i, j, lineOffset, columnOffset;
	    
	    for(i=0;i<(8-4+1)*(8-4+1);i++)
	    {
	      lineOffset=i/5;
	      columnOffset=i%5;
	      colIndex=0;
	      for(j=0;j<16;j++)
	      {
	        if(j/4+j%4==3)
	        {
	        	fourPentaSeq[rowIndex][colIndex]=new RowCol(lineOffset+j/4,columnOffset+j%4);
	        	colIndex++;
	        }
	      }
	      rowIndex++;
	    }
	  }
	          
	  private void addRows4()
	  {
	    int i, j, lineOffset=0, columnOffset=0;
	    
	    for(i=0;i<8*(8-4+1);i++){
	    	lineOffset=i/(8-4+1);
	    	columnOffset=i%(8-4+1);
	    	for(j=0;j<4;j++){
	    		fourPentaSeq[rowIndex][j]=new RowCol(lineOffset, columnOffset+j);
	    	}
	    	rowIndex++;
	    }
	  }
	  private void addColumns4()
	  {
	    int i, j, lineOffset, columnOffset;

	    for(i=0;i<8*(8-4+1);i++){
	    	lineOffset=i%(8-4+1);
	    	columnOffset=i/(8-4+1);
	    	for(j=0;j<4;j++){
	    		fourPentaSeq[rowIndex][j]=new RowCol(lineOffset+j, columnOffset);
	    	}
	    	rowIndex++;
	    }
	  }
	  ///////////////////////////////////////////////
	  
	   private void addMainDiag5()
	  {
	    int i, j, lineOffset, columnOffset;
	    
	    for(i=0;i<(8-5+1)*(8-5+1);i++)
	    {
	      lineOffset=i/4;
	      columnOffset=i%4;
	      colIndex=0;
	      for(j=0;j<25;j++)
	      {
	        if(j/5==j%5)
	        {
	        	fivePentaWinsSeq[rowIndex][colIndex]=new RowCol(lineOffset+j/5,columnOffset+j%5);
	        	colIndex++;
	        }
	      }
	      rowIndex++;
	    }
	  }

	private void addSecDiag5()
	  {
	    
	    int i, j, lineOffset, columnOffset;
	    
	    for(i=0;i<(8-5+1)*(8-5+1);i++)
	    {
	      lineOffset=i/4;
	      columnOffset=i%4;
	      colIndex=0;
	      for(j=0;j<25;j++)
	      {
	        if(j/5+j%5==4)
	        {
	        	fivePentaWinsSeq[rowIndex][colIndex]=new RowCol(lineOffset+j/5,columnOffset+j%5);
	        	colIndex++;
	        }
	      }
	      rowIndex++;
	    }
	  }

	  private void addRows5()
	  {

	    int i, j, lineOffset=0, columnOffset=0;
	    
	    for(i=0;i<8*(8-5+1);i++){
	    	lineOffset=i/(8-5+1);
	    	columnOffset=i%(8-5+1);
	    	for(j=0;j<5;j++)
	    	{
	    		fivePentaWinsSeq[rowIndex][j]=new RowCol(lineOffset, columnOffset+j);
	    	}
	    	rowIndex++;
	    }
	  }
	   
	  private void addColumns5()
	  {
	    int i, j, lineOffset, columnOffset;

	    for(i=0;i<8*(8-5+1);i++){
	    	lineOffset=i%(8-5+1);
	    	columnOffset=i/(8-5+1);
	    	for(j=0;j<5;j++){
	    		fivePentaWinsSeq[rowIndex][j]=new RowCol(lineOffset+j, columnOffset);
	    	}
	    	rowIndex++;
	    }
	  }
	   
	  ///////////////////////////////////////////////
	  public char Winner() 
	  {
		  int i;
		  byte playerMark;
		  for(i=0;i<maxFiveSeq;i++)
		  {
			 playerMark = board[fivePentaWinsSeq[i][0].line][fivePentaWinsSeq[i][0].colmn];
			 if(playerMark!=0&&
				playerMark==board[fivePentaWinsSeq[i][1].line][fivePentaWinsSeq[i][1].colmn]&&
			    playerMark==board[fivePentaWinsSeq[i][2].line][fivePentaWinsSeq[i][2].colmn]&&
			    playerMark==board[fivePentaWinsSeq[i][3].line][fivePentaWinsSeq[i][3].colmn]&&
			    playerMark==board[fivePentaWinsSeq[i][4].line][fivePentaWinsSeq[i][4].colmn])
			    {
				 return (playerMark=='B')?'B':'A';
				}
		   }
		   for(i=0;i<8*8;i++)
		   {
			   if(board[i/8][i%8]==0)
			   {
				   return ' ';
			   }
		   }
		   return 'C';
	  }
}
class RowCol
{
	
	public int line;
	public int colmn;
	public RowCol(int line, int colmn)
	{
		this.line=line;
		this.colmn=colmn;
	}	
}
class Heuristic
{
	public RowCol square;
	
	public long heuristic;
	
}

