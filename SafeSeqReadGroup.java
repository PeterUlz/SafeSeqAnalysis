import java.util.ArrayList;

public class SafeSeqReadGroup{
    String consensus_f = "";
    String consensus_r = "";
    String uid = "";
    int reads = 0;
    ArrayList<String> f_reads = null;
    ArrayList<String> r_reads = null;
  //--------------------------------------------------------
    public SafeSeqReadGroup(String c_uid, String f_read, String r_read){
       uid = c_uid;
       f_reads = new ArrayList<String>();
       r_reads = new ArrayList<String>();
       f_reads.add(f_read);
       r_reads.add(r_read);
       reads = 1;
    }
  //--------------------------------------------------------
    public SafeSeqReadGroup(SafeSeqRead read){
       uid = read.getUID();
       f_reads = new ArrayList<String>();
       r_reads = new ArrayList<String>();
       f_reads.add(read.getFRead());
       r_reads.add(read.getRRead());
       reads = 1;
    }
  //--------------------------------------------------------
    public String getUID(){
       return uid;
    }   

  //--------------------------------------------------------
    public int getReadCount(){
       return reads;
    }   
  //--------------------------------------------------------
    public String getConsensusF(){
       return consensus_f;
    }  
  //--------------------------------------------------------
    public String getConsensusR(){
       return consensus_r;
    }  
  //--------------------------------------------------------
    public void addReads(SafeSeqRead read){
       f_reads.add(read.getFRead());
       r_reads.add(read.getRRead());
       reads++;
    }
  //--------------------------------------------------------
    public void addComplReads(SafeSeqRead read){
       r_reads.add(revCompl(read.getFRead()));
       f_reads.add(revCompl(read.getRRead()));
       reads++;
    }
  //--------------------------------------------------------
    public void addFRead(String f_read){
       f_reads.add(f_read);
       reads++;
    }
  //--------------------------------------------------------
    public void addRRead(String r_read){
       r_reads.add(r_read);
    }

    private String revCompl(String string){
       char[] rev_compl = string.toCharArray();
       char[] temp = string.toCharArray();
       for (int i = 0; i < rev_compl.length; i++){
           if (rev_compl[rev_compl.length-i-1] == 'A'){
               temp[i] = 'T';}
           else if (rev_compl[rev_compl.length-i-1] == 'T'){
               temp[i] = 'A';}
           else if (rev_compl[rev_compl.length-i-1] == 'C'){
               temp[i] = 'G';}
           else if (rev_compl[rev_compl.length-i-1] == 'G'){
               temp[i] = 'C';}
           else {
               temp[i] = 'N';}
       }
       String return_string = new String(temp);
       return return_string;
    }

    private String Compl(String string){
       char[] rev_compl = string.toCharArray();
       char[] temp = string.toCharArray();
       for (int i = 0; i < rev_compl.length; i++){
           if (rev_compl[i] == 'A'){
               temp[i] = 'T';}
           else if (rev_compl[i] == 'T'){
               temp[i] = 'A';}
           else if (rev_compl[i] == 'C'){
               temp[i] = 'G';}
           else if (rev_compl[i] == 'G'){
               temp[i] = 'C';}
           else {
               temp[i] = 'N';}
       }
       String return_string = new String(temp);
       return return_string;
    }
  //--------------------------------------------------------
    public void createConsensusF(){
        if (f_reads.size() == 1){
           consensus_f = f_reads.get(0);}

        char[] consensus = new char[f_reads.get(0).length()];
        for (int i = 0; i < f_reads.get(0).length(); i++){
            int a_count = 0;
            int t_count = 0;
            int c_count = 0;
            int g_count = 0;

            for (int j = 0; j < f_reads.size(); j++){
                char[] temp = f_reads.get(j).toCharArray();
                if ( i >= temp.length){ 
                   continue;}
                if (temp[i] == 'A'){
                   a_count++;}
                if (temp[i] == 'T'){
                   t_count++;}
                if (temp[i] == 'C'){
                   c_count++;}
                if (temp[i] == 'G'){
                   g_count++;}
            }
            if ( (a_count > t_count) && (a_count > c_count) && (a_count > g_count) ){
                consensus[i] = 'A'; }
            if ( (t_count > a_count) && (t_count > c_count) && (t_count > g_count) ){
                consensus[i] = 'T'; }
            if ( (c_count > a_count) && (c_count > t_count) && (c_count > g_count) ){
                consensus[i] = 'C'; }
            if ( (g_count > a_count) && (g_count > t_count) && (g_count > c_count) ){
                consensus[i] = 'G'; }
        }
        consensus_f = new String(consensus);      
    }

  //--------------------------------------------------------
    public void createConsensusR(){
        if (r_reads.size() == 1){
           consensus_r = r_reads.get(0);}

        char[] consensus = new char[r_reads.get(0).length()];
        for (int i = 0; i < r_reads.get(0).length(); i++){
            int a_count = 0;
            int t_count = 0;
            int c_count = 0;
            int g_count = 0;

            for (int j = 0; j < r_reads.size(); j++){
                char[] temp = r_reads.get(j).toCharArray();
                if ( i >= temp.length){ 
                   continue;}
                if (temp[i] == 'A'){
                   a_count++;}
                if (temp[i] == 'T'){
                   t_count++;}
                if (temp[i] == 'C'){
                   c_count++;}
                if (temp[i] == 'G'){
                   g_count++;}
            }
            if ( (a_count > t_count) && (a_count > c_count) && (a_count > g_count) ){
                consensus[i] = 'A'; }
            if ( (t_count > a_count) && (t_count > c_count) && (t_count > g_count) ){
                consensus[i] = 'T'; }
            if ( (c_count > a_count) && (c_count > t_count) && (c_count > g_count) ){
                consensus[i] = 'C'; }
            if ( (g_count > a_count) && (g_count > t_count) && (g_count > c_count) ){
                consensus[i] = 'G'; }
        }
        consensus_r = new String(consensus);      
    }

  //--------------------------------------------------------
    public void printReads(){
        System.out.println("Forward Reads");
        for (int i = 0; i < f_reads.size();i++){
            System.out.println(f_reads.get(i));
        }
        System.out.println("Consensus: "+consensus_f);
        System.out.println("Reverse Reads");
        for (int i = 0; i < r_reads.size();i++){
            System.out.println(r_reads.get(i));
        }
        System.out.println("Consensus: "+consensus_r);
    }
}
