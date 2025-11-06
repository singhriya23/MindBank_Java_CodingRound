import java.util.*;

class Vertex{
    long id;
    Vertex(long id){
        this.id=id;
    }
}

class Edge{
    Vertex from;
    Vertex to;
    Edge(Vertex from, Vertex to){
        this.from=from;
        this.to=to;
    }
}

class dagPath{
    Map<Long,Integer> vertexCache= new HashMap<>();
    public int calculateLongestPath(Map<Vertex, List<Vertex>> dag, Vertex vertex){
        
        if(vertexCache.containsKey(vertex.id)){
          System.out.println("hitting cache " + vertex.id);
          return vertexCache.get(vertex.id);
        }
        int maxDistance = 0;
        for (Vertex neighbor : dag.getOrDefault(vertex, new ArrayList<>())) {
            maxDistance = Math.max(maxDistance, calculateLongestPath(dag, neighbor));
        }
        vertexCache.put(vertex.id, 1 + maxDistance);
        return  1 + maxDistance;

    }
}

public class dagCalc {
    public static void main(String[] args) {
        Vertex V1=new Vertex(1);
        Vertex V2=new Vertex(2);
        Vertex V3=new Vertex(3);
        Vertex V4=new Vertex(4);
        Vertex V5=new Vertex(5);
        Vertex V6=new Vertex(6);
        Vertex V7=new Vertex(7);

        List<Edge> edges=new ArrayList<> ();
        edges.add(new Edge(V1, V2));
        edges.add(new Edge(V1, V3));
        edges.add(new Edge(V1, V5));
        edges.add(new Edge(V3, V4));
        edges.add(new Edge(V5, V6));
        edges.add(new Edge(V6, V7));
        edges.add(new Edge(V2, V7));

        Map<Vertex, List<Vertex>> dag=new HashMap<>();

        for (Edge e:edges){
            dag.putIfAbsent(e.from, new ArrayList<>());
            dag.get(e.from).add(e.to);
        }
      
        dagPath aObj = new dagPath();
        int result = aObj.calculateLongestPath(dag, V1);
        System.out.println(result);
        System.out.println(aObj.vertexCache);
    }
    
}
