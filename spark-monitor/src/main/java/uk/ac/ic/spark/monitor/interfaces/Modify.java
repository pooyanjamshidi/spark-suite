package uk.ac.ic.spark.monitor.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Modify {

	private ArrayList<String> paras = new ArrayList<String>();

	public void modifyConfig(Map<String, String> config) {
		// TODO Auto-generated method stub
		
	}
	
	private void readFile(String path){
	    try {
	    	File file = new File(path);
	        Scanner in = new Scanner(file);
 
            while (in.hasNextLine()) {
                String str = in.nextLine();
                paras.add(str);
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	private int isParaExist(String para){
		//0 means not exits, otherwise return the row
		for (int i = 0; i < paras.size(); i++){
			
			String lineString = paras.get(i);
			if (lineString.charAt(0) == '#')
				continue;
			else{
				int index = lineString.indexOf(" ");
				if (index != -1){//parameter with value
					String temp_para = lineString.substring(0, index); 
					if (temp_para.equals(para))
						return i;
				}
				else{//parameter without value
					String temp_para = lineString;
					if (temp_para.equals(para))
						return i;
				}	
			}	
		}
		return 0;
	}	
	
	public static void main(String[] args) {
		Modify m = new Modify();
		m.readFile("/homes/cl1415/workspace/com.interfaces/src/in.txt");
		System.out.println(m.isParaExist("Apache"));
		System.out.println(m.isParaExist("a"));
	}

}
