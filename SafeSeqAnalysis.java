
import java.lang.Exception.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

//Main Class to perform analysis


public class SafeSeqAnalysis {
    //Store Fastq Files in Analysis
    private static File fastq1;
    private static File fastq2;
    private static File output_fastq1;
    private static File output_fastq2;
    private static File stats;
    private static ArrayList<SafeSeqRead> read_list;
    private static ArrayList<SafeSeqReadGroup> read_group;

  //--------------------------------------------------------
  // This function is called first 
    public static void main(String[] args){
       checkArgs(args);
       getFastq(args);  
       createReadArray();   
       createReadGroups();  
       printResults();
    }

  //--------------------------------------------------------
  // Check command line arguments
    private static void checkArgs(String[] args){
	if (args.length != 5){
	    System.out.println("Usage: java SafeSeqAnalysis <inputfastq_forward> <inputfastq_reverse> <output_fasta_f> <output_fasta_r> <stats>");
	    System.exit(1);
        }
    }
  //--------------------------------------------------------
  // Try to open the two fastq files specified in input and exit when not
    private static void getFastq(String[] args){
       try {
	   fastq1 = new File(args[0]);
	   fastq2 = new File(args[1]);
	   output_fastq1 = new File(args[2]);
	   output_fastq2 = new File(args[3]);
	   stats = new File(args[4]);
       } catch (Exception e) {
           e.printStackTrace();  
       } 
    }

  //--------------------------------------------------------
  // create array of reads from specified FASTQ Files
    private static void createReadArray(){
        System.out.println("Create Read Array");
        int readcount = 0;
	BufferedReader reader1 = null;
	BufferedReader reader2 = null;
	
	//make ArrayList from Class DuplexRead
        read_list = new ArrayList<SafeSeqRead>();
	try {
	    reader1 = new BufferedReader(new FileReader(fastq1));
	    String text = null;
	    reader2 = new BufferedReader(new FileReader(fastq2));

	    while ((text = reader1.readLine()) != null) {
		//read four lines from each FASTQ File
                readcount++;
                //System.out.println("--------------------------------");
		String line1= text;
  		String line2= reader1.readLine();
		String line3= reader1.readLine();
		String line4= reader1.readLine();
		String line1_2= reader2.readLine();
  		String line2_2= reader2.readLine();
		String line3_2= reader2.readLine();
		String line4_2= reader2.readLine();
  
		//create new DuplexRead from 8 FASTQ lines (might be replaced with other class e.g. SafeSeq)
                SafeSeqRead temp = new SafeSeqRead(line1,line2,line3,line4,line1_2,line2_2,line3_2,line4_2);       

  		//set Filter criteria for reads (not applicable for SafeSeq!)
                if ( ! temp.getUID().contains("N") ){    
                    //System.out.println(temp.getFiveBase()); 
		    // add temporary obejct of class DuplexRead to the ArrayList            
                    read_list.add(temp);}
		//output amount of reads analyzed
                if (readcount % 1000000 == 0){
                    System.out.println(readcount+" reads in memory");}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
        }
        System.out.println(read_list.size()+" reads passed filter");
    }

  //--------------------------------------------------------
    private static void createReadGroups(){
       System.out.println("Create Read Groups");

       //create temporary ReadGroup in order to create HashMap 
       SafeSeqReadGroup temp = new SafeSeqReadGroup(read_list.get(0));

       //create ArrayList of class ReadGroup
       read_group = new ArrayList<SafeSeqReadGroup>();
       read_group.add(temp);
      
       //create a HashMap, where String is UID and ReadGroup is ReadGroup associated with UID       
       HashMap<String, SafeSeqReadGroup> rg_hashmap = new HashMap<String, SafeSeqReadGroup>();
       rg_hashmap.put(read_list.get(0).getUID(), temp);
       int count = 0;
       int f_match_count = 0;
       int r_match_count = 0;
       int non_match_count = 0;
         

       //iterate through all the reads
       for (int i = 1; i < read_list.size(); i++){
          count++;
          String uid = read_list.get(i).getUID();

          //iterate through existing read groups to check whether UID is present
          if (rg_hashmap.containsKey(uid)){ 
              f_match_count++;
              rg_hashmap.get(uid).addReads(read_list.get(i));
          } else {
              non_match_count++;
              SafeSeqReadGroup new_group = new SafeSeqReadGroup(read_list.get(i));
              read_group.add(new_group);
              rg_hashmap.put(uid, new_group);
          }
          if (count % 1000000 == 0){
              System.out.println(count+" reads processed\tRead Groups: "+read_group.size());}
       }

       Iterator it = rg_hashmap.entrySet().iterator();
       while (it.hasNext()) {
          Map.Entry pairs = (Map.Entry)it.next();
          SafeSeqReadGroup temp_group = (SafeSeqReadGroup) pairs.getValue();
          temp_group.createConsensusF();
          temp_group.createConsensusR();
          it.remove(); // avoids a ConcurrentModificationException
       }

       
       
    }

  //--------------------------------------------------------
    private static void printResults(){

             
       System.out.println("\nRead Groups: "+Integer.toString(read_group.size()));
       
	try {
	   PrintWriter writer_f = new PrintWriter(output_fastq1, "UTF-8");
           PrintWriter writer_r = new PrintWriter(output_fastq2, "UTF-8");
           PrintWriter writer_stats = new PrintWriter(stats, "UTF-8");
	
       
       
           ArrayList<SafeSeqReadGroup> final_group = new ArrayList<SafeSeqReadGroup>();
           for (int j = 0; j < read_group.size();j++){
              if (read_group.get(j).getReadCount() > 4){
                final_group.add(read_group.get(j));
	        writer_f.println(">"+read_group.get(j).getUID()+"_"+Integer.toString(read_group.get(j).getReadCount()));
	        writer_f.println(read_group.get(j).getConsensusF());
	        writer_r.println(">"+read_group.get(j).getUID()+"_"+Integer.toString(read_group.get(j).getReadCount()));
	        writer_r.println(read_group.get(j).getConsensusR());
              }
           }
           System.out.println("\nRead Groups more than 4 dup: "+Integer.toString(final_group.size()));
           int total = 0;
           int ge2_count = 0;
           int ge5_count = 0;
           int ge10_count = 0;
	   int total_all = 0;
           for (int j = 0; j < read_group.size();j++){
	      total_all += read_group.get(j).getReadCount();
	      writer_stats.println(Integer.toString(read_group.get(j).getReadCount()));
              if (read_group.get(j).getReadCount() > 2){
                 total += read_group.get(j).getReadCount();
                 ge2_count++;
              }             
	      if (read_group.get(j).getReadCount() > 5){
                 ge5_count++;
              }             
	      if (read_group.get(j).getReadCount() > 10){
                 ge10_count++;
              }
           }
	System.out.println("\nRead Groups more than 2 dup: "+Integer.toString(ge2_count));
	System.out.println("\nRead Groups more than 5 dup: "+Integer.toString(ge5_count));
	System.out.println("\nRead Groups more than 10 dup: "+Integer.toString(ge10_count));
           float average = (float) total / (float) ge2_count;
           float average_all = (float) total_all / (float) read_group.size();
           System.out.println("\nAverage Read Group Count: "+Float.toString(average_all));
           System.out.println("\nAverage Read Group Count for Read Groups with more than 2 dup: "+Float.toString(average));
	}
	catch (Exception e) {
		e.printStackTrace();}

       
    }
}
