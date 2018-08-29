public class Linked { 
	
    private int col,row;
   
	private Linked _tail;
	
    private char mark;
    
    
	public Linked(int row, int col, char mark)
	{
			this.mark = mark;
			this.row = row;
			this.col = col;
			_tail = null;		
	}
	
	public void addNode(int row,int col, char mark)
	{
		if(_tail == null)
		{
			_tail = new Linked(row,col,mark);
		}
		else
		{
			_tail.addNode(row, col, mark);
		}
	}
	
	public Linked removeFirst()
	{
		Linked temp;
		if(_tail == null)
		{
			return null;
		}
		temp=_tail;
		_tail=_tail.getTail();
		return temp;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return col;
	}
        
    public char getMark()
    {
            return mark;
    }
    public Linked getTail()
    {
    	return _tail;
    }

}
