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

This will start the development server and visualization at **localhost:3000** in your default web browser.

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

The static analysis component of the Java File Analysis Tool focuses on analyzing the structure and behavior of the provided Java code through the following tasks:
### Control Flow Graph (CFG) Generation: 
For each method in the Java file, the tool constructs a CFG where nodes represent statements or blocks of code, and edges represent the flow of control. The CFG is crucial for understanding the program's structure and is used as a basis for further analysis. The construction of the CFG follows these principles:
- 	Entry and Exit Nodes: Each CFG has an entry node at the beginning and an exit node at the end to represent the start and end of the method's execution.
- Statement Nodes: Each executable statement in the method is represented as a node in the CFG.
- Branch Nodes: Conditional statements like if-else create branch nodes with edges leading to different parts of the CFG based on the condition's outcome.
-	Loop Nodes: Loops are represented with nodes for the loop condition and body, with edges that create a cycle to represent the repetitive execution.
### Variable Analysis: 
The tool performs variable analysis on the CFG to identify various issues related to variable usage:
-	Duplicate Declarations: Detects cases where a variable is declared more than once within the same scope or nested scopes, which is not allowed in Java.
-	Undeclared Variable Assignment: Checks if a value is assigned to a variable that has not been declared. This analysis ensures that the variable must be declared in the same or higher scope for the assignment to be considered valid. It does not check for the use of undeclared variables but focuses on assignment operations.
-	Unused Variables: Identifies variables that are declared but never used in the code. This analysis is optimistic, meaning that a variable is considered used if it appears in any branch of a conditional statement or loop, even if there are paths where it is not used.
-	The variable analysis is implemented primarily in the VariableAnalyzer class, which iterates through the statements in the CFG and applies the above checks.
### Branch Analysis: 
The tool analyzes various types of branching constructs to understand the different paths the program execution might take. Supported branching constructs include:
-	If-Then-Else Statements: Analyzes conditional branching and creates separate paths in the CFG for the true and false branches. If both the IF-THEN and IF-ELSE branches end with return statements, then any code downstream of the IF statement is not included as it becomes unreachable.
-	For and While Loops: Represents loops in the CFG, including the loop condition and body. The tool also handles nested loops and break statements within loops.
-	Break Statements: Specifically handles break statements that are nested inside IF statements. Break statements are represented in the CFG as edges that lead out of the loop structure.

The results of the static analysis are used to create a detailed representation of the code's structure, which is then visualized on the frontend. This visualization helps users comprehend the control flow, variable usage, and branching behavior in their code.

## Dynamic Analysis Details

The dynamic analysis component executes the analyzed code and collects runtime information, including:

*DETAILS ON DYNAMIC FLOW HERE*

The dynamic analysis results are integrated with the static analysis data to provide a more comprehensive understanding of the code's behavior and performance characteristics.