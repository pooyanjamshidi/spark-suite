package uk.ac.ic.spark.monitor.util;


import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import uk.ac.ic.spark.monitor.exceptions.*;


public class ChangeParameter  {

	private boolean propExisted = false;
	BufferedReader br = null;
	BufferedWriter bw = null;


	public void modifyConfig(Map<String, String> config){
		// TODO Auto-generated method stub
		//Traverse config file
		Iterator<Entry<String, String>> mapIterator = config.entrySet().iterator();
		while(mapIterator.hasNext()){
			Entry<String, String> property = (Entry<String, String>)mapIterator.next();
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
		File file = new File("/Users/hubert/Dropbox/Homework/spark-suite/spark-monitor/src/main/resources/configurationFile/ConfigurationParameter.txt");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file);

		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(line.contains(property)){
				isProp = true;
			}
		}

		if(!isProp){
			throw new PropertyNotDefinedExceptions(property);
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
			fileName = "/Users/hubert/Dropbox/Homework/spark-suite/spark-monitor/src/main/resources/configurationFile/spark-env.sh.template";
		}

		if(property.contains(".")){
			fileName = "/Users/hubert/Dropbox/Homework/spark-suite/spark-monitor/src/main/resources/configurationFile/spark-defaults.conf.template";
		}

		if(property.contains("log")){
			fileName = "/Users/hubert/Dropbox/Homework/spark-suite/spark-monitor/src/main/resources/configurationFile/log4j.properties.template";
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
		String tempPath[] = fileName.split("/");

		int pathLength = tempPath.length;
		System.out.println("fileName: " + tempPath[pathLength-1]);
		tempPath[pathLength-1] = "temp_" + tempPath[pathLength-1];
		String tmpFileName = "";
		for(int i = 0; i < tempPath.length; i++){
			if(i > 0)
			 tmpFileName += "/" + tempPath[i];
		}

		//String tmpFileName = tempPath.toString();
		System.out.println(tmpFileName);
		File file = new File(fileName);
		Scanner fileScanner = new Scanner(file);

		//BufferedReader br = null;
		//BufferedWriter bw = null;

		br = new BufferedReader(new FileReader(oldFileName));
		bw = new BufferedWriter(new FileWriter(tmpFileName,true));
		String line;
		while((line = br.readLine()) != null){
			if(line.contains("#")) {
				bw.write(line);
				continue;
			}
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
				else{
					String next = fileScanner.next();
					//debug
					System.out.println("next: " + next);
					int propertyLength = fileScanner.next().length();
					String value = line.substring(propertyLength + 1);
					//debug
					System.out.println("value: " + value);
					bw.write(line + '\n');

				}
			}
		}//while

		//Property not yet existed, add it to file
		if(!propExisted){

				System.out.println("new property: " + property);
				System.out.println("new value: " + newValue);
			    bw.write(property + " " + newValue + '\n');
		}

		//reset
		propExisted = false;

		if(br != null)
			br.close();
		if(bw != null)
			bw.flush();
			bw.close();


		// Delete old file.
		File oldFile = new File(oldFileName);
		oldFile.delete();

		// Rename new file to old file name
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile);
	}//modifyParameter


	//test
    public static void main(String[] args){
		Map<String, String> propertyList = new HashMap<String, String>();
//		propertyList.put("spark.shuffle.io.maxRetries", "1");
//		propertyList.put("SPARK_LOG_DIR", "test5");
//		propertyList.put("SPARK_IDENT_STRING", "test3");
//		propertyList.put("SPARK_NICENESS", "test1rt");
		ChangeParameter changeParameter = new ChangeParameter();
		changeParameter.modifyConfig(propertyList);
	}
}
