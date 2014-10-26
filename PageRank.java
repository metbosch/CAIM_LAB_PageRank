import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;

public class PageRank {

    static class Edge {
        int origin;                         // origin airport
        int weight;                         // number of routes in this edge
    }

    static class EdgeList {
        int weight;                         // Total number of outgoing routes from airport
        ArrayList<Edge> list;               // Incoming edges to airport
        HashMap<Integer, Edge> listAux;     // Temp. structure to generate list faster
    }

    static String airportCodes[];           // index to short code
    static String airportNames[];           // index to airport name
    static HashMap<String,Integer> airportIndices;  // airport code to index
    static EdgeList[] G;                    // G[i] is a list of pairs (j,k) meaning
                                            // "there are k routes from airport j to airport i"
    //....                                  // other info??

    public static void readAirports() {
      try {
         String fileName = "airports.txt";
         System.out.println("Opening file " + fileName);
         FileInputStream fstream = new FileInputStream(fileName);
         DataInputStream in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));

         String strLine;
         int index = 0;
         ArrayList<String> codeTemp = new ArrayList<String>();
         ArrayList<String> nameTemp = new ArrayList<String>();
         airportIndices = new HashMap<String,Integer>();

         while ((strLine = br.readLine()) != null) {
               String[] aLine = strLine.split(",");
               String airportCode = aLine[4];
               String airportName = aLine[1].substring(1, aLine[1].length() - 1) + " (" + aLine[3].substring(1, aLine[3].length() - 1) + ")";
               if (airportCode.length() > 2) {
                     airportCode = airportCode.substring(1, airportCode.length() - 1);
                     codeTemp.add(airportCode);
                     nameTemp.add(airportName);
                     airportIndices.put(airportCode, index); //MARC
                     index++;
               }

         }

         airportCodes = new String[index];
         airportNames = new String[index];
         G = new EdgeList[index];

         for (int i = 0; i < index; i++) {
              airportCodes[i] = codeTemp.get(i);
              airportNames[i] = nameTemp.get(i);
              G[i] = new EdgeList();
              G[i].weight = 0;
              G[i].list = new ArrayList<Edge>();
              G[i].listAux = new HashMap<Integer, Edge>();
         }
         // TO DO: DUMP STUFF TO airportCodes, airportNames, airportIndices

         System.out.println("Finished airports read. Total created " + index + " airports");

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
         System.out.println("Opening file " + fileName);
         FileInputStream fstream = new FileInputStream(fileName);
         DataInputStream in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));

         String strLine;
         int index = 0;
         while ((strLine = br.readLine()) != null) {
               String[] aLine = strLine.split(",");
               Integer fromAirport = airportIndices.get(aLine[2]);
               Integer toAirport = airportIndices.get(aLine[4]);
               if (fromAirport == null || toAirport == null) continue;
               else ++index;

               G[fromAirport].weight++;
               Edge tmp = G[toAirport].listAux.get(fromAirport);
               if (tmp == null) {
                  tmp = new Edge();
                  tmp.origin = fromAirport;
                  tmp.weight = 1;
                  G[toAirport].listAux.put(fromAirport, tmp);
                  G[toAirport].list.add(tmp);
               } else {
                  tmp.weight++;
               }
         }


         // At this point we do not need more the temporary list for each airport
         for (int i = 0; i < G.length; ++i) {
               G[i].listAux.clear();
         }

         System.out.println("Finished routes read. Total processed " + index + " routes");

         in.close();

       } catch (Exception e){
         //Catch exception if any
             System.err.println("Error: " + e.getMessage());
             // return null;
       }
   }

   public static void computePageRanks() {
      //....
   }

   public static void outputPageRanks() {
      //...
   }

   public static void main(String args[])  {

       readAirports();   // get airport names, codes, and assign indices
       readRoutes();     // read tuples and build graph
       computePageRanks();
       outputPageRanks();

    }

}
