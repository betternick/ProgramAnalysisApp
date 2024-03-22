package org.graph;

import spoon.Launcher;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtElement;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CFGBuilder {
    // Global map to store CFGs for each method
    private Map<String, CFG> globalCFGMap = new HashMap<>();

    public static void main(String[] args) {
        CFGBuilder builder = new CFGBuilder();
        builder.buildCFGs("backend/examples/Simple.java");

        // Print the global CFG map
        builder.printGlobalCFGMap();

    }

//    public void printGlobalCFGMap() {
//        for (Map.Entry<String, CFG> entry : globalCFGMap.entrySet()) {
//            System.out.println("CFG for method: " + entry.getKey());
//            entry.getValue().print();
//            System.out.println();
//        }
//    }

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
        Node entryNode = cfg.createNode(new CodeBlock(new String[]{"Entry"}, fileName, method.getPosition().getLine()));
        cfg.setEntryNode(entryNode);

        Node currentNode = entryNode;

        // Create a global exit node for the method
        Node exitNode = cfg.createNode(new CodeBlock(new String[]{"Exit"}, fileName, -1));
        cfg.setExitNode(exitNode);

        // Iterate through the statements of the method
        for (CtStatement statement : method.getBody().getStatements()) {
            currentNode = addStatementAndCreateNode(statement, currentNode, cfg, exitNode);
        }

        // Connect the last statement to the exit node only if it's not a return statement
        if (!(currentNode.codeBlock.code[0].startsWith("return"))) {
            currentNode.addNext(exitNode);
            exitNode.addPrevious(currentNode);
        }

        return cfg;
    }











    private Node addStatementAndCreateNode(CtStatement statement, Node currentNode, CFG cfg, Node exitNode) {
        String[] code = {statement.toString()};
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
        } else if (statement instanceof CtReturn) {
            // For return statements, connect directly to the exit node
            newNode.addNext(exitNode);
            exitNode.addPrevious(newNode);
            return newNode;
        } else {
            // For simple statements, just move to the next
            return newNode;
        }
    }




    private Node handleIfStatement(CtIf ifStmt, Node currentNode, CFG cfg, String fileName, Node exitNode) {
        Node ifConditionNode = cfg.createNode(new CodeBlock(new String[]{"If condition: " + ifStmt.getCondition().toString()}, fileName, ifStmt.getPosition().getLine()));
        currentNode.addNext(ifConditionNode);
        ifConditionNode.addPrevious(currentNode);

        // True branch
        Node trueBranchNode = cfg.createNode(new CodeBlock(new String[]{"True branch"}, fileName, ifStmt.getThenStatement().getPosition().getLine()));
        ifConditionNode.addNext(trueBranchNode);
        trueBranchNode.addPrevious(ifConditionNode);
        Node lastNodeInTrueBranch = buildCFGForBlock(ifStmt.getThenStatement(), trueBranchNode, cfg, exitNode);

        // False branch (if present)
        Node falseBranchNode = null;
        Node lastNodeInFalseBranch = null;
        if (ifStmt.getElseStatement() != null) {
            falseBranchNode = cfg.createNode(new CodeBlock(new String[]{"False branch"}, fileName, ifStmt.getElseStatement().getPosition().getLine()));
            ifConditionNode.addNext(falseBranchNode);
            falseBranchNode.addPrevious(ifConditionNode);
            lastNodeInFalseBranch = buildCFGForBlock(ifStmt.getElseStatement(), falseBranchNode, cfg, exitNode);
        }

        // Connect both branches to the after-if node
        Node afterIfNode = cfg.createNode(new CodeBlock(new String[]{"After If-Else"}, fileName, -1));
        trueBranchNode.addNext(afterIfNode);
        afterIfNode.addPrevious(trueBranchNode);
        if (falseBranchNode != null) {
            falseBranchNode.addNext(afterIfNode);
            afterIfNode.addPrevious(falseBranchNode);
        }

        return afterIfNode;
    }

    private Node handleLoopStatement(CtLoop loopStmt, Node currentNode, CFG cfg, String fileName, Node exitNode) {
        String loopCondition = loopStmt instanceof CtWhile ?
                "while (" + ((CtWhile) loopStmt).getLoopingExpression().toString() + ")" :
                loopStmt instanceof CtFor ?
                        "for (" + String.join("; ", ((CtFor) loopStmt).getForInit().stream().map(CtElement::toString).collect(Collectors.toList())) +
                                "; " + ((CtFor) loopStmt).getExpression().toString() +
                                "; " + String.join(", ", ((CtFor) loopStmt).getForUpdate().stream().map(CtElement::toString).collect(Collectors.toList())) + ")" :
                        "Unknown loop type";

        Node loopConditionNode = cfg.createNode(new CodeBlock(new String[]{"Loop condition: " + loopCondition}, fileName, loopStmt.getPosition().getLine()));
        currentNode.addNext(loopConditionNode);
        loopConditionNode.addPrevious(currentNode);

        // Create a node for the loop body
        Node loopBodyNode = cfg.createNode(new CodeBlock(new String[]{"Loop body"}, fileName, loopStmt.getBody().getPosition().getLine()));
        loopConditionNode.addNext(loopBodyNode);
        loopBodyNode.addPrevious(loopConditionNode);

        // Build the CFG for the loop body
        Node lastNodeInLoopBody = buildCFGForBlock(loopStmt.getBody(), loopBodyNode, cfg, exitNode);

        // Connect the loop body back to the loop condition to represent the looping
        loopBodyNode.addNext(loopConditionNode);
        loopConditionNode.addPrevious(loopBodyNode);

        // Create a node for after the loop
        Node afterLoopNode = cfg.createNode(new CodeBlock(new String[]{"After loop"}, fileName, -1));
        loopConditionNode.addNext(afterLoopNode);
        afterLoopNode.addPrevious(loopConditionNode);

        return afterLoopNode;
    }

    private Node buildCFGForBlock(CtStatement block, Node currentNode, CFG cfg, Node exitNode) {
        if (block instanceof CtBlock) {
            for (CtStatement stmt : ((CtBlock<?>) block).getStatements()) {
                currentNode = addStatementAndCreateNode(stmt, currentNode, cfg, exitNode);
            }
        } else {
            currentNode = addStatementAndCreateNode(block, currentNode, cfg, exitNode);
        }
        return currentNode; // Return the last node in the block
    }


}