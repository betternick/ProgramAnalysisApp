package org.graph;

import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtElement;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// The CFG, Node and CFG Builder were made with the help of ChatGPT 4.0
public class CFGBuilder {
    // Global map to store CFGs for each method
    public Map<String, CFG> globalCFGMap = new HashMap<>();

    public static void main(String[] args) {
        CFGBuilder builder = new CFGBuilder();
        builder.buildCFGs("backend/examples/Simple.java");
        // Print the global CFG map
        builder.printGlobalCFGMap();

    }




    public void printGlobalCFGMap() {
        for (Map.Entry<String, CFG> entry : globalCFGMap.entrySet()) {
            System.out.println("CFG for method: " + entry.getKey());
            // Use the new toTextRepresentation method to print the CFG
            System.out.println(entry.getValue().toTextRepresentation());
            System.out.println();
        }
    }

    public void buildCFGs(String filePath) {
        Launcher launcher = new Launcher();
        launcher.addInputResource(filePath);
        CtType<?> ctType = launcher.buildModel().getAllTypes().iterator().next();

        // Build CFG for each method in the class
        for (CtMethod<?> method : ctType.getMethods()) {
            CFG cfg = buildCFGForMethod(method);
            String methodSignature = method.getSignature();
            globalCFGMap.put(methodSignature, cfg);
        }
    }

    private CFG buildCFGForMethod(CtMethod<?> method) {
        CFG cfg = new CFG();
        String fileName = method.getPosition().getFile().getName();
        Node entryNode = cfg
                .createNode(new CodeBlock(new String[] { "Entry" }, fileName, method.getPosition().getLine()));
        cfg.setEntryNode(entryNode);

        Node currentNode = entryNode;

        // Create a global exit node for the method
        Node exitNode = cfg.createNode(new CodeBlock(new String[] { "Exit" }, fileName, -1));
        cfg.setExitNode(exitNode);

        // Iterate through the statements of the method
        for (CtStatement statement : method.getBody().getStatements()) {
            currentNode = addStatementAndCreateNode(statement, currentNode, cfg, exitNode);
        }

        // Connect the last statement to the exit node only if it's not a return
        // statement
        if (!(currentNode.codeBlock.code[0].startsWith("Statement: return"))) {
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

        currentNode.addNext(newNode);
        newNode.addPrevious(currentNode);

        // For control flow changes, handle them specifically
        if (statement instanceof CtIf) {
            return handleIfStatement((CtIf) statement, newNode, cfg, fileName, exitNode);
        } else if (statement instanceof CtLoop) {
            return handleLoopStatement((CtLoop) statement, newNode, cfg, fileName, exitNode);
        } else if (statement instanceof CtReturn || statement instanceof CtBreak) {
            // For return and break statements, connect directly to the exit node
            newNode.addNext(exitNode);
            exitNode.addPrevious(newNode);
            // Prepend "Statement:" label for return and break statements
            newNode.codeBlock.code[0] = "Statement: " + newNode.codeBlock.code[0];
            return newNode;
        } else {
            // Prepend "Statement:" label for simple statements
            newNode.codeBlock.code[0] = "Statement: " + newNode.codeBlock.code[0];
            return newNode;
        }
    }

    private Node handleIfStatement(CtIf ifStmt, Node currentNode, CFG cfg, String fileName, Node exitNode) {
        // Prepend the words "If-Else statement:" to the code block of the existing
        // if-else node
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
        buildCFGForBlockIf(ifStmt.getThenStatement(), trueBranchNode, cfg, exitNode);

        // False branch (if present)
        if (ifStmt.getElseStatement() != null) {
            Node falseBranchNode = cfg.createNode(new CodeBlock(new String[] { "False branch" }, fileName,
                    ifStmt.getElseStatement().getPosition().getLine()));
            ifConditionNode.addNext(falseBranchNode);
            falseBranchNode.addPrevious(ifConditionNode);
            buildCFGForBlockIf(ifStmt.getElseStatement(), falseBranchNode, cfg, exitNode);
        }

        // Connect both branches to the after-if node
        Node afterIfNode = cfg.createNode(new CodeBlock(new String[] { "After If-Else" }, fileName, -1));
        trueBranchNode.addNext(afterIfNode);
        afterIfNode.addPrevious(trueBranchNode);

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
                currentNode = addStatementAndCreateNode(stmt, currentNode, cfg, exitNode);
            }
            return currentNode; // Return the last node in the block
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
        buildCFGForBlockIfNestedLoop(ifStmt.getThenStatement(), trueBranchNode, cfg, exitNode, afterLoopNode);

        // False branch (if present)
        Node falseBranchNode = null;
        if (ifStmt.getElseStatement() != null) {
            falseBranchNode = cfg.createNode(new CodeBlock(new String[] { "False branch" }, fileName,
                    ifStmt.getElseStatement().getPosition().getLine()));
            ifConditionNode.addNext(falseBranchNode);
            falseBranchNode.addPrevious(ifConditionNode);
            buildCFGForBlockIfNestedLoop(ifStmt.getElseStatement(), falseBranchNode, cfg, exitNode, afterLoopNode);
        }

        // Connect both branches to the after-if node
        Node afterIfNode = cfg.createNode(new CodeBlock(new String[] { "After If-Else" }, fileName, -1));
        trueBranchNode.addNext(afterIfNode);
        afterIfNode.addPrevious(trueBranchNode);
        if (falseBranchNode != null) {
            falseBranchNode.addNext(afterIfNode);
            afterIfNode.addPrevious(falseBranchNode);
        }

        return afterIfNode;
    }

    private Node buildCFGForBlockIfNestedLoop(CtStatement block, Node currentNode, CFG cfg, Node exitNode,
            Node afterLoopNode) {
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
                } else {
                    currentNode = addStatementAndCreateNode(stmt, currentNode, cfg, exitNode);
                }
            }
        } else {
            currentNode = addStatementAndCreateNode(block, currentNode, cfg, exitNode);
        }
        return currentNode;
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
                        currentNode = breakStatementNode; // Continue building the CFG from the break statement node
                    }
                    breakEncountered = true; // Set the flag to indicate that a break statement has been encountered
                } else if (stmt instanceof CtIf) {

                    // If the statement is an if-else block, use the specialized method to handle
                    // nested loops
                    currentNode = handleIfStatementForNestedLoop((CtIf) stmt, currentNode, cfg, fileName, exitNode,
                            afterLoopNode);
                } else if (!breakEncountered) {
                    currentNode = addStatementAndCreateNode(stmt, currentNode, cfg, exitNode);
                } else {
                    // If a break statement was encountered, create new nodes without connecting
                    // them to the previous nodes in the loop
                    currentNode = cfg.createNode(new CodeBlock(new String[] { stmt.toString() },
                            currentNode.codeBlock.fileName, stmt.getPosition().getLine()));
                }
            }
        } else {
            currentNode = addStatementAndCreateNode(block, currentNode, cfg, exitNode);
        }
        return currentNode; // Return the last node in the block
    }
}
