package main.gala.ai;

import java.util.LinkedList;
import java.util.Random;

import main.gala.ai.ABPlayer.pair;
import main.gala.common.Direction;


/*uposledzamy gracza trudnego, oprócz oceny liczy się pewna losowość */


public class ABRandomPlayer extends ABPlayer {
	  private pair alphaBeta(int ballPosition, LinkedList<Direction> seq)
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
		  {//if(x==0)System.out.println(index);
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
          		  int oponent = LOSE;
          //		  System.out.println(tempGrade);
          		  visited[bp] = false;
          //		  System.out.println(visited[bp]);
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
          			  double r = Math.random() * 10;
          			  if(r > 8 || grade == noGrade)
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
		  {System.out.println("X");
			  visited[bp] = false;
		  }
		  return new pair(grade, new LinkedList<Direction> (best));
	  }

}
