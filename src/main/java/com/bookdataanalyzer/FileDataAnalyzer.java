package com.bookdataanalyzer;

import java.io.*;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Analyzes the text in the given file and produces an instance of FileDataStats that contains the information of interest 
 * in the current file.
 * @author nirav99
 *
 */
public class FileDataAnalyzer
{
  private FileDataStats fileDataStats;
  
  private static MaxentTagger posTagger;
  
  static
  {
    posTagger = new MaxentTagger("english-left3words-distsim.tagger");
  }
  
  public FileDataAnalyzer(File inputFile) throws IOException
  {
    fileDataStats = new FileDataStats();
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
    StringBuilder content = new StringBuilder();
    
    while((line = reader.readLine()) != null)
    {
      content.append(line).append("\n");
    }
    reader.close();
    
    String textWithoutPunctuations = removePunctuations(content.toString());
    
    String posTaggedText = posTagger.tagString(textWithoutPunctuations);
    String[] posTaggedWords = posTaggedText.split("\\s+");
    
    for(String posTaggedWord : posTaggedWords)
      fileDataStats.updateWordAndPOSTagCount(posTaggedWord);
  }
  
  /**
   * Removes punctuation from the line
   * @param content
   * @return
   */
  private String removePunctuations(String content)
  { 
    String newContent = content.replaceAll("[\\?;:<>!#\\(\\)\\[\\]\\{\\}\"^~/\\.,]+", " ");
    newContent = newContent.replaceAll("\\s+", " ");
    return newContent;
  }
}
