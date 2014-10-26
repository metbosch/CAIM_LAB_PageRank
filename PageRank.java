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
    static double[] ranks;                  // Rank of each airport

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
        ranks = new double[index];

        final double initRank = 1.0/index;
        for (int i = 0; i < index; i++) {
             airportCodes[i] = codeTemp.get(i);
             airportNames[i] = nameTemp.get(i);
             G[i] = new EdgeList();
             G[i].weight = 0;
             G[i].list = new ArrayList<Edge>();
             G[i].listAux = new HashMap<Integer, Edge>();
             ranks[i] = initRank;
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

    /**
      * Compute the page ranks for each airport
      * @param labmda [0, 1] Lambda value for page rank equation
      * @param stoper [0, inf.] Stop condition value. Specifies the maximum average change
      *               beetwen two iteration in page ranks
      * @param maxIters Max. number of iterations
      * @return Nuber of done iterations
      */
    public static int computePageRanks(final double lambda, final double stoper, final int maxIters) {
        final double maxChange = stoper * ranks.length;
        double change;
        int iters;

        System.out.println("Starting pageRank calculation");
        for (iters = 0; iters < maxIters; ++iters) {
            change = 0.0;
            for (int i = 0; i < ranks.length; ++i) {
                double newRank = 0.0;
                for (int j = 0; j < G[i].list.size(); ++j) {
                    Edge inEdge = G[i].list.get(j);
                    // (Rank of J) * (Edges from J to I) / (Outgoing edges of J)
                    newRank += ranks[inEdge.origin] * ((double)(inEdge.weight)) /
                                                      ((double)(G[inEdge.origin].weight));
                }
                newRank = (1 - lambda) + lambda*newRank;
                change += Math.abs(ranks[i] - newRank);
                ranks[i] = newRank;
            }
            if (change < maxChange) break;
        }
        System.out.println("Finished pageRank calculation after " + iters + " iterations");

        return iters;
    }

    public static void outputPageRanks() {
       //...
    }

    /**
      * Print usage info for this program
      */
    public static void usage() {
        System.out.println("Allowed parameters: maxIters, lambda, precision");
        System.out.println("If some parameter don't existes, default values will be used");
        System.out.println("Example: java PageRank maxIters 10000 lambda 0.9");
        System.exit(0);
    }


    /**
      * Getter of parameters from args array. If one error is found, usage() will be called
      * @param args Array of string to procces
      * @param max Maximum number of expected parameters (One parameter uses two positions in args array)
      * @return Map with the parameters, key: String, value: Double
      */
    public static HashMap<String, Double> getParams(String args[], final int max) {
       if (args.length%2 != 0 || args.length > max*2) {
           usage();
       }

       HashMap<String, Double> ret = new HashMap<String, Double>();
       for (int i = 0; i < args.length; i += 2) {
           ret.put(args[i], Double.parseDouble(args[i + 1]));
       }
       return ret;
    }

    public static void main(String args[])  {

        HashMap<String, Double> params = getParams(args, 3);
        if (params.get("maxIters") == null) params.put("maxIters", 1000000.0);
        if (params.get("lambda") == null) params.put("lambda", 0.85);
        if (params.get("precision") == null) params.put("precision", 0.001);

        readAirports();   // get airport names, codes, and assign indices
        readRoutes();     // read tuples and build graph
        computePageRanks(params.get("lambda"), params.get("precision"), params.get("maxIters").intValue());
        outputPageRanks();

    }

}
