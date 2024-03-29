Use this file to commit information clearly documenting your check-ins' content. If you want to store more information/details besides what's required for the check-ins that's fine too. Make sure that your TA has had a chance to sign off on your check-in each week (before the deadline); typically you should discuss your material with them before finalizing it here.

# Project 2 - Check-In 5

## Status of final user study; any feedback and changes planned:
Made a paper mock-up of the tool and ran user studies to gather feedback. Users found the tool helpful for understanding code flow but suggested improvements in the visualization for better clarity. We plan to refine the graph visualization based on user feedback, such as adding color coding for different types of nodes and attaching analysis results as an expandable pop-up for each node instead of only at the end.
 
## Plans for final video (possible draft version): 
Outline the structure of the video, including an introduction to the tool, a demonstration of its features, and a summary of user feedback and future improvements. The video should focus on the tool's functionality and how it can be used to analyze Java code. Create a draft version of the video for review and feedback from the team.
 
## Planned timeline for the remaining days:
Apr 5, Check-in 6: Finalize the integration between the backend analysis tool and the frontend visualization. Conduct thorough testing of the analysis tool and visualization with various code examples to ensure accuracy and usability. Document the usage instructions and limitations of the tool, and prepare for the final presentation.
Apr 12, Final Presentation: Present the completed tool, demonstrate its features, and discuss the feedback received from users. Highlight the team's accomplishments and future directions for the project.
 
## Progress against the timeline planned for your team, including the specific goals you defined (originally as part of Check-in 2) for Check-in 5:
- CFG: Extended the CFGBuilder tool to handle more complex code structures such as Breaks and nested breaks as well as converting the graph into a format needed for frontend graph visualization library.
- Analysis: Incorporated dynamic analysis components. Integrating the CFGBuilder class with the Analyzer class as a singleton, and added logging of dynamically gathered data in the graph data structure.
- Frontend: Developing interaction elements for the frontend, allowing users to navigate and explore the control-flow graph.
- API: Integrated the backend CFGBuilder tool with the frontend so that the Java file sent by the frontend is now processed by backend and the graph file sent to frontend as a JSON. We are on track with the planned timeline, with all goals for Check-in 5 met.
- Tests: Added tests for CFG builder.

----------------------------------------

# Project 2 - Check-In 4

```C
+-------------------------------------------------------+
|                                                       |
|                        Front End                      |
|                                                       |
+-------------------------------------------------------+
        |           ^                       ^
        |           |                       |
        |           |                       |
        | Project   |                       | Result
        | Info      |                       |
        v           |                       |
+----------------------+        +-----------------------+
|                      |        |                       |
|  Graph Generation    | -----> |       Analysis        |
|                      |        |                       |
+----------------------+        +-----------------------+
            |                               ^
            | Place info                    | Log
            v                               |
+--------------------------+                |
|    Code Injection        |----------------+
|       Execution          |
+--------------------------+
```


## Status of implementation so far.
- **Frontend:** Started implementing base functionality to allow users to upload Java program files 
- **API:** Designed API to accept zip files and have begun implementing/testing it
API design to expose graph to frontend depends on the backend implementation of the graph, which is still in progress
- **Custom CFG:** Implemented structure of CFG and nodes.
Implemented CFGBuilder. This makes Control Flow Graphs for all methods in the given file. It handles branching arising out of IF, While-loop, For-loop, and Return statements.

## Plans for final user study.
- Run the MVP tool for the users and get their feedback.
- See which metrics the users think are helpful and which could be changed/added

## Planned timeline for the remaining days.
- Mar 29, Check-in 5
Extend the analysis tool to handle more complex code structures and incorporate the additional analysis component.
Develop the interaction elements for the frontend, allowing users to navigate and explore the control-flow graph.
Start integrating the backend analysis tool with the frontend visualization.
- Apr 5, Check-in 6
Finalize the integration between the backend analysis tool and the frontend visualization. 
Conduct thorough testing of the analysis tool and visualization with various code examples to ensure accuracy and usability.
Document the usage instructions and limitations of the tool, and prepare for the final presentation.

