package fr.uga.pddl4j.tutorial.asp;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.heuristics.relaxation.Heuristic;
import fr.uga.pddl4j.heuristics.relaxation.HeuristicToolKit;
import fr.uga.pddl4j.parser.ErrorManager;
import fr.uga.pddl4j.planners.Planner;
import fr.uga.pddl4j.planners.ProblemFactory;
import fr.uga.pddl4j.planners.Statistics;
import fr.uga.pddl4j.planners.statespace.AbstractStateSpacePlanner;
import fr.uga.pddl4j.planners.statespace.StateSpacePlanner;
import fr.uga.pddl4j.planners.statespace.search.strategy.AStar;
import fr.uga.pddl4j.planners.statespace.search.strategy.Node;
import fr.uga.pddl4j.util.AbstractCodedOp;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.CondBitExp;
import fr.uga.pddl4j.util.MemoryAgent;
import fr.uga.pddl4j.util.Plan;
import fr.uga.pddl4j.util.SequentialPlan;
import lejos.hardware.Button;
import Final.Cam_Palet;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Set;

/**
 * This class implements a simple forward planner based on A* algorithm.
 *
 * @author D. Pellier
 * @version 1.0 - 06.06.2018
 */
public final class ASP extends AbstractStateSpacePlanner {

    /*
     * The arguments of the planner.
     */
    private Properties arguments;

    /**
     * Creates a new HSP planner with the default parameters.
     *
     * @param arguments the arguments of the planner.
     */
    public ASP(final Properties arguments) {
        super();
        this.arguments = arguments;
    }

