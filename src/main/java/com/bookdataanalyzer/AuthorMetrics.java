package com.bookdataanalyzer;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Encapsulates the metrics for each author
 * @author nirav99
 *
 */
public class AuthorMetrics
{
  private String authorName;
  
  private HashMap<String, Integer> trigramMap;
  private HashMap<String, Integer> bigramMap;
  private HashMap<String, Integer> unigramMap;
	  
  private int totalUnigrams;
  private int totalBigrams;
  private int totalTrigrams;
  
  public AuthorMetrics(String authorName)
  {
    this.authorName = authorName;
    trigramMap = new HashMap<String, Integer>();
    bigramMap = new HashMap<String, Integer>();
    unigramMap = new HashMap<String, Integer>();
  }
  
  /**
   * Aggregate the metrics from different files
   * @param fileDataStats
   */
  public void updateMetrics(FileDataStats fileDataStats)
  {
    totalUnigrams += fileDataStats.totalUnigrams();
    totalBigrams += fileDataStats.totalBigrams();
    totalTrigrams += fileDataStats.totalTrigrams();
    
    updateMetricsHelper(fileDataStats.unigramMap(), this.unigramMap);
    updateMetricsHelper(fileDataStats.bigramMap(), this.bigramMap);
    updateMetricsHelper(fileDataStats.trigramMap(), this.trigramMap);
  }
  
  /**
   * Helper method to update the map
   * @param sourceMap
   * @param destMap
   */
  private void updateMetricsHelper(HashMap<String, Integer> sourceMap, HashMap<String, Integer> destMap)
  {
    Set<Entry<String, Integer>> sourceEntrySet = sourceMap.entrySet();
    
    Integer destValue = null;
    
    for(Entry<String, Integer> sourceEntry : sourceEntrySet)
    {
      destValue = destMap.get(sourceEntry.getKey());
      
      destValue = (destValue == null) ? sourceEntry.getValue() : sourceEntry.getValue() + destValue;
      
      destMap.put(sourceEntry.getKey(), destValue);
    }
  }
  
  /**
   * Dump the content of all the maps for debugging / testing
   */
  public void printInfo()
  {
    System.out.println("Author name : " + authorName);
    System.out.println("\n\nUnigram Total : " + totalUnigrams);
    printInfoHelper(unigramMap);
    System.out.println("\n\nBigram Total : " + totalBigrams);
    printInfoHelper(bigramMap);
    System.out.println("\n\nTrigram Total : " + totalTrigrams);
    printInfoHelper(trigramMap);
  }
  
  public void printInfo(PrintStream ps)
  {
    ps.println("Author name : " + authorName);
	ps.println("\n\nUnigram Total : " + totalUnigrams);
	printInfoHelper(unigramMap, ps);
	ps.println("\n\nBigram Total : " + totalBigrams);
    printInfoHelper(bigramMap, ps);
    ps.println("\n\nTrigram Total : " + totalTrigrams);
    printInfoHelper(trigramMap, ps);
  }
  
  private void printInfoHelper(HashMap<String, Integer> phraseMap)
  {
    Set<Entry<String, Integer>> entrySet = phraseMap.entrySet();
    
    for(Entry<String, Integer> entry : entrySet)
      System.out.println(entry.getKey() + " --> " + entry.getValue());
  }
  
  private void printInfoHelper(HashMap<String, Integer> phraseMap, PrintStream ps)
  {
    Set<Entry<String, Integer>> entrySet = phraseMap.entrySet();
    
    for(Entry<String, Integer> entry : entrySet)
      ps.println(entry.getKey() + " --> " + entry.getValue());
  }
}
