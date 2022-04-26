public class MinimaxPlayer implements Player {
	int id; 
	int opponent_id;
    int cols; 
    
    @Override
    public String name() {
        return "Minnie";
    }

    @Override
    public void init(int id, int msecPerMove, int rows, int cols) {
    	this.id = id; //id is your player's id, opponent's id is 3-id
    	this.cols = cols;
    	opponent_id = 3-id;
    }

    @Override
    public void calcMove(
        Connect4Board board, int oppMoveCol, Arbitrator arb) 
        throws TimeUpException {
        
        if (board.isFull()) {
            throw new Error ("Complaint: The board is full!");
        }
        
        int move = 0; 
        int maxDepth = 1;
        
        while(!arb.isTimeUp() && maxDepth <= board.numEmptyCells()) {
        	// do minimax search
        	// start the first level of minimax, set move as you're finding the bestScore
			// first level is maximizing so next level is minimizing (isMaximizing = FALSE)
        	
			int topScore = -1000;
			int bestScore = 0; //This will change right away from call to minimax
			//for all possible first moves, find the best one based on minimax as depth increases and time allowed
			for(int col = 0; col < (board.numCols() - 1); col++)
			{
				if(!board.isValidMove(col))
        	    {
        		    continue;
        	    }
				board.move(col, id);
                bestScore = minimax(board, maxDepth-1, false, arb);
				if(bestScore > topScore)
				{
                    topScore = bestScore;
					arb.setMove(move);
                }					
		        board.unmove(col, id);
            } 
            maxDepth++; //crank up another level while there is still time and moves left
        }        

    }
    
    public int minimax(Connect4Board board, int depth, boolean isMaximizing, Arbitrator arb) {
    	int bestScore = 0;
    	
    	if (depth == 0 || board.numEmptyCells() == 0 || arb.isTimeUp()) {
    		return score(board);
    	}
 	
		if (isMaximizing)
		{
			bestScore = -1000;
			for(int col = 0; col < (board.numCols() - 1); col++)
			{
				if(!board.isValidMove(col))
        	    {
        		    continue;
        	    }
				board.move(col, id);
				bestScore = Math.max(bestScore, minimax(board, depth - 1, false, arb)); 
				board.unmove(col, id);
			}
			return bestScore;
		}

		else /* minimizing player */ 
		{
			bestScore = 1000;
			for(int col = 0; col < (board.numCols() - 1); col++)
			{
				if(!board.isValidMove(col))
        	    {
        		    continue;
        	    }
				board.move(col, id);   	
				bestScore = Math.min(bestScore, minimax(board, depth - 1, true, arb)); 
				board.unmove(col, id);
			}
			return bestScore;	
		}
    	
    }
    
    // your score - opponent's score
    public int score(Connect4Board board) {
    	return calcScore(board, id) - calcScore(board, opponent_id);
    }
    
    
	// Return the number of connect-4s that player #id has.
	public int calcScore(Connect4Board board, int id)
	{
		final int rows = board.numRows();
		final int cols = board.numCols();
		int score = 0;
		// Look for horizontal connect-4s.
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c <= cols - 4; c++) {
				if (board.get(r, c + 0) != id) continue;
				if (board.get(r, c + 1) != id) continue;
				if (board.get(r, c + 2) != id) continue;
				if (board.get(r, c + 3) != id) continue;
				score++;
			}
		}
		// Look for vertical connect-4s.
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c) != id) continue;
				if (board.get(r + 1, c) != id) continue;
				if (board.get(r + 2, c) != id) continue;
				if (board.get(r + 3, c) != id) continue;
				score++;
			}
		}
		// Look for diagonal connect-4s.
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = 0; r <= rows - 4; r++) {
				if (board.get(r + 0, c + 0) != id) continue;
				if (board.get(r + 1, c + 1) != id) continue;
				if (board.get(r + 2, c + 2) != id) continue;
				if (board.get(r + 3, c + 3) != id) continue;
				score++;
			}
		}
		for (int c = 0; c <= cols - 4; c++) {
			for (int r = rows - 1; r >= 4 - 1; r--) {
				if (board.get(r - 0, c + 0) != id) continue;
				if (board.get(r - 1, c + 1) != id) continue;
				if (board.get(r - 2, c + 2) != id) continue;
				if (board.get(r - 3, c + 3) != id) continue;
				score++;
			}
		}
		return score;
	}

}