## Progress against the timeline planned for your team, including the specific goals you defined (originally as part of Check-in 2) for Check-in 4; any revisions to Check-in 5 goals.
We are on track since we met all the goals set for check-in 4 which were:  
- Initialize the repository and set up the development environment for the backend and frontend. (1 person: Backend developer, 1 person: Frontend developer)
- Implement a basic version of the analysis tool that can generate a control-flow graph for simple code examples. (2 people: Backend developers)
- Design the initial layout and structure of the graph visualization in the React frontend. (1 person: Frontend developer)

----------------------------------------

# Project 2 - Check-In 3
## Mockup of how your project is planned to operate (as used for your first user study). Include any sketches/examples/scenarios.
<img src="/controlFlowDiagram.jpg" height="600">

<img src="/performanceAnalysisReport.jpg" height="600">

## Notes about first user study results.
Looking at the mock-up shown in the earlier section, one of the feedback received was that it was unclear what the different colors of the blocks were signifying. At this stage, we haven't decided how we want to use colouring to convey information but this is something we plan to look into. An early idea is to use a gradient from low to dark with dark signifying higher system resources use and room for optimization. 
There was additional confusion regarding the extent of the call graph, particularly regarding the inclusion of programming functionalities like exceptions, retries, and recursion. Since we are still determining the full scope of our analysis, it is unclear whether or not these features will be included. However, we will keep them in mind when finalizing the scope. 
The user also had some suggestions on how we could potentially expand our analysis. One idea was to include information about the program state alongside the call graph, akin to the functionality provided by a debugger. This would help the programmer visualize a particular input as it moved through the program. Another suggestion was to allow for the comparison of GCFGs across different commits so developers could analyze the effects their changes had. Again, when finalizing the scope of our analysis, we will make sure to keep these suggestions in mind. 

## Any changes to original design.
Original design was a call graph, which was changed to a global control-flow graph (GCFG).
We wanted to add further useful information to our analysis and also to our  visualization, so we decided to add dynamic analysis which highlights different code blocks in our GCFG with information about potentially problematic areas of code or code which can be investigated from an optimization perspective. The additional measures we will be highlighting are execution time, number of times run, memory usage, CPU usage etc.
A new challenge from these changes is creating the global control flow graph, which may require some more complex calculation.


## Progress against the timeline planned for your team, including the specific goals you defined (as part of Check-in 2) for Check-in 3; any revisions to future Check-in goals.
In check-in 2, we expected us to meet the following targets for check-in 3:
#### *1. Decide on the additional analysis component to complement the control-flow graph (e.g., data-flow analysis, complexity metrics, etc.). (Entire team)*

We have met this goal and come up with ideas for additional analysis (mentioned above) to complement our control-flow graph.

#### *2. Prepare code examples that demonstrate different control structures and function calls to be used for testing the analysis tool. (1 person: Backend developer)*

Since we were still working on finalizing our idea and a back-and-forth exchange of ideas with the TA we did not get to a stage where we can implement it and have code example ready. We did decide that we will be working with JAVA and have set up the repo with the boiler-plate code needed to start working.

#### *3. Test the selected libraries and tools to ensure they meet the requirements for generating and analyzing the control-flow graph. (1 person: Backend developer)*

We have found some useful libraries: Spoon and JavaParser for generating the AST, Byte Buddy for profiling the program during exection

----------------------------------------

# Project 2 - Check-In 2
## Brief description of your planned program analysis (and visualization, if applicable) ideas.
- Given a code, we can generate a graph connected by edges that shows the execution flow.
- Includes branches for for/while loops and if statements
```
    +------+
    | main |
    +------+
       |
       | calls
       v
   +-----------+
   | factorial |
   +-----------+
       ^  |      
       |  | recursive
       |  | calls
       |  v
   +-----------+
   | factorial |
   +-----------+
       .
       .
       .
```
## Notes of any important changes/feedback from TA discussion.
- The first idea isn’t very good, it doesn’t involve too much analysis
- TA said that a call-graph/control flow graph satisfies the analysis requirement of the project, however, it would be nice to add an additional analysis - component if possible.
- We don’t necessarily need a visual component given that we satisfy the other two requirements of the project (static program analysis, applies to real-world programming language), but it would be nice to have a visualization component since it makes sense to have a visual component with a call-graph output.
- We should decide on a topic and come up with some examples+code snippets for next week’s meeting.
## Any planned follow-up tasks or features still to design. 
- Browse for tools/libraries to traverse code execution and assist with:
  - Inside a function, how to analysis the execution path
  - Drill down into what the analysis will be.
  - Find call dependencies in different functions
  - Detect code smells such as low cohesion+high coupling as a stretch goal for additional analysis drawing from the control-flow graph.

