package main.gala.ai;

import java.util.ArrayList;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;


import main.gala.chart.Chart;
import main.gala.common.Direction;
import main.gala.enums.GameState;



/* przeszukiwanie DFS dla sztucznej inteligencji
 * 
 * alfabeta
 * 
 * ocena: zblizanie sie do bramki, duze punkty za stany koncowe, preferowanie krotkich sekwencji*/
public class ABPlayer implements IArtificialIntelligence {

	
	/*stale oceny*/
	

	public static int noGrade = -10000000, //najnizsza, cokowliek jest lepsze, wyusza zrobienie czegokolwiek
			selfBlockGrade = -100000, //samozablokowanie
			futureSelfBlock = -5000, //samozablokowanie w przyszlosci
			enemyGoalGrade = -100000, //przeciwnik zdobywa gola
			firstMoveGrade = 100, //promujemy krotsze sekwencje
			nextMoveGrade = -3,
			GoalGrade = 1000000, // zwycieztwo
			EnemyBlockGrade = 1000000, //przeciwnik zablokowany
			futureGoalGrade = 50000, //pewna bramka w nastepnym ruchu
			WIN = futureGoalGrade,
			LOSE = -WIN;
			


	public List<Integer> indexList;
	public static int DIRECTIONS = 8;
    protected Set<Integer> edges = new HashSet<>();
    
    protected boolean[] visited;
    protected int[] xdir, ydir;
    protected int WIDTH, HEIGHT;
	public ABPlayer()
	{
		indexList = new ArrayList<Integer>() {
            {
                for (int i = 0; i < DIRECTIONS; ++i)
                    add(i);
            }};
	}
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
	  public class pair
	  {
		  public int grade;
		  public LinkedList<Direction> seq;
		  public pair(int g, LinkedList<Direction> s)
		  {
			  grade = g;
			  seq = s;
		  }
	  };
	  
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
            		  visited[bp] = false;            		  edges.add(edgeHash);
            		  seq.removeLast();
            		  return new pair(tempGrade, new LinkedList(seq));
            	  }
            	  else
            	  {
            		  pair p = new pair(0, new LinkedList<Direction>());
            		  p = alphaBeta(nextPosition, new LinkedList<Direction>(seq));
            		  if(p.grade >= grade)
            		  {
            			  grade = p.grade;
            			  best = new LinkedList<Direction>(p.seq);
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
	  
	  //to na lepszy sprzet, telefony za wolne:
/*	  private int alphaBetaOpponent(int ballPosition, LinkedList<Direction> seq)
	  {
		  Direction direction;
		  int nextPosition, edgeHash;

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
            		  tempGrade -= nextMoveGrade;// * seq.size();
            		  System.out.println(tempGrade);
            		  visited[bp] = false;
            		  System.out.println(visited[bp]);
            		  edges.add(edgeHash);
            		  seq.removeLast();
            		  return EnemyGrader(bp);
            	  }
            	  else
            		  tempGrade = alphaBetaOpponent(bp, new LinkedList<Direction>(seq));
            	  edges.add(edgeHash);
            	  seq.removeLast();
              }
		  }
		  return tempGrade;
	  }*/

	  protected int SelfGrader(int ballPosition)
	  {
		  int sol = 0;
		  if(ballPosition < (WIDTH + 1)) sol += GoalGrade;
		  if(ballPosition > (WIDTH + 1) * (HEIGHT + 2)) sol += enemyGoalGrade;
		  int x, y = 0;
		  for(int i = 0; i < 8; i++)
		  {
			  if(edges.contains(computeHash(ballPosition, computeNext(ballPosition, new Direction(xdir[i], ydir[i])))))
					  y++;
		  }
		  if(y == 0) sol += selfBlockGrade;
		  sol += PositionGrade(ballPosition);
		  return sol;
		  
	  }
	/*  private int EnemyGrader(int ballPosition)
	  {
		  int sol = 0;
		  return sol;
	  }
	  */
	  private int PositionGrade(int ballPosition)
	  {
		  int x = ballPosition % (WIDTH + 1);
		  int y = ballPosition / (WIDTH + 1);
		  x = (WIDTH / 2) + 1 - x;
		  y = HEIGHT + 1 - y;
		  int sol = y * y;
		  if(y > (HEIGHT + 5 / 3))
				  sol += (x * x);
		  return sol;
	  }
	    public IArtificialIntelligence getInstance() {
	        return new ABPlayer();
	    }
	    protected int computeNext(int ballPosition, final Direction direction) {
	        return ballPosition + direction.getX() + (WIDTH + 1) * direction.getY();
	    }
	    protected int computeHash(final int start, final int next) {
	        return Math.max(start, next) * Chart.MULTIPLIER + Math.min(start, next);
	    }
	    
}
