import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import exceptions.PropertyNotDefinedExceptions;



public class ChangeParameter implements ConfigModificatorInterface{

	private boolean propExisted = false;
	
	@Override
	public void modifyConfig(Map<String, String> config){
		// TODO Auto-generated method stub
		//Traverse config file
		Iterator<Entry<String, String>> mapIterator = config.entrySet().iterator();
		while(mapIterator.hasNext()){
			Map.Entry<String, String> property = (Map.Entry<String, String>)mapIterator.next();
			String key = property.getKey();
			String value = property.getValue();
			try {
				this.isProperty(key);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (PropertyNotDefinedExceptions e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String fileName = this.getFileName(key);
			try {
				this.modifyParameter(fileName, key, value);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}//modifyConfig
	
	/**
	 * 
	 * @param property
	 * @throws FileNotFoundException
	 * @throws PropertyNotDefinedExceptions
	 */
	private void isProperty(String property) throws FileNotFoundException, PropertyNotDefinedExceptions{
		boolean isProp = false;
		File file = new File("ConfigurationParameter.txt");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file);
		
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(line.contains(property)){
				isProp = true;
			}
		}
		
		if(!isProp){
			throw new PropertyNotDefinedExceptions();
		}
		
	}
	
	/**
	 * 
	 * @param property
	 * @return Belong to which file
	 */
	private String getFileName(String property){
		String fileName = null;
		if(property.contains("_")){
			fileName = "spark-env.sh.template";
		}
		
		if(property.contains(".")){
			fileName = "spark-defaults.conf.template";
		}
		
		if(property.contains("log")){
			fileName = "log4j.properties.template";
		}
		return fileName;
		
	}//getFileName
	
	/**
	 * 
	 * @param fileName
	 * @return the path of the file in Spark
	 */
	
	@SuppressWarnings("unused")
	private String getPath(String fileName){
		String pathName = null;
		// TODO get the path of the file
		
		
		return pathName;
	}
	
	
	/**
	 * 
	 * @param fileName
	 * @param property
	 * @param newValue
	 * @throws IOException
	 */
	@SuppressWarnings({ "resource" })
	private void modifyParameter(String fileName, String property, String newValue) throws IOException {
		String oldFileName = fileName;
		String tmpFileName = "temp_" + oldFileName;
		
		File file = new File(fileName);
		Scanner fileScanner = new Scanner(file);
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		br = new BufferedReader(new FileReader(oldFileName));
		bw = new BufferedWriter(new FileWriter(tmpFileName));
		String line;
		while((line = br.readLine()) != null){
			if(line.contains("#"))
				continue;
			else{
				// Property already existed, modify its value 
				if(line.contains(property)){
					propExisted = true;
					String next = fileScanner.next();
					//debug
					System.out.println("next: " + next);
					int propertyLength = fileScanner.next().length();
					String value = line.substring(propertyLength + 1);
					//debug
					System.out.println("value: " + value);
					// Replace old value
					line = line.replace(value, " " + newValue );
					bw.write(line + '\n');
				}
			}
		}//while
		
		//Property not yet existed, add it to file
		if(!propExisted){
			bw.write(property + " " + newValue + '\n');
		}
		
		//reset
		propExisted = false;
		
		if(br != null)
			br.close();
		if(bw != null)
			bw.close();
		
		// Delete old file.
		File oldFile = new File(oldFileName);
		oldFile.delete();
		
		// Rename new file to old file name
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile);
	}//modifyParameter

}