## Planned division of main responsibilities between team members.
- Backend: generate the graph data structure (or API)
  - One person to use the tool to traverse the code and translate to DS
  - Two people to work with the provided DS to extract analysis information
- Frontend: show the data structure, design interaction elements
  - One person to design layout and structure of the graph visual using REACT
(OPTIONAL: 1 person to design and test the API and help parse propvided JSON object appropriately) This person can also help out in other places as needed

## Summary of progress so far.
- We have decided to use JAVA for backend and found the necessary libraries and tools to help meet the project goals.
- Decided to use control-flow graph as the basis of our analysis which narrows the analysis possibilities down to what can be achieved in the scope of a control-flow diagram. 
- Decided on a REACT frontend that reacts with an API with JAVA backend.

## Roadmap for what should be done when, including specific goals for completion by future Milestones (propose at least three such goals per future Milestone, along with who will work on them; you can revise these later as needed).
### Mar 15, Check-in 3
- Decide on the additional analysis component to complement the control-flow graph (e.g., data-flow analysis, complexity metrics, etc.). (Entire team) 
- Prepare code examples that demonstrate different control structures and function calls to be used for testing the analysis tool. (1 person: Backend developer) 
- Test the selected libraries and tools to ensure they meet the requirements for generating and analyzing the control-flow graph. (1 person: Backend developer)
### Mar 22, Check-in 4
- Initialize the repository and set up the development environment for the backend and frontend. (1 person: Backend developer, 1 person: Frontend developer) 
- Implement a basic version of the analysis tool that can generate a control-flow graph for simple code examples. (2 people: Backend developers) 
- Design the initial layout and structure of the graph visualization in the React frontend. (1 person: Frontend developer)
### Mar 29, Check-in 5
- Extend the analysis tool to handle more complex code structures and incorporate the additional analysis component. (2 people: Backend developers) 
- Develop the interaction elements for the frontend, allowing users to navigate and explore the control-flow graph. (1 person: Frontend developer) 
- Start integrating the backend analysis tool with the frontend visualization. (1 person: Backend developer, 1 person: Frontend developer)
### Apr 5, Check-in 6
- Finalize the integration between the backend analysis tool and the frontend visualization. (1 person: Backend developer, 1 person: Frontend developer) 
- Conduct thorough testing of the analysis tool and visualization with various code examples to ensure accuracy and usability. (Entire team) 
- Document the usage instructions and limitations of the tool, and prepare for the final presentation. (1 person: Backend developer, 1 person: Frontend developer)
### Due Date: April 8, 9am


----------------------------------------

# Project 2 - Check-In 1
## Brief description of your discussions within your team so far, and any current candidate ideas for your project. You should talk with your TA/Alex/Caroline as soon as possible about these ideas; due to the project start mid-week it's OK if you have not yet done this, but make sure to note the progress you have made so far.
Our team has been actively discussing potential project ideas, and currently, two concepts are under consideration. Idea 1 focuses on generating a comprehensive code coverage report through both dynamic and static analyses. Dynamic analysis assesses executed lines, while static analysis involves determining branches and paths. However, the initial feedback from the TA highlighted the need for a stronger analysis component and more robust visualization. Idea 2 involves creating a call graph through static analysis, mapping functions and their interactions, which received positive feedback from the TA for its solid static analysis and visualization components. The team has unanimously opted for Java as the programming language due to its familiarity among team members and the availability of resources within the classroom.

## Any planned follow-up tasks for the next week.
Moving forward, our team aims to refine the chosen ideas based on the TA's feedback. We acknowledge the importance of strengthening the analysis components and enhancing the visualization aspect to meet project goals effectively. As we continue brainstorming, we are open to exploring additional program analysis ideas that align with our objectives. Our goal for the upcoming week is to finalize the project ideas, seek TA approval, and subsequently allocate tasks for implementation, ensuring a well-structured and collaborative development process.

----------------------------------------
