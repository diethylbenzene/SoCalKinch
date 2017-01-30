#SoCalKinch
A Flexible KinchRanks system.

###[For More Info on KinchRanks, click here.](http://www.kinch2002.com/kinchranks/)

## About
I developed this program to facilitate generating KinchRanks for the SoCal area, but have since adapted it for use with certain regions.

SoCalKinch takes a list of WCA IDs from a `.txt` file, separated by newlines.

Example:

```
2009ZEMD01
2007VALK01
2008CHOI04
```

It then calculates each competitor's KinchRanks from either the best results it finds (for regional rankings), or the WRs. It will then output it to a `.csv` file.

## Prerequisites
Java 1.8

OpenCSV 3.8

## Usage

Double-click for the GUI version.

For the non-GUI version:

`java -jar socalkinch.jar [WCA ID file path] [relativeOrWR (true or false)] [cleanOnExit(true or false)] [Output .csv file path]`
