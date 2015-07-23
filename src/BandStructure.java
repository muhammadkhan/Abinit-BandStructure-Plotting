import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BandStructure{

    private boolean kptScanMode = false;
    private double fermi = FERMI_NULL;
    private int nkpt;

    /**
     * key = k-point number
     * value = energy of each band at that k-point
     */
    private Map<Integer, List<Double>> bandData;
    private static final double FERMI_NULL   = Double.MAX_VALUE;
    private static final String FERMI_PREFIX = "Fermi";
    private static final String EIGVL_PREFIX = "Eigenvalues";

    private double determineFermiLevel(File f) throws IOException{
	BufferedReader br = new BufferedReader(new FileReader(f));
	String line;
	double x = FERMI_NULL;
	while((line = br.readLine()) != null){
	    line = line.trim();
	    if(line.startsWith(FERMI_PREFIX)){
		String[] parts = line.split("\\s+");
		for(int i = 0; i < parts.length; i++){
		    try{
			x = Double.parseDouble(parts[i]);
			break;
		    } catch(NumberFormatException nfe){
			continue;
		    }
		}
	    }
	}
	return x;
    }

    private String excludeLast(String s){
	return s.substring(0, s.length() - 1);
    }

    private <T> String delimit(Collection<T> stuff, String delimiter, String prefix){
	String res = "";
	for(T obj : stuff){
	    res += prefix + obj.toString() + delimiter;
	}
	return res.substring(0, res.length() - delimiter.length());
    }

    private <T> String delimit(Collection<T> stuff, String delimiter){
	return delimit(stuff, delimiter, "");
    }

    /**
       Precondition: a < b
     */
    private List<Integer> genRange(int a, int b){
	List<Integer> res = new ArrayList<Integer>();
	for(int i = a; i <= b; i++){
	    res.add(i);
	}
	return res;
    }

    private List<Double> shiftList(List<Double> lst, double shift){
	List<Double> ret = new ArrayList<Double>();
	for(Double d : lst)
	    ret.add(d + shift);
	return ret;
    }

    public BandStructure(String f, String fermiF) throws Exception{
	File in = new File(f);
	fermi = determineFermiLevel(new File(fermiF));
	BufferedReader br = new BufferedReader(new FileReader(in));
	bandData = new HashMap<Integer, List<Double>>();
	String inputLine;
	while((inputLine = br.readLine()) != null){
	    inputLine = inputLine.trim();
	    /*if(inputLine.startsWith(FERMI_PREFIX)){
	        fermi = determineFermiLevel(inputLine);
	    }*/
	    if(inputLine.startsWith(EIGVL_PREFIX)){
		kptScanMode = true;
		String[] parts = inputLine.split("\\s+");
		for(int i = 0; i < parts.length; i++){
		    if(parts[i].startsWith("nkpt")){
			nkpt = Integer.parseInt(parts[i+1]);
			break;
		    }
		}
	    }
	    else if(inputLine.startsWith("kpt") && kptScanMode){
		String[] parts = inputLine.split("\\s+");
		int kpt = Integer.parseInt(excludeLast(parts[1]));
		int nband = Integer.parseInt(excludeLast(parts[3]));
		List<Double> added = new ArrayList<Double>();
		while(added.size() < nband){
		    String[] data = br.readLine().trim().split("\\s+");
		    for(String datum : data)
			added.add(Double.parseDouble(datum));
		}
		bandData.put(kpt, added);
	    }
	}
	br.close();
	BufferedWriter bwCSV = new BufferedWriter(new FileWriter(new File(f + "_GEN.csv")));
	bwCSV.write("kpt,"+delimit(genRange(1,bandData.get(1).size()), ",", "Band "));
	bwCSV.newLine();
	for(Integer k : bandData.keySet()){
	    List<Double> bandsAtK = bandData.get(k);
	    bandsAtK = (fermi == FERMI_NULL) ? bandsAtK : shiftList(bandsAtK, fermi);
	    bwCSV.write(k + "," + delimit(bandsAtK,","));
	    bwCSV.newLine();
	}
	if(fermi != FERMI_NULL){
	    bwCSV.flush();
	    bwCSV.newLine();
	    bwCSV.write("Fermi,");
	    for(int i = 0; i < bandData.get(1).size(); i++){
		bwCSV.write(fermi + "");
		if(i < bandData.get(1).size()- 1)
		    bwCSV.write(',');
	    }
	    bwCSV.newLine();
	}
	bwCSV.close();
    }
    
    public static void main(String[] args){
	try{
	    new BandStructure(args[0], args[1]);
	} catch(Exception e){
	    throw new RuntimeException(e.getMessage());
	}
    }
}
