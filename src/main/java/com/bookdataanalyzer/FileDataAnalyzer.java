package com.bookdataanalyzer;

import java.io.*;
import java.util.*;

/**
 * Analyzes the text in the given file and produces an instance of FileDataStats that contains the information of interest 
 * in the current file.
 * @author nirav99
 *
 */
public class FileDataAnalyzer
{
  private FileDataStats fileDataStats;
  
  private String[] lastTwoWords = null;

  public FileDataAnalyzer(File inputFile) throws IOException
  {
    fileDataStats = new FileDataStats();
    lastTwoWords = new String[2];
    
    processFile(inputFile);
  }
  
  /**
   * Returns the count of the metrics collected from this file
   */
  public FileDataStats fileDataStats()
  {
    return this.fileDataStats;
  }
  
  /**
   * Analyzes the given data file
   * @param inputFile
   * @throws IOException
   */
  private void processFile(File inputFile) throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
    
    String line = null;
    
    while((line = reader.readLine()) != null)
    {
      processLine(line);
    }
    reader.close();
    fileDataStats.truncateLowFrequencyValues();
  }
  
  private void processLine(String line)
  {
    String processedLine = removePunctuations(line);
    
    String[] words = processedLine.split("\\s+");
    
    for(int i = 0; i < words.length; i++)
    {
      fileDataStats.updateNGramCount(words[i]); // Update unigram count
      
      if(i < words.length - 1) // Update bigram count
        fileDataStats.updateNGramCount(words[i] + " " + words[i + 1]);
      
      if(i < words.length - 2) // Update trigram count
        fileDataStats.updateNGramCount(words[i] + " " + words[i + 1]+ " " + words[i + 2]);
    }
    
    if(words.length > 0)
    {
      // Use the last 2 words from previous line to build 2-gram and 3-gram and update their corresponding counts
      if(!isWordNullOrEmpty(lastTwoWords[1]))
        fileDataStats.updateNGramCount(lastTwoWords[1] + " " + words[0]);

      if(!isWordNullOrEmpty(lastTwoWords[1]) && !isWordNullOrEmpty(lastTwoWords[0]))
        fileDataStats.updateNGramCount(lastTwoWords[0] + " " + lastTwoWords[1] + " "+ words[0]);
    
      // Update the last two words of this line to generate the n-grams for the next line
      lastTwoWords[0] = (words.length >= 2) ? words[words.length - 2] : null;
      lastTwoWords[1] = (words.length >= 1) ? words[words.length - 1] : null;
    }
    else
    {
      lastTwoWords[0] = null;
      lastTwoWords[1] = null;
    }
  }
  
  /**
   * Removes punctuation from the line
   * @param content
   * @return
   */
  private String removePunctuations(String content)
  { 
    String newContent = content.replaceAll("[\\?;:<>!#\\(\\)\\[\\]\\{\\}\"^~/\\.,]+", " ");
    newContent = newContent.replaceAll("\\s+", " ").toLowerCase();
    return newContent;
  }
  
  private boolean isWordNullOrEmpty(String word)
  {
    return word == null || word.trim().isEmpty();
  }
}
