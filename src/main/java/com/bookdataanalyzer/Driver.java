package com.bookdataanalyzer;

import java.io.*;
import java.util.*;

public class Driver
{
  private File inputDir;
  private File outputDir;
  
  private HashMap<String, AuthorMetrics> authorMetricsMap;
  
  public Driver(File inputDir, File outputDir)
  {
    this.inputDir = inputDir;
    this.outputDir = outputDir;
    
    authorMetricsMap = new HashMap<String, AuthorMetrics>();
    
    String[] fileList = inputDir.list();
    
    for(String fileName : fileList)
    {
      if(fileName.endsWith(".txt"))
      {
        processFile(fileName);
      }
    }
  }
  
  public void showInformation()
  {
    File outputFile = null;
    
    Set<String> keySet = authorMetricsMap.keySet();
    
    for(String key : keySet)
    {
      try
      {
        outputFile = new File(outputDir.getAbsolutePath() + File.separator + key + "_analysis.txt");
        System.out.println("Writing file : " + outputFile.getAbsolutePath());
        
        if(false == outputFile.createNewFile())
        {
          outputFile.delete();
          outputFile.createNewFile();
        }
        
        FileOutputStream fos = new FileOutputStream(outputFile);
        PrintStream ps = new PrintStream(fos);
        
        AuthorMetrics authorMetrics = authorMetricsMap.get(key);
        authorMetrics.printInfo(ps);
        ps.flush();
        ps.close();
      }
      catch(Exception e)
      {
        System.err.println(e.getMessage());
        e.printStackTrace();
      }
    }
    
  }
  
  private void processFile(String fileName)
  {
    System.out.println("Processing file : " + fileName);
    
    // Assumption is that file name starts with author name followed by underscore
    int indexOfUnderscore = fileName.indexOf("_");
    
    String authorName = (indexOfUnderscore >= 0) ? fileName.substring(0,  indexOfUnderscore) : null;

    File inputFile = new File(this.inputDir.getAbsolutePath() + File.separator + fileName);
    
    try
    {
      FileDataAnalyzer fileDataAnalyzer = new FileDataAnalyzer(inputFile);
      FileDataStats fileDataStats = fileDataAnalyzer.fileDataStats();
      
      if(authorName != null)
      {
        AuthorMetrics authorMetrics = authorMetricsMap.get(authorName);
        
        if(authorMetrics == null)
          authorMetrics = new AuthorMetrics(authorName);
        authorMetrics.updateMetrics(fileDataStats);
        
        authorMetricsMap.put(authorName, authorMetrics);
      }
	}
    catch (IOException e)
    {
      System.out.println("Error while processing file : " + fileName + "\n" + e.getMessage());
      e.printStackTrace();
	}
  }
  
  public static void main(String[] args)
  {
    try
    {
      File inputFile = new File("/Users/nirav99/Documents/JavaPrograms/BookDataAnalyzer/data/");
      File outputFile = new File("/Users/nirav99/Documents/JavaPrograms/BookDataAnalyzer/data/analysis");
      Driver driver = new Driver(inputFile, outputFile);
      driver.showInformation();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
