package com.bookdataanalyzer;

import java.io.*;

public class Driver
{
  private File inputDir;
  private File outputDir;
  
  public Driver(File inputDir, File outputDir)
  { 
    this.inputDir = inputDir;
    this.outputDir = outputDir;
    
    if(!this.inputDir.exists() || !this.inputDir.isDirectory())
      throw new IllegalArgumentException("Specified input directory is not valid");
    if(!this.outputDir.exists() || !this.outputDir.isDirectory())
      throw new IllegalArgumentException("Specified output directory is not valid");
  }
  
  /**
   * Process every text file in the input directory
   */
  public void processFiles()
  {
    CollectiveMetrics collectiveMetrics = new CollectiveMetrics();
    
    String[] dataFiles = inputDir.list();
    
    for(String fileName : dataFiles)
    {
      if(fileName.endsWith(".txt"))
        collectiveMetrics.processFile(inputDir, fileName);
    }
    
    collectiveMetrics.writeSummary(new File(outputDir.getAbsolutePath() + File.separator + "summary.csv"));
    collectiveMetrics.writeDetailedInformation(outputDir);
  }
  
  public static void main(String[] args)
  {
    try
    {
      if(args == null || args.length != 2 || args[0].toLowerCase().contains("help"))
      {
        printUsage();
        return;
      }
      Driver driver = new Driver(new File(args[0]), new File(args[1]));
      driver.processFiles();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  private static void printUsage()
  {
    System.err.println("Usage");
    System.err.println("Provide 2 parameters");
    System.err.println("InputDirectory - directory having data files to analyze");
    System.err.println("OutputDirectory - directory where to write analysis results");
  }
}
