package com.bookdataanalyzer;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Encapsulates the metrics for a single author
 * @author nirav99
 *
 */
public class AuthorMetrics
{
  private String authorName;
  
  private HashMap<String, Integer> unigramMap;
	  
  private int totalUnigrams; // Also total number of words for each author
  
  private int totalNouns;
  private int totalVerbs;
  private int totalAdjectives;
  
  public AuthorMetrics(String authorName)
  {
    this.authorName = authorName;
    unigramMap = new HashMap<String, Integer>();
  }
  
  /**
   * Aggregate the metrics from different files
   * @param fileDataStats
   */
  public void updateMetrics(FileDataStats fileDataStats)
  {
    totalUnigrams += fileDataStats.totalUnigrams();
    
    totalNouns += fileDataStats.totalNouns();
    totalVerbs += fileDataStats.totalVerbs();
    totalAdjectives += fileDataStats.totalAdjectives();
    
    updateMetricsHelper(fileDataStats.unigramMap(), this.unigramMap);
  }
  
  public int totalWords()
  {
    return totalUnigrams;
  }
  
  public int totalNouns()
  {
    return totalNouns;
  }
  
  public int totalVerbs()
  {
    return totalVerbs;
  }
  
  public int totalAdjectives()
  {
    return totalAdjectives;
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
    System.out.println("Total Nouns : " + totalNouns + " Percentage = " + 1.0 * totalNouns / totalUnigrams * 100.0);
    System.out.println("Total Verbs : " + totalNouns + " Percentage = " + 1.0 * totalVerbs / totalUnigrams * 100.0);
    System.out.println("Total Adjectives : " + totalAdjectives + " Percentage = " + 1.0 * totalAdjectives / totalUnigrams * 100.0);
    
    System.out.println("\n\nUnigram Total : " + totalUnigrams);
    printInfoHelper(unigramMap);
  }
  
  public void printInfo(PrintStream ps)
  {
    ps.println("Author name : " + authorName);
    ps.println("Total Nouns : " + totalNouns + " Percentage = " + 1.0 * totalNouns / totalUnigrams * 100.0);
    ps.println("Total Verbs : " + totalNouns + " Percentage = " + 1.0 * totalVerbs / totalUnigrams * 100.0);
    ps.println("Total Adjectives : " + totalAdjectives + " Percentage = " + 1.0 * totalAdjectives / totalUnigrams * 100.0);

	ps.println("\n\nUnigram Total : " + totalUnigrams);
	printInfoHelper(unigramMap, ps);
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
  
  /**
   * Truncate low frequency values
   */
  public void truncateLowFrequencyValues()
  {
    truncateLowFrequencyValues(unigramMap, totalUnigrams);
  }
  
  /**
   * Helper method for truncateLowFrequencyValues - removes words with frequency < 0.1%
   * @param phraseMap
   * @param mapType
   */
  private void truncateLowFrequencyValues(HashMap<String, Integer> phraseMap, int total)
  {
    Iterator<Entry<String, Integer>> iter = phraseMap.entrySet().iterator();
    
    int value;
    Entry<String, Integer> entry;
    
    while(iter.hasNext())
    {
      entry = iter.next();
      value = entry.getValue();
      
      if(1.0 * value / total * 100.0 <= 0.1)
        iter.remove();
    }
  }
}
