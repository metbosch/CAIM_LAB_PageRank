import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

public class PageRank {

    class Edge {
        int dest;     // destination airport
        int weight;   // number of routes in this edge
    }

    class EdgeList {
        int weight;    // total number of edges = sum of second components of list
        ArrayList<Edge> list;
        HashMap<Integer, Integer> listAux;
    }

    static String airportCodes[];           // index to short code
    static String airportNames[];           // index to airport name
    static HashMap<String,Integer> airportIndices;  // airport code to index
    static EdgeList[] G;             // G[i] is a list of pairs (j,k) meaning
                                     // "there are k routes from airport i to airport j"
    //....                             // other info??

    public static void readAirports() {
      try {
         String fileName = "airports.txt";
         System.out.println("... opening file "+fileName);
         FileInputStream fstream = new FileInputStream(fileName);
         DataInputStream in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));

         String strLine;
         int index = 0;
         ArrayList<String> codeTemp = new ArrayList<String>();
         ArrayList<String> nameTemp = new ArrayList<String>();
         while ((strLine = br.readLine()) != null) {
               String[] aLine = strLine.split(",");
               String airportCode = aLine[4];
               String airportName = aLine[1]+" ("+aLine[3]+")";
               if (airportCode.length() > 2) {
                   codeTemp.add(airportCode);
                   nameTemp.add(airportName);
                   airportIndices.put(airportCode,index); //MARC
                   index++;
                }

         }
         
         airportCodes[] = new String[index];
         airportNames[] = new String[index];
         G[] = new EdgeList[index];
         
         for (int i = 0; i < index; i++) {
            airportCodes[i] = codeTemp.get(i);
            airportNames[] = nameTemp.get(i);
         }
         // TO DO: DUMP STUFF TO airportCodes, airportNames, airportIndices

         System.out.println("... "+index+" airports read");

         in.close();

       } catch (Exception e){
		     //Catch exception if any
             System.err.println("Error: " + e.getMessage());
             // return null;
       }

    }


   public static void readRoutes() {
      try {
         String fileName = "routes.txt";
         System.out.println("... opening file "+fileName);
         FileInputStream fstream = new FileInputStream(fileName);
         DataInputStream in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));

         String strLine;
         int index = 0;
         while ((strLine = br.readLine()) != null) {
               String[] aLine = strLine.split(",");
               Integer fromAirport = airportIndices.get(aLine[2]);
               Integer toAirport = airportIndices.get(aLine[4]);

               G[fromAirport].weight++;
               Integer cont = G[fromAirport].listAux.get(toAirport);
               if (cont == null) {
                  G[fromAirport].listAux.put(toAirport, new Integer(1));
               } else {
                  ++cont;
               }
         }

         // Transform the HashMap of each edge into a ArrayList
         // Maybe this can be done in the first iteration fo pageRank Algorithm
         for (int i = 0; i < G.lenght; ++i) {
               Iterator< Map.Entry<Integer, Integer> > it = G[i].listAux.entrySet().iterator();
               while (it.hasNext()) {
                     Map.Entry<Integer, Integer> elem = it.next();
                     Edge e = new Edge();
                     e.dest = elem.getKey();
                     e.weight = elem.getValue();
                     G[i].list.add(e);
               }
               G[i].listAux.clear();
         }

         System.out.println("... " + index + " routes read");

         in.close();

       } catch (Exception e){
         //Catch exception if any
             System.err.println("Error: " + e.getMessage());
             // return null;
       }
   }

   public static computePageRanks() {
      ....
   }

   public static outputPageRanks() {
      ...
   }

   public static void main(String args[])  {

       readAirports();   // get airport names, codes, and assign indices
       readRoutes();     // read tuples and build graph
       computePageRanks();
       outputPageRanks();

    }

}