    /**
     * Solves the planning problem and returns the first solution search found.
     *
     * @param problem the problem to be solved.
     * @return a solution search or null if it does not exist.
     */
    @Override
    public Plan search(final CodedProblem problem) {

        // First we create an instance of the heuristic to use to guide the search
        final Heuristic heuristic = HeuristicToolKit.createHeuristic(Heuristic.Type.FAST_FORWARD, problem);

        // We get the initial state from the planning problem
        final BitState init = new BitState(problem.getInit());

        // We initialize the closed list of nodes (store the nodes explored)
        final Set<Node> close = new HashSet<>();

        // We initialize the opened list to store the pending node according to function f
        final double weight = (double) arguments.get(StateSpacePlanner.WEIGHT);
        final PriorityQueue<Node> open = new PriorityQueue<>(100, new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                double f1 =  weight * n1.getHeuristic() + n1.getCost();
                double f2 = weight * n2.getHeuristic() + n2.getCost();
                return Double.compare(f1, f2);
            }
        });

        // We create the root node of the tree search
        final Node root = new Node(init, null, -1, 0, heuristic.estimate(init, problem.getGoal()));

        // We adds the root to the list of pending nodes
        open.add(root);
        Plan plan = null;

        final int timeout = ((int) this.arguments.get(Planner.TIMEOUT)) * 1000;
        long time = 0;

        // We start the search
        while (!open.isEmpty() && plan == null && time < timeout) {

            // We pop the first node in the pending list open
            final Node current = open.poll();
            close.add(current);

            // If the goal is satisfy in the current node then extract the search and return it
            if (current.satisfy(problem.getGoal())) {
                return this.extractPlan(current, problem);
            }

            // Else we try to apply the operators of the problem to the current node
            else {
                for (int i = 0; i < problem.getOperators().size(); i++) {
                    // We get the its operator of the problem
                    BitOp a = problem.getOperators().get(i);
                    // If the operator is applicable in the current node
                    if (a.isApplicable(current)) {
                        Node next = new Node(current);
                        // We apply the effect of the operator
                        final List<CondBitExp> effects = a.getCondEffects();
                        for (CondBitExp ce : effects) {
                            if (current.satisfy(ce.getCondition())) {
                                next.apply(ce.getEffects());
                            }
                        }
                        // We set the new child node information
                        final double g = current.getCost() + 1;
                        if (!close.contains(next)) {
                            next.setCost(g);
                            next.setParent(current);
                            next.setOperator(i);
                            next.setHeuristic(heuristic.estimate(next, problem.getGoal()));
                            open.add(next);
                        }
                    }
                }
            }
        }

        // We compute the memory by the search
        this.getStatistics().setMemoryUsedToSearch(MemoryAgent.sizeOf(open) + MemoryAgent.sizeOf(close));

        // Finally, we return the search computed or null if no search was found
        return plan;
    }

    /**
     * Extracts a search from a specified node.
     *
     * @param node the node.
     * @param problem the problem.
     * @return the search extracted from the specified node.
     */
    private Plan extractPlan(final Node node, final CodedProblem problem) {
        Node n = node;
        final Plan plan = new SequentialPlan();
        while (n.getOperator() != -1) {
            final BitOp op = problem.getOperators().get(n.getOperator());
            plan.add(0, op);
            n = n.getParent();
        }
        return plan;
    }

    /**
     * Print the usage of the ASP planner.
     */
    private static void printUsage() {
        final StringBuilder strb = new StringBuilder();
        strb.append("\nusage of PDDL4J:\n")
                .append("OPTIONS   DESCRIPTIONS\n")
                .append("-o <str>    operator file name\n")
                .append("-f <str>    fact file name\n")
                .append("-w <num>    the weight used in the a star seach (preset: 1.0)\n")
                .append("-t <num>    specifies the maximum CPU-time in seconds (preset: 300)\n")
                .append("-h          print this message\n\n");
        Planner.getLogger().trace(strb.toString());
    }

    /**
     * Parse the command line and return the planner's arguments.
     *
     * @param args the command line.
     * @return the planner arguments or null if an invalid argument is encountered.
     */
    private static Properties parseCommandLine(String[] args) {

        // Get the default arguments from the super class
        final Properties arguments = StateSpacePlanner.getDefaultArguments();

        // Parse the command line and update the default argument value
        for (int i = 0; i < args.length; i += 2) {
            if ("-o".equalsIgnoreCase(args[i]) && ((i + 1) < args.length)) {
                if (!new File(args[i + 1]).exists()) return null;
                arguments.put(Planner.DOMAIN, new File(args[i + 1]));
            } else if ("-f".equalsIgnoreCase(args[i]) && ((i + 1) < args.length)) {
                if (!new File(args[i + 1]).exists()) return null;
                arguments.put(Planner.PROBLEM, new File(args[i + 1]));
            } else if ("-t".equalsIgnoreCase(args[i]) && ((i + 1) < args.length)) {
                final int timeout = Integer.parseInt(args[i + 1]) * 1000;
                if (timeout < 0) return null;
                arguments.put(Planner.TIMEOUT, timeout);
            } else if ("-w".equalsIgnoreCase(args[i]) && ((i + 1) < args.length)) {
                final double weight = Double.parseDouble(args[i + 1]);
                if (weight < 0) return null;
                arguments.put(StateSpacePlanner.WEIGHT, weight);
            } else {
                return null;
            }
        }
        // Return null if the domain or the problem was not specified
        return (arguments.get(Planner.DOMAIN) == null
                || arguments.get(Planner.PROBLEM) == null) ? null : arguments;
    }

    /**
     * The main method of the <code>ASP</code> example. The command line syntax is as
     * follow:
     * <p>
     * <pre>
     * usage of ASP:
     *
     * OPTIONS   DESCRIPTIONS
     *
     * -o <i>str</i>   operator file name
     * -f <i>str</i>   fact file name
     * -w <i>num</i>   the weight used in the a star search (preset: 1)
     * -t <i>num</i>   specifies the maximum CPU-time in seconds (preset: 300)
     * -h              print this message
     *
     * </pre>
     * </p>
     *
     * @param args the arguments of the command line.
     */
    public static void main(String[] args) {
    	try 
        {
          int port = 63000;
          
          /*EV3 ev3brick = (EV3) BrickFinder.getLocal();
          Sound.setVolume(10);
          Sound.beep();*/

          // Create a socket to listen on the port.
          DatagramSocket dsocket = new DatagramSocket(port);

          // Create a buffer to read datagrams into. If a
          // packet is larger than this buffer, the
          // excess will simply be discarded!
          byte[] buffer = new byte[2048];
          byte[] bufferSend = new byte[2048];

          // Create a packet to receive data into the buffer
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          
          //A packet for the pddl data
          DatagramPacket pddlData;

          // Now loop forever, waiting to receive packets and printing them.
          boolean again = true;
          while (again) 
          {
        	String data = new String();
            // Wait to receive a datagram
            //dsocket.receive(packet);
            
            
            //Once received we launch pddl4j and send the info
        	System.out.println("Starting pddl ... ");
            data = run(args);
            bufferSend = data.getBytes();
            System.out.println("pddl done.");
            
            pddlData = new DatagramPacket(bufferSend, bufferSend.length);
            
            System.out.println("Sending data ... ");
            dsocket.send(pddlData);
            System.out.println("data sent");

            // Convert the contents to a string, and display them
            /*String msg = new String(buffer, 0, packet.getLength());
            //System.out.println(packet.getAddress().getHostName() + ": "
            //    + msg);
            
            String[] palets = msg.split("\n");
            
            for (int i = 0; i < palets.length; i++) 
            {
            	String[] coord = palets[i].split(";");
            	int x = Integer.parseInt(coord[1]);
            	int y = Integer.parseInt(coord[2]);
            
            	System.out.println();
            	LCD.clear();
            	LCD.drawString("                                                                     ", 0, i);
            	LCD.drawString("x: " + Integer.toString(x) + " y: " + Integer.toString(y) , 0, i);
            }*/
            

            // Reset the length of the packet before reusing it.
            packet.setLength(buffer.length);
            
            if(Button.ESCAPE.isDown()) {
    			again = false;
    		}
          }
         
        } 
        catch (Exception e) 
        {
          System.err.println(e);
        }
    }
    
    public static String initParameters(String plan, HashMap<String, Point> emplacements) {
    	String[] lines = plan.split(System.getProperty("line.separator"));
    	String coord = null;
    	
    	//for(String line : lines){
    		String[] res = lines[0].split("\\s+");
    		String nameFunction = res[2];
    		if (nameFunction.equals("goto")) {
    			String namePal = res[5].substring(0, res[5].length() -1);
    			
    			coord = emplacements.get(namePal).getX() + " " + emplacements.get(namePal).getY();
    			
    			System.out.println("TODO : goto " + emplacements.get(namePal) + "[" + namePal + "]");
    		}
    	//}
    	
    	return coord;
    }
    
    public static String run(String[] args) {
    	final Properties arguments = ASP.parseCommandLine(args);
        if (arguments == null) {
            ASP.printUsage();
            System.exit(0);
        }
        
    	final ASP planner = new ASP(arguments);
    	final ProblemFactory factory = ProblemFactory.getInstance();

        File domain = (File) arguments.get(Planner.DOMAIN);
        
        String strProblem = new String("");
        strProblem = strProblem.concat("(define (problem pb_robot)\r\n" + 
        		"  (:domain dataRobot)\r\n" + 
        		"\r\n" + 
        		"  (:objects\r\n" + 
        		"     C3PO - robot\n" +
        		"	  p - pince\n" + 
        		"     ");
        System.out.println("Accessing camera ... ");
        Cam_Palet cam = new Cam_Palet();
        System.out.println("Accessing camera ... ");
        ArrayList<Point> palets = cam.GetPaletList();
        System.out.println("Camera ok");
        
        /*ArrayList<Point> palets = new ArrayList<Point>();
        palets.add(new Point(5, 5));
        palets.add(new Point(8, 8));
        palets.add(new Point(9, 10));*/
        HashMap<String, Point> infoPalets = new HashMap<String, Point>();
        
        int i = 0;
        for (Point p : palets) {
        	strProblem = strProblem.concat("pal"+i + " ");
        	i++;
        }
        strProblem = strProblem.concat("- palet\n		 ");
        
        i = 0;
        for (Point p : palets) {
        	strProblem = strProblem.concat("emppal"+i + " ");
        	infoPalets.put("emppal"+i, p);
        	i++;
        }
        strProblem = strProblem.concat("emprobot - coord)\n \n (:init (open p)\n		");
        
        i = 0;
        for (Point p : palets) {
        	strProblem = strProblem.concat("(at pal"+i+" emppal"+i+")\n		");
        	i++;
        }
        strProblem = strProblem.concat("( at C3PO emprobot)\n  )\n\n  ");
        strProblem = strProblem.concat("(:goal (and ");
        
        //strProblem = strProblem.concat("(in pal1 C3PO) (close p)");
        
        i = 0;
        for (Point p : palets) {
        	strProblem = strProblem.concat("(at pal"+i+" emprobot) ");
        	i++;
        } 
        
        strProblem = strProblem.concat("))\n)");
        
        System.err.println(strProblem);
        
        //File problem = (File) arguments.get(Planner.PROBLEM);
        File customProblem = new File("customProblem.pddl");
        ErrorManager errorManager = null;
        try {
            if (customProblem.createNewFile()) {
            	System.out.println("Custom problem created");
            }
            FileWriter fw = new FileWriter(customProblem, false);
            fw.write(strProblem);
            fw.close();
            
            errorManager = factory.parse(domain, customProblem);
        } catch (IOException e) {
        	e.printStackTrace();
            Planner.getLogger().trace("\nunexpected error when parsing the PDDL planning problem description.");
            System.exit(0);
        }

        if (!errorManager.isEmpty()) {
            errorManager.printAll();
            System.exit(0);
        } else {
            Planner.getLogger().trace("\nparsing domain file done successfully");
            Planner.getLogger().trace("\nparsing problem file done successfully\n");
        }

        final CodedProblem pb = factory.encode();
        planner.getStatistics().setMemoryUsedForProblemRepresentation(MemoryAgent.getDeepSizeOf(pb));

        Planner.getLogger().trace("\nencoding problem done successfully ("
                + pb.getOperators().size() + " ops, "
                + pb.getRelevantFacts().size() + " facts)\n");

        if (!pb.isSolvable()) {
            Planner.getLogger().trace(String.format("goal can be simplified to FALSE."
                    +  "no search will solve it%n%n"));
            System.exit(0);
        }

        long begin = System.currentTimeMillis();
        final Plan plan = planner.search(pb);
        planner.getStatistics().setTimeToSearch(System.currentTimeMillis() - begin);

        if (plan != null) {
            // Print plan information
            Planner.getLogger().trace(String.format("%nfound plan as follows:%n%n" + pb.toString(plan)));
            //Planner.getLogger().trace(String.format("%nplan total cost: %4.2f%n%n", plan.cost()));
            
            String coord = initParameters(pb.toString(plan), infoPalets);
            
            return coord;
            /*for (BitOp bop : plan.actions()) {
            	System.out.println(" -->" + bop.getName() + "<--");
            	//System.out.println(" -->" + bop.getTypeOfParameters(0) + "<--");
            	//if (bop.getName().equals("goto")) {
            		System.out.println(bop.getName().toUpperCase());
            		for (CondBitExp j : bop.getCondEffects().) {
            			System.out.println("Param : " + j.getEffects());
            		}
            	//}
            }*/
            
        } else {
            Planner.getLogger().trace(String.format(String.format("%nno plan found%n%n")));
            return null;
        }

        // Get the runtime information from the planner
        /*Statistics info = planner.getStatistics();

// Print time information
        long time = info.getTimeToParse() +  info.getTimeToEncode() + info.getTimeToSearch();
        Planner.getLogger().trace(String.format("%ntime spent:   %8.2f seconds parsing %n", info.getTimeToParse()/1000.0));
        Planner.getLogger().trace(String.format("              %8.2f seconds encoding %n", info.getTimeToEncode()/1000.0));
        Planner.getLogger().trace(String.format("              %8.2f seconds searching%n", info.getTimeToSearch()/1000.0));
        Planner.getLogger().trace(String.format("              %8.2f seconds total time%n", time/1000.0));

// Print memory usage information
        long memory = info.getMemoryUsedForProblemRepresentation() + info.getMemoryUsedToSearch();
        Planner.getLogger().trace(String.format("%nmemory used:  %8.2f MBytes for problem representation%n", info.getMemoryUsedForProblemRepresentation()/(1024.0*1024.0)));
        Planner.getLogger().trace(String.format("              %8.2f MBytes for searching%n", info.getMemoryUsedToSearch()/(1024.0*1024.0)));
        Planner.getLogger().trace(String.format("              %8.2f MBytes total%n%n%n", memory/(1024.0*1024.0)));*/

    }
}
