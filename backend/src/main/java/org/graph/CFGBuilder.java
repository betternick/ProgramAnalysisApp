package org.graph;

/// CHAT GPT use acknowledgement: It was used at all different stages to write and debug code in the graph package.
import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.*;

// The CFG, Node and CFG Builder were made with the help of ChatGPT 4.0
public class CFGBuilder {
    // Global map to store CFGs for each method
    public static Map<String, CFG> globalCFGMap = new HashMap<>();

    public static void main(String[] args) {
        CFGBuilder builder = new CFGBuilder();
        builder.buildCFGs("backend/examples/MasterFileWithErrorCases.java");

        // builder.serializeMap("cfgMap.ser");

        // Print the global CFG map
        builder.printGlobalCFGMap();

        // Convert all CFGs to JSON
        String jsonOutput = CFGConverter.convertAllCFGs(CFGBuilder.globalCFGMap);

        // Print the JSON output
        System.out.println(jsonOutput);
    }

    public static Map<String, CFG> deserializeMap(String fileName) {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Map<String, CFG>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("Error casting to Map<String, CFG>: " + e.getMessage());
        }
        return null; // Return null if deserialization failed
    }

    // Method to serialize a Map<String, CFG> to a file
    // So the Instrumentation module can use it
    public void serializeMap(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(globalCFGMap);
        } catch (IOException e) {
            System.err.println("Error serializing map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printGlobalCFGMap() {
        for (Map.Entry<String, CFG> entry : globalCFGMap.entrySet()) {
            System.out.println("CFG for method: " + entry.getKey());
            // Use the new toTextRepresentation method to print the CFG
            System.out.println(entry.getValue().toTextRepresentation());
            System.out.println();
        }
    }

    public void printCFG(String methodName) {
        CFG cfg = globalCFGMap.get(methodName);
        if (cfg != null) {
            System.out.println("CFG for method: " + methodName);
            System.out.println(cfg.toTextRepresentation());
            System.out.println();
        } else {
            System.out.println("No CFG found for method: " + methodName);
        }
    }

    public Map<String, CFG> getGlobalCFGMap() {
        return new HashMap<>(globalCFGMap);
    }

    public void buildCFGs(String filePath) {
        // Clear the global CFG map before building new CFGs
        globalCFGMap.clear();

        Launcher launcher = new Launcher();
        launcher.addInputResource(filePath);
        CtType<?> ctType = launcher.buildModel().getAllTypes().iterator().next();

        // Create an instance of VariableAnalyzer
        VariableAnalyzer variableAnalyzer = new VariableAnalyzer();
        // Create an instance of VariableAnalyzer

        // Build CFG for each method in the class
        for (CtMethod<?> method : ctType.getMethods()) {
            CFG cfg = buildCFGForMethod(method);

            // Analyze and annotate the CFG
            variableAnalyzer.analyzeAndAnnotateCFG(method, cfg);

            // Construct the method signature with parameter names
            String methodName = method.getSimpleName();
            String parameters = method.getParameters().stream()
                    .map(p -> p.getType() + " " + p.getSimpleName())
                    .collect(Collectors.joining(", ", "(", ")"));
            String methodSignature = methodName + parameters;

            // Add the CFG to the global map
            globalCFGMap.put(methodSignature, cfg);
            // variableAnalyzer.doesJavaFileCompile(filePath);
        }
    }

    private CFG buildCFGForMethod(CtMethod<?> method) {
        CFG cfg = new CFG();
        String fileName = method.getPosition().getFile().getName();
        Node entryNode = cfg
                .createNode(new CodeBlock(new String[] { "Entry" }, fileName, method.getPosition().getLine()));
        cfg.setEntryNode(entryNode);

        Node currentNode = entryNode;
        Node exitNode = cfg.createNode(new CodeBlock(new String[] { "Exit" }, fileName, -1));
        cfg.setExitNode(exitNode);

        boolean returnEncountered = false;
        for (CtStatement statement : method.getBody().getStatements()) {
            Node newNode = addStatementAndCreateNode(statement, currentNode, cfg, exitNode);
            if (newNode == null) {
                // A return statement was encountered, stop processing further statements
                returnEncountered = true;
                break;
            } else {
                currentNode = newNode;
            }
        }

        // Ensure the last statement is connected to the exit node only if a return
        // statement was not encountered
        if (!returnEncountered && currentNode != exitNode) {
            currentNode.addNext(exitNode);
            exitNode.addPrevious(currentNode);
        }

        return cfg;
    }

    private Node addStatementAndCreateNode(CtStatement statement, Node currentNode, CFG cfg, Node exitNode) {
        String[] code = { statement.toString() };
        String fileName = currentNode.codeBlock.fileName;
        int lineStart = statement.getPosition().getLine();
        CodeBlock codeBlock = new CodeBlock(code, fileName, lineStart);
        Node newNode = cfg.createNode(codeBlock);

        // For control flow changes, handle them specifically
        if (statement instanceof CtIf) {
            currentNode.addNext(newNode);
            newNode.addPrevious(currentNode);
            return handleIfStatement((CtIf) statement, newNode, cfg, fileName, exitNode);
        } else if (statement instanceof CtLoop) {
            currentNode.addNext(newNode);
            newNode.addPrevious(currentNode);
            return handleLoopStatement((CtLoop) statement, newNode, cfg, fileName, exitNode);
        } else if (statement instanceof CtReturn || statement instanceof CtBreak) {
            // For return and break statements, connect directly to the exit node
            currentNode.addNext(newNode);
            newNode.addPrevious(currentNode);
            newNode.addNext(exitNode);
            exitNode.addPrevious(newNode);
            // Prepend "Statement:" label for return and break statements
            newNode.codeBlock.code[0] = "Statement: " + newNode.codeBlock.code[0];
            // Return null to indicate that no further statements should be connected
            return null;
        } else {
            // For simple statements, connect to the new node and prepend "Statement:" label
            currentNode.addNext(newNode);
            newNode.addPrevious(currentNode);
            newNode.codeBlock.code[0] = "Statement: " + newNode.codeBlock.code[0];
            return newNode;
        }
    }

    private Node handleIfStatement(CtIf ifStmt, Node currentNode, CFG cfg, String fileName, Node exitNode) {
        currentNode.codeBlock.code[0] = "If-Else statement: " + currentNode.codeBlock.code[0];

        Node ifConditionNode = cfg
                .createNode(new CodeBlock(new String[] { "If condition: " + ifStmt.getCondition().toString() },
                        fileName, ifStmt.getPosition().getLine()));
        currentNode.addNext(ifConditionNode);
        ifConditionNode.addPrevious(currentNode);

        // True branch
        Node trueBranchNode = cfg.createNode(new CodeBlock(new String[] { "True branch" }, fileName,
                ifStmt.getThenStatement().getPosition().getLine()));
        ifConditionNode.addNext(trueBranchNode);
        trueBranchNode.addPrevious(ifConditionNode);
        Node trueBranchLastNode = buildCFGForBlockIf(ifStmt.getThenStatement(), trueBranchNode, cfg, exitNode);

        // False branch (if present)
        Node falseBranchNode = null;
        Node falseBranchLastNode = null;
        Boolean falseBranchExists = ifStmt.getElseStatement() != null;
        if (ifStmt.getElseStatement() != null) {
            falseBranchNode = cfg.createNode(new CodeBlock(new String[] { "False branch" }, fileName,
                    ifStmt.getElseStatement().getPosition().getLine()));
            ifConditionNode.addNext(falseBranchNode);
            falseBranchNode.addPrevious(ifConditionNode);
            falseBranchLastNode = buildCFGForBlockIf(ifStmt.getElseStatement(), falseBranchNode, cfg, exitNode);
        }

        // Connect both branches to the after-if node
        Node afterIfNode = cfg.createNode(new CodeBlock(new String[] { "After If-Else" }, fileName, -1));
        trueBranchNode.addNext(afterIfNode);
        afterIfNode.addPrevious(trueBranchNode);
        if (falseBranchNode != null) {
            falseBranchNode.addNext(afterIfNode);
            afterIfNode.addPrevious(falseBranchNode);
        }

        // Check if both branches end with a return statement
        boolean trueBranchEndsWithReturn = trueBranchLastNode == null;
        boolean falseBranchEndsWithReturn = falseBranchLastNode == null && falseBranchExists;

        // System.out.println("True branch ends with return: " +
        // trueBranchEndsWithReturn);
        // System.out.println("False branch ends with return: " +
        // falseBranchEndsWithReturn);

        if (trueBranchEndsWithReturn && falseBranchEndsWithReturn) {
            System.out.println("Both branches end with return. Returning null.");
            // Return null to indicate that subsequent code is unreachable
            return null;
        }

        return afterIfNode;
    }

    private Node handleLoopStatement(CtLoop loopStmt, Node currentNode, CFG cfg, String fileName, Node exitNode) {
        String loopType = loopStmt instanceof CtWhile ? "While loop"
                : loopStmt instanceof CtFor ? "For loop" : "Unknown loop type";
        String loopCondition = loopStmt instanceof CtWhile
                ? "while (" + ((CtWhile) loopStmt).getLoopingExpression().toString() + ")"
                : loopStmt instanceof CtFor
                        ? "for ("
                                + String.join("; ",
                                        ((CtFor) loopStmt).getForInit().stream().map(CtElement::toString)
                                                .collect(Collectors.toList()))
                                + "; " + ((CtFor) loopStmt).getExpression().toString()
                                + "; "
                                + String.join(", ",
                                        ((CtFor) loopStmt).getForUpdate().stream().map(CtElement::toString)
                                                .collect(Collectors.toList()))
                                + ")"
                        : "Unknown loop condition";

        // Prepend the loop type to the code block of the existing loop node
        currentNode.codeBlock.code[0] = loopType + ": " + currentNode.codeBlock.code[0];

        Node loopConditionNode = cfg.createNode(new CodeBlock(new String[] { "Loop condition: " + loopCondition },
                fileName, loopStmt.getPosition().getLine()));
        currentNode.addNext(loopConditionNode);
        loopConditionNode.addPrevious(currentNode);

        // Create a node for the loop body
        Node loopBodyNode = cfg.createNode(
                new CodeBlock(new String[] { "Loop body" }, fileName, loopStmt.getBody().getPosition().getLine()));
        loopConditionNode.addNext(loopBodyNode);
        loopBodyNode.addPrevious(loopConditionNode);

        // Connect the loop body back to the loop condition to represent the looping
        loopBodyNode.addNext(loopConditionNode);
        loopConditionNode.addPrevious(loopBodyNode);

        // Create a node for after the loop
        Node afterLoopNode = cfg.createNode(new CodeBlock(new String[] { "After loop" }, fileName, -1));
        loopConditionNode.addNext(afterLoopNode);
        afterLoopNode.addPrevious(loopConditionNode);

        // Build the CFG for the loop body
        buildCFGForBlockLoop(loopStmt.getBody(), loopBodyNode, cfg, exitNode, afterLoopNode);

        return afterLoopNode;
    }

    private Node buildCFGForBlockIf(CtStatement block, Node currentNode, CFG cfg, Node exitNode) {
        if (block instanceof CtBlock) {
            for (CtStatement stmt : ((CtBlock<?>) block).getStatements()) {
                Node newNode = addStatementAndCreateNode(stmt, currentNode, cfg, exitNode);
                if (newNode == null) {
                    // A return statement was encountered, stop processing further statements
                    return null;
                } else {
                    currentNode = newNode;
                }
            }
        } else {
            currentNode = addStatementAndCreateNode(block, currentNode, cfg, exitNode);
        }
        return currentNode; // Return the last node in the block
    }

    private Node handleIfStatementForNestedLoop(CtIf ifStmt, Node currentNode, CFG cfg, String fileName, Node exitNode,
            Node afterLoopNode) {
        // Create a new node for the if-else statement
        String ifElseStatement = "If-Else statement: " + ifStmt.toString();
        Node ifElseNode = cfg
                .createNode(new CodeBlock(new String[] { ifElseStatement }, fileName, ifStmt.getPosition().getLine()));
        currentNode.addNext(ifElseNode);
        ifElseNode.addPrevious(currentNode);

        Node ifConditionNode = cfg
                .createNode(new CodeBlock(new String[] { "If condition: " + ifStmt.getCondition().toString() },
                        fileName, ifStmt.getPosition().getLine()));
        ifElseNode.addNext(ifConditionNode);
        ifConditionNode.addPrevious(ifElseNode);

        // True branch
        Node trueBranchNode = cfg.createNode(new CodeBlock(new String[] { "True branch" }, fileName,
                ifStmt.getThenStatement().getPosition().getLine()));
        ifConditionNode.addNext(trueBranchNode);
        trueBranchNode.addPrevious(ifConditionNode);
        Node trueBranchLastNode = buildCFGForBlockIfNestedLoop(ifStmt.getThenStatement(), trueBranchNode, cfg, exitNode,
                afterLoopNode);

        // False branch (if present)
        Node falseBranchNode = null;
        Node falseBranchLastNode = null;
        Boolean falseBranchExists = ifStmt.getElseStatement() != null;
        if (ifStmt.getElseStatement() != null) {
            falseBranchNode = cfg.createNode(new CodeBlock(new String[] { "False branch" }, fileName,
                    ifStmt.getElseStatement().getPosition().getLine()));
            ifConditionNode.addNext(falseBranchNode);
            falseBranchNode.addPrevious(ifConditionNode);
            falseBranchLastNode = buildCFGForBlockIfNestedLoop(ifStmt.getElseStatement(), falseBranchNode, cfg,
                    exitNode, afterLoopNode);
        }

        // Connect both branches to the after-if node
        Node afterIfNode = cfg.createNode(new CodeBlock(new String[] { "After If-Else" }, fileName, -1));
        trueBranchNode.addNext(afterIfNode);
        afterIfNode.addPrevious(trueBranchNode);
        if (falseBranchNode != null) {
            falseBranchNode.addNext(afterIfNode);
            afterIfNode.addPrevious(falseBranchNode);
        }

        // Check if both branches end with a return statement
        boolean trueBranchEndsWithReturn = trueBranchLastNode == null;
        boolean falseBranchEndsWithReturn = falseBranchLastNode == null && falseBranchExists;

        // System.out.println("True branch ends with return: " +
        // trueBranchEndsWithReturn);
        // System.out.println("False branch ends with return: " +
        // falseBranchEndsWithReturn);

        if (trueBranchEndsWithReturn && falseBranchEndsWithReturn) {
            System.out.println("Both branches end with return. Returning null.");
            // Return null to indicate that subsequent code is unreachable
            return null;
        }

        return afterIfNode;
    }

    private Node buildCFGForBlockIfNestedLoop(CtStatement block, Node currentNode, CFG cfg, Node exitNode,
            Node afterLoopNode) {
        boolean breakEncountered = false; // Flag to indicate if a break statement has been encountered
        if (block instanceof CtBlock) {
            for (CtStatement stmt : ((CtBlock<?>) block).getStatements()) {
                if (stmt instanceof CtBreak) {
                    // For a break statement inside an if-else block within a loop, connect directly
                    // to the afterLoopNode
                    Node breakStatementNode = cfg.createNode(new CodeBlock(new String[] { "Statement: break" },
                            currentNode.codeBlock.fileName, stmt.getPosition().getLine()));
                    currentNode.addNext(breakStatementNode);
                    breakStatementNode.addPrevious(currentNode);
                    breakStatementNode.addNext(afterLoopNode);
                    afterLoopNode.addPrevious(breakStatementNode);
                    breakEncountered = true; // Set the flag to indicate that a break statement has been encountered
                } else if (!breakEncountered) {
                    currentNode = addStatementAndCreateNode(stmt, currentNode, cfg, exitNode);
                }
            }
        } else {
            currentNode = addStatementAndCreateNode(block, currentNode, cfg, exitNode);
        }
        if (currentNode == null) {
            // A return statement was encountered, stop processing further statements
            return null;
        }
        return currentNode; // Return the last node in the block
    }

    private Node buildCFGForBlockLoop(CtStatement block, Node currentNode, CFG cfg, Node exitNode, Node afterLoopNode) {
        String fileName = currentNode.codeBlock.fileName; // Add this line to define fileName
        boolean breakEncountered = false; // Flag to indicate if a break statement has been encountered
        if (block instanceof CtBlock) {
            for (CtStatement stmt : ((CtBlock<?>) block).getStatements()) {
                if (stmt instanceof CtBreak) {
                    // For a break statement, connect directly to the after loop node
                    if (!breakEncountered) {
                        Node breakStatementNode = cfg.createNode(new CodeBlock(new String[] { "Statement: break" },
                                currentNode.codeBlock.fileName, stmt.getPosition().getLine()));
                        currentNode.addNext(breakStatementNode);
                        breakStatementNode.addPrevious(currentNode);
                        breakStatementNode.addNext(afterLoopNode);
                        afterLoopNode.addPrevious(breakStatementNode);
                    }
                    breakEncountered = true; // Set the flag to indicate that a break statement has been encountered
                } else if (!breakEncountered && stmt instanceof CtIf) {
                    // Handle if statements within loops using the handleIfStatementForNestedLoop
                    // method
                    currentNode = handleIfStatementForNestedLoop((CtIf) stmt, currentNode, cfg, fileName, exitNode,
                            afterLoopNode);
                } else if (!breakEncountered) {
                    Node newNode = addStatementAndCreateNode(stmt, currentNode, cfg, exitNode);
                    if (newNode == null) {
                        // A return statement was encountered, stop processing further statements
                        // break;
                        return null;
                    } else {
                        currentNode = newNode;
                    }
                }
            }
        } else {
            currentNode = addStatementAndCreateNode(block, currentNode, cfg, exitNode);
        }
        return currentNode; // Return the last node in the block
    }

    public List<Integer> getAllNodeIds() {
        List<Integer> nodeIds = new ArrayList<>();
        for (CFG cfg : globalCFGMap.values()) {
            for (Node node : cfg.getNodes()) {
                nodeIds.add(node.getId());
            }
        }
        return nodeIds;
    }

}
