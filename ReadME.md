Building the code
-----------------

Building the jar requires gradle. To build using gradle, navigate to the project directory and give the command

```
gradle jar
```

Running the code
-----------------

Use the command

```java -Xmx2g -jar BookDataAnalyzer.jar {inputdir} {outputdir} ```

Where inputdir represents the data directory where the provided data files are present in, and the outputdir represents the
directory where to write the analysis results.

Understanding the results
-------------------------

In the output directory, one file per author is produced. This file contains frequency of every word used by that author and
the number of nouns, verbs and adjectives used by that author.

A short summary comparing all the authors is present in a file called summary.csv.

A conclusion of this study can be found in a file name conclusion.txt