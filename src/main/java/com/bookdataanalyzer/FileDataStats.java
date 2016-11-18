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
  private HashMap<String, Integer> unigramMap;
  
  private int totalUnigrams;
  
  private int totalNouns;
  private int totalVerbs;
  private int totalAdjectives;
  
  public FileDataStats()
  {
    unigramMap = new HashMap<String, Integer>();
  }
  
  public void updateWordAndPOSTagCount(String wordWithPosTag)
  {
    int lastIndexOfUnderscore = wordWithPosTag.lastIndexOf("_");
    String word = wordWithPosTag.substring(0, lastIndexOfUnderscore);
    String posTag = wordWithPosTag.substring(lastIndexOfUnderscore + 1);
    
    if(posTag.equals("JJ") || posTag.equals("JJR") || posTag.equals("JJS"))
      totalAdjectives++;
    else
    if(posTag.startsWith("VB"))
      totalVerbs++;
    else
    if(posTag.equals("NN") || posTag.equals("NNS"))
      totalNouns++;
    
    if(word.matches("[A-Za-z_\\-]+"))
    {
      updatePhraseCountMap(word.toLowerCase(), unigramMap);
      totalUnigrams++;
    }
  }
  
  public HashMap<String, Integer> unigramMap()
  {
    return this.unigramMap;
  }

  public int totalUnigrams()
  {
    return this.totalUnigrams;
  }
  
  public int totalVerbs()
  {
    return this.totalVerbs;
  }
  
  public int totalNouns()
  {
    return this.totalNouns;
  }
  
  public int totalAdjectives()
  {
    return this.totalAdjectives;
  }
 
  /**
   * Dump the content of all the maps for debugging / testing
   */
  public void printInfo()
  {
    System.out.println("\n\nUnigram Total : " + totalUnigrams);
    printInfoHelper(unigramMap);
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
}
