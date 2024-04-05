# Java File Analysis Tool

This tool is designed to analyze Java files and provide both static and dynamic analysis, along with a visualization of the code structure.

## How to Run the Tool

### Backend

To run the backend analysis, use the following command:
`./gradlew run`
This will execute the static and dynamic analysis processes.

### Frontend

To run the frontend visualization, use the following command:
`yarn start`

This will start the development server and open the visualization in your default web browser.

### User Flow
The user should begin by uploading the Java file that they want to analyze. After uploading it, a control flow graph (CFG) representation of the 
program will be statically generated on the webpage. 

Next, the user can hit the "execute" button, which runs the dynamic analysis. This assumes a main method is present in the file. 
If there are compilation or runtime errors in your Java file, an error will appear alerting the user of the issue. If
execution was successful, the dynamic analysis information will be appended to each node of the CFG.

## Interpreting the Visualization

The visualization represents a control flow graph of the analyzed Java file's main method, appended with runtime analysis of each node. 
It consists of the following components:

*DETAILS ON FRONTEND HERE*

## Static Analysis Details

The static analysis component performs the following tasks:

*DETAILS ON STATIC FLOW HERE*
## Dynamic Analysis Details

The dynamic analysis component executes the analyzed code and collects runtime information, including:

*DETAILS ON DYNAMIC FLOW HERE*

The dynamic analysis results are integrated with the static analysis data to provide a more comprehensive understanding of the code's behavior and performance characteristics.