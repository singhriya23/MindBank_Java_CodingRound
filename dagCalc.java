import java.util.*;

class Vertex{
    long id;
    Vertex(long id){
        this.id=id;
    }

    @Override
   public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Vertex other = (Vertex) o;
    return id == other.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
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

class dagPath {
    Map<Long, Integer> vertexCache = new HashMap<>();

    public int calculateLongestPath(Map<Vertex, List<Vertex>> dag, Vertex vertex) {
        if (dag == null || vertex == null) return 0;

        Set<Long> visited = new HashSet<>();
        Set<Long> recStack = new HashSet<>();

        if (hasCycle(dag, vertex, visited, recStack)) {
            System.out.println("Cycle detected! Not a DAG.");
            return -1; // 🟩 return early if cycle found
        }

        return dfs(dag, vertex);
    }

    private boolean hasCycle(Map<Vertex, List<Vertex>> dag, Vertex v,
                             Set<Long> visited, Set<Long> recStack) {
        if (recStack.contains(v.id)) return true;  // found back-edge → cycle
        if (visited.contains(v.id)) return false;  // already processed safely

        visited.add(v.id);
        recStack.add(v.id);

        for (Vertex neighbor : dag.getOrDefault(v, new ArrayList<>())) {
            if (hasCycle(dag, neighbor, visited, recStack)) return true;
        }

        recStack.remove(v.id);
        return false;
    }

    private int dfs(Map<Vertex, List<Vertex>> dag, Vertex vertex) {
        if (vertexCache.containsKey(vertex.id)) {
            return vertexCache.get(vertex.id);
        }

        int maxDistance = 0;
        for (Vertex neighbor : dag.getOrDefault(vertex, new ArrayList<>())) {
            maxDistance = Math.max(maxDistance, dfs(dag, neighbor));
        }

        vertexCache.put(vertex.id, 1 + maxDistance);
        return 1 + maxDistance;
    }
}


public class dagCalc {
    public static void main(String[] args) {
        testDAG();
        testEmptyDAG();
        testDisconnectedGraph();
        testNullInput();
        testingVertexByID();
        testCyclicDAG();
    }
    public static void testDAG() {
        Vertex V1 = new Vertex(1);
        Vertex V2 = new Vertex(2);
        Vertex V3 = new Vertex(3);
        Vertex V4 = new Vertex(4);
        Vertex V5 = new Vertex(5);
        Vertex V6 = new Vertex(6);
        Vertex V7 = new Vertex(7);

        List<Edge> edges = Arrays.asList(
            new Edge(V1, V2),
            new Edge(V1, V3),
            new Edge(V1, V5),
            new Edge(V3, V4),
            new Edge(V5, V6),
            new Edge(V6, V7),
            new Edge(V2, V7)
        );

        Map<Vertex, List<Vertex>> dag = new HashMap<>();
        for (Edge e : edges) {
            dag.putIfAbsent(e.from, new ArrayList<>());
            dag.get(e.from).add(e.to);
        }

        dagPath obj = new dagPath();
        int result = obj.calculateLongestPath(dag, V1);
        int expectedResult = 4; 
        System.out.println("Testing longest path DAG: " + (result == expectedResult ? "PASSED" : "FAILED") + " | Result: " + result);
    }
    public static void testEmptyDAG() {
        dagPath obj1 = new dagPath();
        int result = obj1.calculateLongestPath(new HashMap<>(), new Vertex(1));
        int expectedResult = 1; 
        System.out.println("Testing an empty DAG: " + (result == expectedResult ? "PASSED" : "FAILED") + " | Result: " + result);
    }
    public static void testDisconnectedGraph() {
        Vertex V1 = new Vertex(1);
        Vertex V2 = new Vertex(2);
        Map<Vertex, List<Vertex>> dag = new HashMap<>();
        dag.put(V1, new ArrayList<>()); 
        dag.put(V2, new ArrayList<>()); 
        dagPath obj2 = new dagPath();
        int result = obj2.calculateLongestPath(dag, V1);
        int expected = 1;
        System.out.println("Testing a disconnected graph: " + (result == expected ? "PASSED" : "FAILED") + " | Result: " + result);
    }

    public static void testNullInput() {
    dagPath obj = new dagPath();
    int result = obj.calculateLongestPath(null, null);
    int expected = 0;
    System.out.println("Test Null Input Safe: " + (result == expected ? "PASSED" : "FAILED") + " | Result: " + result);
    }

    public static void testingVertexByID() {
        Vertex v1 = new Vertex(10);
        Vertex v2 = new Vertex(10);
        Vertex v3 = new Vertex(11);

        boolean equalsPass = v1.equals(v2) && !v1.equals(v3);
        boolean hashPass = v1.hashCode() == v2.hashCode() && v1.hashCode() != v3.hashCode();

        System.out.println("Testing Vertex equals() & hashCode(): " +
            (equalsPass && hashPass ? "PASSED" : "FAILED"));
    }
    
    public static void testCyclicDAG() {
        Vertex V1 = new Vertex(1);
        Vertex V2 = new Vertex(2);
        Vertex V3 = new Vertex(3);

        List<Edge> edges = Arrays.asList(
            new Edge(V1, V2),
            new Edge(V2, V3),
            new Edge(V3, V1) 
        );

        Map<Vertex, List<Vertex>> graph = new HashMap<>();
        for (Edge e : edges) {
            graph.putIfAbsent(e.from, new ArrayList<>());
            graph.putIfAbsent(e.to, new ArrayList<>());
            graph.get(e.from).add(e.to);
        }

        dagPath obj = new dagPath();
        int result = obj.calculateLongestPath(graph, V1);
        System.out.println("Testing cycle detection: " + (result == -1 ? "PASSED" : "FAILED") + " | Result: " + result);
    }

}
