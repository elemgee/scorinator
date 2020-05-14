# Scorinator

This is a command line utility for computing a score for a list of names.

## Assumptions

- The list of files provided includes only first names with no spaces. I stubbed out some configuration paramters
that would allow the input strings to be treated in various ways if the desire was to make this a more generic service.
For instance, if the list might contain spaces, or full names we would need to decide how to handle non-alpha characters
like spaces, commas, hyphens, and other elements. For this exercise, I am assuming that the scoring is 
{A=1, B=2 ... Y=25, Z=26} so
we're scoring this in a case-insensitive manner. To avoid polluting the score with unexpected or unmapped characters, 
I've normalized the input string to uppercase and removed all non alpha characters. That certainly can be adjusted, but
that should allow this process to gracefully (and predictably) handle data other than the very clean test data.
- One of the additional requirements is to allow scoring first and last names also. I'm assuming that since the mapping
only included uppercase alpha, that will still hold. We could do any arbitrary score mapping, but for now, I'm assuming
that we'll only score alpha characters. I'm also assuming that name ordering doesn't matter (first last; last, first, ...).
For the current scoring algorithm, it wouldn't make any difference and the normalization of names could be made to handle
that if order became important.
- I've included tests even though this is a tiny exercise. I struggle like many to improve test coverage but in the 
interest of putting my best foot forward... here you go. I pulled apart the `ScoreRunner` to test for expected exceptions,
but that is just an academic exercise. The focus for the runner was just to give reasonable output for the exercise
and blow up gracefully if someone didn't pass in the right arguments.  
- The  `NameScorer` should be able to be called from a web service passing a `List[String]`. It could also be called
from a wrapper than manages the rules for normalization and scoring based on the type of string.


## Original Instructions

Create a command line utility that will compute a score for a list of first names.

The list of names will be provided as a text file. The full path to the names file will be specified as a command line argument. The names file will contain a single line of quoted, comma-separated names. A small sample of data can be found at the end of this document and a full sample file (names.txt) is attached.

To score a list of names, you must sort it alphabetically and sum the individual scores for all the names. To score a name, sum the alphabetical value of each letter (A=1, B=2, C=3, etc...) and multiply the sum by the name’s position in the list (1-based).

For example, when the sample data below is sorted into alphabetical order, LINDA, which is worth 12 + 9 + 14 + 4 + 1 = 40, is the 4th name in the list. So, LINDA would obtain a score of 40 x 4 = 160. The correct score for the entire list is 3194. The correct score for the attached names.txt file is 871198282.

Your solution should be submitted as source files only – no complied binaries or jar files are to be submitted.  The code should be syntactically correct and compile without errors. You are encouraged to use any common open source Java libraries that you feel will help; however, you must provide a manifest of dependent libraries as one of the source files.

Your code should be written as if it were part of a real company codebase. As such, it should be optimized for readability and maintainability. Also, you are aware of the following up-coming requirements changes that should factor into your design decisions:

* Another department will want to use this utility as well, but they have a much more complex name scoring algorithm.
* This scoring feature will be added to the company's intranet web-app, allowing employees to upload and score files from their desktop.
* The company will be switching from first names only to both first and last names in the file.

**Sample names file data**:
```csv
"MARY","PATRICIA","LINDA","BARBARA","VINCENZO","SHON","LYNWOOD","JERE","HAI"
```

### Version History

<<<<<<< HEAD

=======
**2020-05-13**: MVP simple scorer
>>>>>>> stubs for initial thoughts and documentation of expectations
