package uk.ac.ic.spark.monitor.util;


import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Pattern;

import uk.ac.ic.spark.monitor.exceptions.*;
import uk.ac.ic.spark.monitor.config.*;

public class
ChangeParameter{

	private boolean propExisted = false;
	BufferedReader br = null;
	BufferedWriter bw = null;


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
		File file = new File(ConstantConfig.SPARK_CONFIG);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file);

		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(line.contains(property)){
				isProp = true;
			}
		}

		if(!isProp){
			//throw new PropertyNotDefinedExceptions();
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
			fileName = ConstantConfig.SPARK_ENV_SH_TEMPLATE;
		}

		if(property.contains(".")){
			fileName = ConstantConfig.SPARK_DEFAULTS_CONF;
		}

		if(property.contains("log")){
			fileName = ConstantConfig.SPARK_LOG4J;
		}

		Pattern pattern = Pattern.compile( "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$" );

		if(pattern.matcher(property).matches()){
			fileName = ConstantConfig.SPARK_SLAVES;
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
				bw.write(line + '\n');
				continue;
			}
			else{
				
				// Property already existed, modify its value
				if(line.contains(property)){
					propExisted = true;
					String next = fileScanner.next();
					//debug
					System.out.println("next: " + next);
					int propertyLength = next.length();
					//String value = line.substring(propertyLength + 1);
					String value = fileScanner.next();
					//debug
					System.out.println("value: " + value);
					// Replace old value
					String tempLine = null;
					tempLine = line.replace(value, " " + newValue );
					bw.write(tempLine + '\n');

				}
				else if(!line.contains(property)){
					System.out.println("original line: " + line.toString());
					//if(fileScanner.next() != "#"){
					String next = fileScanner.next();
					//debug
					//original next should be a property
					System.out.println("original next: " + next);
					int propertyLength = next.length();
					//String value = line.substring(propertyLength + 1);
					String value = fileScanner.next();
					//debug
					System.out.println("original value: " + value);
					bw.write(line + '\n');
					//}

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
		propertyList.put("SPARK_CONF_DIR", "test1");
		propertyList.put("SPARK_LOG_DIR", "test66665");
		propertyList.put("SPARK_IDENT_STRING", "testerere");
		propertyList.put("SPARK_NICENESS", "test1rt");
		propertyList.put("spark.master", "spark://master:7077");
		propertyList.put("spark.eventLog.compress", "qjx19930605");
		ChangeParameter changeParameter = new ChangeParameter();
		changeParameter.modifyConfig(propertyList);
	}
}
