package com.bookdataanalyzer;

import java.io.*;
import java.util.*;

/**
 * Metrics collected for every author
 * @author nirav99
 *
 */
public class CollectiveMetrics
{
  private HashMap<String, AuthorMetrics> authorMetricsMap;
  
  public CollectiveMetrics()
  {
    authorMetricsMap = new HashMap<String, AuthorMetrics>();
  }
  
  public void processFile(File inputDir, String fileName)
  {
    System.out.println("Processing file : " + fileName);
    
    // Assumption is that file name starts with author name followed by underscore
    int indexOfUnderscore = fileName.indexOf("_");
    
    String authorName = (indexOfUnderscore >= 0) ? fileName.substring(0,  indexOfUnderscore) : null;

    File inputFile = new File(inputDir.getAbsolutePath() + File.separator + fileName);
    
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
  
  /**
   * Write a file that contains the summary of the percentage of different types of words per author
   * @param summaryFile
   */
  public void writeSummary(File summaryFile)
  {
    try
    {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(summaryFile), "UTF-8"));
      writer.write("Author,PercentNouns,PercentVerbs,PercentAdjactives");
      writer.newLine();
      Set<String> keySet = authorMetricsMap.keySet();
      AuthorMetrics authorMetrics;
      
      double percentNouns;
      double percentVerbs;
      double percentAdjectives;
      
      for(String key : keySet)
      {
        authorMetrics = authorMetricsMap.get(key);
        percentNouns = 1.0 * authorMetrics.totalNouns() / authorMetrics.totalWords() * 100;
        percentVerbs = 1.0 * authorMetrics.totalVerbs() / authorMetrics.totalWords() * 100;
        percentAdjectives = 1.0 * authorMetrics.totalAdjectives() / authorMetrics.totalWords() * 100;
        
        writer.write(key + "," + String.format("%.2f", percentNouns) + "," + 
        String.format("%.2f", percentVerbs) + "," + String.format("%.2f", percentAdjectives));
        writer.newLine();
      }
      writer.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * Generate a file per author that contains more detailed information about the data collected for that author.
   * @param outputDir
   */
  public void writeDetailedInformation(File outputDir)
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
        authorMetrics.truncateLowFrequencyValues();
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
}