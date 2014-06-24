package main.gala.ai;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.gala.ai.ABPlayer.pair;
import main.gala.chart.Chart;
import main.gala.common.Direction;


/*uposledzamy gracza trudnego, oprócz oceny liczy się pewna losowość */


public class ABRandomPlayer extends ABPlayer {
	
	  public List<Direction> executeMoveSequence(final Chart chart) {
	        List<Direction> moveSequence;
	        
	        /*zapisujemy potrzebne fanty */
	        
	        xdir = chart.X_COORDS;
	        ydir = chart.Y_COORDS;
	        edges  = chart.getEdges();
	        visited = chart.getVisited();
	        HEIGHT = chart.getHeight();
	        WIDTH = chart.getWidth();
	        int ballPosition = chart.getballPosition();
	        Random r = new Random();
	        Collections.shuffle(indexList, r);
	        moveSequence = this.alphaBeta(ballPosition, new LinkedList<Direction>()).seq;
	        return moveSequence;

	    }
	  protected pair alphaBeta(int ballPosition, LinkedList<Direction> seq)
	  {
		  Direction direction;
		  int nextPosition, edgeHash;
		  LinkedList<Direction> best = new LinkedList<Direction>();
		  int grade = noGrade;
		  int tempGrade = 0;
		  int bp = ballPosition;
		  boolean vis = visited[bp];
		  visited[bp] = true;
		 
		   for(int index : indexList)
		  {
			  direction = new Direction(xdir[index], ydir[index]);
            nextPosition = computeNext(bp, direction);
            edgeHash = computeHash(bp, nextPosition);
            if(edges.contains(edgeHash))
            {
          	  edges.remove(edgeHash);
          	 seq.addLast(direction);
          	  if(!vis)
          	  {
          		  tempGrade = SelfGrader(bp);
          		  tempGrade += firstMoveGrade;
          		  tempGrade -= nextMoveGrade * (seq.size() - 1);
          		  visited[bp] = false;
          		  edges.add(edgeHash);
          		  seq.removeLast();
          		  return new pair(tempGrade, new LinkedList(seq));
          	  }
          	  else
          	  {
          		  pair p = new pair(0, new LinkedList<Direction>());
          		  p = alphaBeta(nextPosition, new LinkedList<Direction>(seq));
          		  if(p.grade >= grade)
          		  {
          			  double r = Math.random() * 100;
          			  if(r > 25 || grade == noGrade || seq.size() == 1)
          			  {
          				  grade = p.grade;
          				  best = new LinkedList<Direction>(p.seq);
          			  }
          		  }
          	  }
          	  edges.add(edgeHash);
          	  seq.removeLast();
            }
		  }
		  if(!vis)
		  {
			  visited[bp] = false;
		  }
		  return new pair(grade, new LinkedList<Direction> (best));
	  }
	    public IArtificialIntelligence getInstance() {
	        return new ABRandomPlayer();
	    }

}
