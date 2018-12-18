package FinalProject;
// Lucy Chen (YIC76) Jordan Widjaja(JOW105)
import net.datastructures.Entry;
import net.datastructures.HeapAdaptablePriorityQueue;
import java.util.*;


class Pair
{
    int destination;
    int weight;
    
    Pair(int d, int w)
    {
        destination = d;
        weight = w;
    }
    
    int getDestination () 
    {
        return destination;
    }
    int getWeight () {
        return weight;
    }
}

class Graph
{
    int num_vertices;
    int num_edges;
    ArrayList<Pair> adjList [];

    Graph(int V)
    {
        num_vertices = V;
        num_edges = 0;
        
        //store graph as adjacency list
        adjList = new ArrayList[V];
        
        for(int i = 0; i < V; i++)
        {
            adjList[i] = new ArrayList<>();
        }
    }
    
    int numEdges() {
        return num_edges;
    }
    
    int numVertices() {
        return num_vertices;
    }
    
    void addEdge(int src, int dest, int weight)
    {
        Pair to_add = new Pair(dest,weight);
        adjList[src].add(to_add);
        
        Pair to_add2 = new Pair(src,weight);
        adjList[dest].add(to_add2);
        num_edges++;
    }
//returns list of all adjacent pairs and edge weight    
    ListIterator getAdjVertex(int src) {
        ListIterator adjVertex = adjList[src].listIterator();
        return adjVertex;
    }
    
    //print graph in adjlist
    void printGraph()
    {
        for(int i = 0; i < num_vertices; i++)
        {
            System.out.println("From vertex " + i);
            for(Pair p: adjList[i])
            {  
                System.out.println("adjacent node:" + p.getDestination() + " weight:" + p.getWeight() + "->");
            }
            System.out.println();
        }
    }
}
public class FinalProject
{
    public static void main(String[] args)
    {
        //set number of vertices; to be used for testing runtime**
        int num_vertices = 10; //if vertices > 30, please comment out 161-169 so it doesn't take forever to print distance list
        
        Random r = new Random(1); //to randomize weight of edges
        
        //create complete graph
        Graph G = new Graph(num_vertices);
        for (int i=0; i<num_vertices-1;i++){
            for (int j=i+1; j<num_vertices; j++)
            G.addEdge(i,j,r.nextInt(10)+1);
        }
        
//print graph in list order
//       G.printGraph();
        System.out.println("Total number of edges=" +G.numEdges());
      
        //set source
        int src = 0;
        int vertices = G.numVertices();
        
        //Using HeapAdaptablePriorityQueue to store mininum distance from soruce as key and prior node as value
        HeapAdaptablePriorityQueue<Integer,Integer> Queue= new HeapAdaptablePriorityQueue();
        
     
        ArrayList <Entry<Integer,Integer>> entries = new ArrayList<>(); 
        //keep track of node and their entries; set initial distance of all nodes to infinity and src to 0
        for (int i=0; i<vertices; i++){
            if (i == src) {
                entries.add(i,Queue.insert(0,i));
            }
            else {
                entries.add(i,Queue.insert(Integer.MAX_VALUE,i));   
            }
        }
       
        //keep track of all nodes that have not been visited yet
        ArrayList<Integer> notVisited = new ArrayList<>(); 
        for (int i =0; i < vertices; i++) {
            notVisited.add(i);
        }
        
        long startTime = System.currentTimeMillis();
     
//Dijkstras Algorithm starts here
        while(!Queue.isEmpty()) {
            
            Entry<Integer, Integer> vertex = Queue.removeMin();    
            int u = entries.indexOf(vertex);
            ListIterator adjVertex = G.getAdjVertex (u); 
            
            while(adjVertex.hasNext()){
                
                Pair dest = (Pair)adjVertex.next(); 
                int v = dest.getDestination();
                
                if(notVisited.contains(v)) {
               
                    Entry<Integer,Integer> adj = entries.get(v);

                    if ((vertex.getKey() + dest.getWeight()) < adj.getKey()){
                        Queue.replaceKey(adj, vertex.getKey()+dest.getWeight());
                        Queue.replaceValue(adj,u);
                    }
                }
            }
            
            notVisited.remove(Integer.valueOf(u)); //remove visited node from not visited
        }
//Dijskstras Algorithm ends here

        long endTime = System.currentTimeMillis();
        System.out.println("RunTime: " + ((endTime-startTime)));
        
//Start Print out shortest distance and path;

        for(int i= 0; i < entries.size(); i++) {
        System.out.print("From" + src +""+ " To node "+i + " Shortest Distance = "+ entries.get(i).getKey());
        System.out.print(" Path:");
        printpath(entries, i, src);
        System.out.println();
        }
//End print
    }
  //back track all the parent node until the src is reached
  static void printpath(ArrayList <Entry<Integer,Integer>> entries, int i, int src) {
     
      if(i!=src){
      printpath(entries,entries.get(i).getValue(), src);
      }
      System.out.print(i+">");
  }
}


