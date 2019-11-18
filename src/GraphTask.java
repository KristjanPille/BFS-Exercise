import java.util.*;

/** Container class to different classes, that makes the whole
 * set of classes one class formally.
 */
public class GraphTask {

   /** Main method. */
   public static void main (String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   private int V;
   /** Actual main method to run examples and everything. */
   public void run() {
      Graph g = new Graph ("G");
      g.createRandomSimpleGraph (0, 0);
      g.Graphsolution(g);

      // TODO!!! Your experiments here
   }

   // TODO!!! add javadoc relevant to your problem
   class Vertex {

      private String id;
      private Vertex next;//tipp
      private Arc first;//kaar
      private int info = 0;

      // You can add more fields, if needed

      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

      // TODO!!! Your Vertex methods here!
   }


   /** Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;
      private int info = 0;
      // You can add more fields, if needed

      Arc (String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

      // TODO!!! Your Arc methods here!
   } 


   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;

      Graph (String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph (String s) {
         this (s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty ("line.separator");
         StringBuffer sb = new StringBuffer (nl);
         sb.append (id);
         sb.append (nl);
         Vertex v = first;
         while (v != null) {
            sb.append (v.toString());
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append (" ");
               sb.append (a.toString());
               sb.append (" (");
               sb.append (v.toString());
               sb.append ("->");
               sb.append (a.target.toString());
               sb.append (")");
               a = a.next;
            }
            sb.append (nl);
            v = v.next;
         }
         return sb.toString();
      }

      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         V++;
         return res;
      }

      public Arc createArc (String aid, Vertex from, Vertex to) {
         Arc res = new Arc (aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * @param n number of vertices added to this graph
       */
      public void createRandomTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + String.valueOf(n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               createArc ("a" + varray [vnr].toString() + "_"
                  + varray [i].toString(), varray [vnr], varray [i]);
               createArc ("a" + varray [i].toString() + "_"
                  + varray [vnr].toString(), varray [i], varray [vnr]);
            } else {}
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int [info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res [i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraph (int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException 
               ("Impossible number of edges: " + m);
         first = null;
         createRandomTree (n);       // n-1 edges created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j) 
               continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0) 
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected [j][i] = 1;
            edgeCount--;  // a new edge happily created
         }
      }
      /**
       * Meetod on graafi laiuti läbimise algoritm, mis kasutab maatriksit
       * tippude läbimiseks. Ei kasuta kaari!!!
       * Tagastusväärtus: List tippudest mis on külastatud, külastamise järjekorras.
       * Juhul, kui graph on võrdne null, visatakse error.
       * createRandomSimpleGraph meetodis on muud vea juhud käsitletud.
       */
      public List<String> Graphsolution(Graph graph){
         if (graph instanceof Graph && graph.first != null && graph.first.next != null){
            List<String> visited = new ArrayList<>();
            for(Map.Entry<String, List<String>> entry : vertexMap(graph).entrySet()) {
               String key = entry.getKey();
               List<String> value = entry.getValue();
               if (!visited.contains(key)){
                  visited.add(key);
               }
               for (String s: value) {
                  if (!visited.contains(s)){
                     visited.add(s);
                  }
               }
            }
            return visited;
         }
         throw new RuntimeException(graph + " is not a valid graph!");
      }
      /**
       * Meetod mis võtab sisendiks graafi ning tagastab
       * sõnastiku graafi tippudest, kasutades createAdjMatrix
       * meetodit.
       */
      public Map<String, List<String>> vertexMap(Graph graph){
         Map<String, List<String>> vertexes = new HashMap<>();
         int vertexid = 1;
         int ver = 1;
         for (int[] v: graph.createAdjMatrix()) {
            List<String> vertex = new ArrayList<>();
            for (int s: v) {
               if (s == 1){
                  vertex.add("v" + ver);
               }
               ver++;
            }
            vertexes.put("v" + vertexid, vertex);
            vertexid++;
            ver =1;
         }
         return vertexes;
      }
   }
}