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
    }

    static String airportCodes[];           // index to short code
    static String airportNames[];           // index to airport name
    static HashMap<String,Integer> airportIndices;  // airport code to index
    static EdgeList[] G;             // G[i] is a list of pairs (j,k) meaning
                                     // "there are k routes from airport i to airport j"
    ....                             // other info??
    
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
               String fromAirport = aLine[2];
               String toAirport = aLine[4];

               //A lot of things to do
         }

         // TO DO: DUMP STUFF TO airportCodes, airportNames, airportIndices
         
         System.out.println("... "+index+" routes read");

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
