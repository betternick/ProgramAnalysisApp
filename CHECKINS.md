Use this file to commit information clearly documenting your check-ins' content. If you want to store more information/details besides what's required for the check-ins that's fine too. Make sure that your TA has had a chance to sign off on your check-in each week (before the deadline); typically you should discuss your material with them before finalizing it here.

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
