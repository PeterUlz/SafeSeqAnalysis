
import java.util.ArrayList;

//class for storing a Duplex Read
public class SafeSeqRead{
    private String name = "";
    String sequence1 = "";
    String sequence2 = "";
    String uid = "";
    String consensus = "";
  //--------------------------------------------------------
  // Constructor for class DuplexRead
  // use four lines from each FASTQ File to create an object of class DuplexRead
    public SafeSeqRead(String line1_1,String line2_1,String line3_1,String line4_1,String line1_2,String line2_2,String line3_2,String line4_2){
       name = line1_1;
       uid = line2_1.substring(0,14);
       sequence1 = line2_1.substring(15);
       sequence2 = line2_2;
    }
  //--------------------------------------------------------
    public String getUID(){
       return uid;
    }

  //--------------------------------------------------------
    public String getFRead(){
       return sequence1;
    }
  //--------------------------------------------------------
    public String getRRead(){
       return sequence2;
    }
  //--------------------------------------------------------
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
  //--------------------------------------------------------
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
}
