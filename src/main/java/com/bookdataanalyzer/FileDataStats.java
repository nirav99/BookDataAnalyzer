package com.bookdataanalyzer;

import java.util.*;
import java.util.Map.Entry;

/**
 * Data of interest collected from a single text file (a single book)
 * @author nirav99
 *
 */
public class FileDataStats
{
  private HashMap<String, Integer> trigramMap;
  private HashMap<String, Integer> bigramMap;
  private HashMap<String, Integer> unigramMap;
  
  private int totalUnigrams;
  private int totalBigrams;
  private int totalTrigrams;
  
  public FileDataStats()
  {
    trigramMap = new HashMap<String, Integer>();
    bigramMap = new HashMap<String, Integer>();
    unigramMap = new HashMap<String, Integer>();
  }
  
  /**
   * Updates the corresponding counter for the given n-gram
   * @param nGram
   */
  public void updateNGramCount(String nGram)
  {
    if(nGram != null)
    {
      nGram = nGram.toLowerCase();
    
      int numWords = nGram.split("\\s+").length;

      if(numWords == 1)
      {
        updatePhraseCountMap(nGram, unigramMap);
        totalUnigrams++;
      }
      else
      if(numWords == 2)
      {
        updatePhraseCountMap(nGram, bigramMap);
        totalBigrams++;
      }
      else
      if(numWords == 3)
      {
        updatePhraseCountMap(nGram, trigramMap);
        totalTrigrams++;
      }
    }
  }
  
  public HashMap<String, Integer> unigramMap()
  {
    return this.unigramMap;
  }
  
  public HashMap<String, Integer> bigramMap()
  {
    return this.bigramMap;
  }
  
  public HashMap<String, Integer> trigramMap()
  {
    return this.trigramMap;
  }

  public int totalUnigrams()
  {
    return this.totalUnigrams;
  }
  
  public int totalBigrams()
  {
    return this.totalBigrams;
  }
  
  public int totalTrigrams()
  {
    return this.totalTrigrams;
  }
  
  /**
   * Dump the content of all the maps for debugging / testing
   */
  public void printInfo()
  {
    System.out.println("\n\nUnigram Total : " + totalUnigrams);
    printInfoHelper(unigramMap);
    System.out.println("\n\nBigram Total : " + totalBigrams);
    printInfoHelper(bigramMap);
    System.out.println("\n\nTrigram Total : " + totalTrigrams);
    printInfoHelper(trigramMap);
  }
  
  private void printInfoHelper(HashMap<String, Integer> phraseMap)
  {
    Set<Entry<String, Integer>> entrySet = phraseMap.entrySet();
    
    for(Entry<String, Integer> entry : entrySet)
      System.out.println(entry.getKey() + " --> " + entry.getValue());
  }
  
  private void updatePhraseCountMap(String phrase, HashMap<String, Integer> phraseMap)
  {
    Integer value = phraseMap.get(phrase);
	    
    if(value == null)
      value = 0;
    
    value = value + 1;
    
    phraseMap.put(phrase, value);  
  }
  
  /**
   * Truncate low frequency values
   */
  public void truncateLowFrequencyValues()
  {
    truncateLowFrequencyValues(unigramMap, "unigram");
    truncateLowFrequencyValues(bigramMap, "bigram");
    truncateLowFrequencyValues(trigramMap, "trigram");
  }
  
  /**
   * Helper method for truncateLowFrequencyValuess
   * @param phraseMap
   * @param mapType
   */
  private void truncateLowFrequencyValues(HashMap<String, Integer> phraseMap, String mapType)
  {
    int total = 0;
    
    if(mapType.equals("unigram"))
      total = totalUnigrams;
    else
    if(mapType.equals("bigram"))
      total = totalBigrams;
    else
    if(mapType.equals("trigram"))
      total = totalTrigrams;
    
    Iterator<Entry<String, Integer>> iter = phraseMap.entrySet().iterator();
    
    int value;
    Entry<String, Integer> entry;
    
    while(iter.hasNext())
    {
      entry = iter.next();
      value = entry.getValue();
      
      if(value <= 1)
        iter.remove();
    }
  }
}
