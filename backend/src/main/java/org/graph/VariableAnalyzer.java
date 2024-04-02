package org.graph;

import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.visitor.filter.TypeFilter;


import javax.tools.*;
import java.io.File;
import java.util.*;

public class VariableAnalyzer {




    public Map<Integer, String> analyzeVariables(CtMethod<?> ctMethod, CFG cfg) {
        Map<Integer, String> variableIssues = new HashMap<>();
        Set<String> usedVariables = new HashSet<>();
        Set<String> initializedVariables = new HashSet<>();

        Deque<Map<String, Integer>> scopeStack = new ArrayDeque<>();
        scopeStack.push(new HashMap<>()); // Push the global scope

        if (ctMethod.getBody() != null) {
            analyzeStatements(ctMethod.getBody().getStatements(), scopeStack, variableIssues, usedVariables, initializedVariables);
        }

        // Add comments to nodes in the CFG based on the variable analysis results
        for (Node node : cfg.getNodes()) {
            if (variableIssues.containsKey(node.codeBlock.lineStart)) {
                node.addComment(variableIssues.get(node.codeBlock.lineStart));
            }
        }

        return variableIssues;
    }

//    private void analyzeStatements(List<CtStatement> statements, Deque<Map<String, Integer>> scopeStack, Map<Integer, String> variableIssues, Set<String> usedVariables, Set<String> initializedVariables) {
//        for (CtStatement statement : statements) {
//            System.out.println("Analyzing statement: " + statement);
//            System.out.println("Current scope: " + scopeStack.peek());
//            System.out.println("Used variables: " + usedVariables);
//            System.out.println("Initialized variables: " + initializedVariables);
//            System.out.println("Variable issues: " + variableIssues);
//
//            if (statement instanceof CtVariable) {
//                CtVariable<?> variable = (CtVariable<?>) statement;
//                String variableName = variable.getSimpleName();
//                int line = variable.getPosition().getLine();
//
//                Map<String, Integer> currentScope = scopeStack.peek();
//                if (currentScope.containsKey(variableName)) {
//                    variableIssues.put(line, "Duplicate declaration of variable '" + variableName + "'");
//                } else {
//                    currentScope.put(variableName, line);
//                }
//
//                if (variable.getDefaultExpression() != null) {
//                    initializedVariables.add(variableName);
//                }
//            } else if (statement instanceof CtAssignment) {
//                CtAssignment<?, ?> assignment = (CtAssignment<?, ?>) statement;
//                if (assignment.getAssigned() instanceof CtVariableAccess) {
//                    CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) assignment.getAssigned();
//                    String variableName = variableAccess.getVariable().getSimpleName();
//                    int line = variableAccess.getPosition().getLine();
//
//                    boolean declared = scopeStack.stream().anyMatch(scope -> scope.containsKey(variableName));
//                    if (!declared) {
//                        System.out.println("Variable '" + variableName + "' assigned a value without declaration at line " + line);
//                        variableIssues.put(line, "Variable '" + variableName + "' assigned a value without declaration 1");
//                    } else {
//                        usedVariables.add(variableName);
//                    }
//                }
//            } else if (statement instanceof CtVariableAccess) {
//                CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) statement;
//                String variableName = variableAccess.getVariable().getSimpleName();
//                int line = variableAccess.getPosition().getLine();
//
//                System.out.println("Checking variable access: " + variableName + " at line " + line);
//
//                boolean declared = scopeStack.stream().anyMatch(scope -> scope.containsKey(variableName));
//                System.out.println("Variable '" + variableName + "' declared in scope: " + declared);
//
//                if (!declared) {
//                    System.out.println("Variable '" + variableName + "' assigned a value without declaration at line " + line);
//                    variableIssues.put(line, "Variable '" + variableName + "' assigned a value without declaration 2");
//                } else {
//                    usedVariables.add(variableName);
//                }
//            }  else if (statement instanceof CtExpression) {
//            analyzeExpression((CtExpression<?>) statement, scopeStack, variableIssues, usedVariables, initializedVariables);
//        }
//
//            else if (statement instanceof CtLoop) {
//                CtLoop loop = (CtLoop) statement;
//                scopeStack.push(new HashMap<>(scopeStack.peek())); // Copy the current scope
//                System.out.println("Entering new scope for loop");
//                System.out.println("Scope stack after entering loop: " + scopeStack);
//                CtStatement loopBody = loop.getBody();
//                if (loopBody instanceof CtBlock) {
//                    analyzeStatements(((CtBlock<?>) loopBody).getStatements(), scopeStack, variableIssues, usedVariables, initializedVariables);
//                }
//                scopeStack.pop();
//                System.out.println("Exiting scope for loop");
//                System.out.println("Scope stack after exiting loop: " + scopeStack);
//            } else if (statement instanceof CtAssignment) {
//                CtAssignment<?, ?> assignment = (CtAssignment<?, ?>) statement;
//                String variableName = ((CtVariableAccess) assignment.getAssigned()).getVariable().getSimpleName();
//                int line = assignment.getPosition().getLine();
//
//                boolean declared = scopeStack.stream().anyMatch(scope -> scope.containsKey(variableName));
//                if (!declared) {
//                    System.out.println("Variable '" + variableName + "' assigned a value without declaration at line " + line);
//                    variableIssues.put(line, "Variable '" + variableName + "' assigned a value without declaration 3");
//                } else {
//                    usedVariables.add(variableName);
//                }
//            }  else if (statement instanceof CtBinaryOperator) {
//            CtBinaryOperator<?> binaryOperator = (CtBinaryOperator<?>) statement;
//            analyzeExpression(binaryOperator.getLeftHandOperand(), scopeStack, variableIssues, usedVariables, initializedVariables);
//            analyzeExpression(binaryOperator.getRightHandOperand(), scopeStack, variableIssues, usedVariables, initializedVariables);
//        }
//            else if (statement instanceof CtIf) {
//                CtIf ctIf = (CtIf) statement;
//
//                // Then branch
//                scopeStack.push(new HashMap<>(scopeStack.peek()));
//                System.out.println("Entering new scope for then branch");
//                System.out.println("Scope stack after entering then branch: " + scopeStack);
//                CtStatement thenStatement = ctIf.getThenStatement();
//                if (thenStatement instanceof CtBlock) {
//                    analyzeStatements(((CtBlock<?>) thenStatement).getStatements(), scopeStack, variableIssues, usedVariables, initializedVariables);
//                }
//                scopeStack.pop();
//                System.out.println("Exiting scope for then branch");
//                System.out.println("Scope stack after exiting then branch: " + scopeStack);
//
//                // Else branch
//                if (ctIf.getElseStatement() != null) {
//                    scopeStack.push(new HashMap<>(scopeStack.peek()));
//                    System.out.println("Entering new scope for else branch");
//                    System.out.println("Scope stack after entering else branch: " + scopeStack);
//                    CtStatement elseStatement = ctIf.getElseStatement();
//                    if (elseStatement instanceof CtBlock) {
//                        analyzeStatements(((CtBlock<?>) elseStatement).getStatements(), scopeStack, variableIssues, usedVariables, initializedVariables);
//                    }
//                    scopeStack.pop();
//                    System.out.println("Exiting scope for else branch");
//                    System.out.println("Scope stack after exiting else branch: " + scopeStack);
//                }
//            }
//            else {
//                System.out.println("Unhandled statement type: " + statement.getClass().getSimpleName());
//            }
//
//        }
//
//        // Check for variables declared but not used or initialized
//        Map<String, Integer> currentScope = scopeStack.peek();
//        for (Map.Entry<String, Integer> entry : currentScope.entrySet()) {
//            String variableName = entry.getKey();
//            int line = entry.getValue();
//            if (!usedVariables.contains(variableName) && !initializedVariables.contains(variableName)) {
//                variableIssues.put(line, "Variable '" + variableName + "' declared but not initialized");
//            }
//        }
//    }

    private void analyzeStatements(List<CtStatement> statements, Deque<Map<String, Integer>> scopeStack, Map<Integer, String> variableIssues, Set<String> usedVariables, Set<String> initializedVariables) {
        for (CtStatement statement : statements) {
            System.out.println("Analyzing statement: " + statement);
            System.out.println("Current scope: " + scopeStack.peek());
            System.out.println("Used variables: " + usedVariables);
            System.out.println("Initialized variables: " + initializedVariables);
            System.out.println("Variable issues: " + variableIssues);

            if (statement instanceof CtVariable) {
                CtVariable<?> variable = (CtVariable<?>) statement;
                String variableName = variable.getSimpleName();
                int line = variable.getPosition().getLine();

                Map<String, Integer> currentScope = scopeStack.peek();
                if (currentScope.containsKey(variableName)) {
                    variableIssues.put(line, "Duplicate declaration of variable '" + variableName + "'");
                } else {
                    currentScope.put(variableName, line);
                }

                if (variable.getDefaultExpression() != null) {
                    initializedVariables.add(variableName);
                }
            } else if (statement instanceof CtAssignment) {
                CtAssignment<?, ?> assignment = (CtAssignment<?, ?>) statement;
                if (assignment.getAssigned() instanceof CtVariableAccess) {
                    CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) assignment.getAssigned();
                    String variableName = variableAccess.getVariable().getSimpleName();
                    int line = variableAccess.getPosition().getLine();

                    boolean declared = scopeStack.stream().anyMatch(scope -> scope.containsKey(variableName));
                    if (!declared) {
                        System.out.println("Variable '" + variableName + "' assigned a value without declaration at line " + line);
                        variableIssues.put(line, "Variable '" + variableName + "' assigned a value without declaration 1");
                    } else {
                        usedVariables.add(variableName);
                    }
                }
            } else if (statement instanceof CtLoop) {
                CtLoop loop = (CtLoop) statement;
                scopeStack.push(new HashMap<>(scopeStack.peek())); // Copy the current scope
                System.out.println("Entering new scope for loop");
                System.out.println("Scope stack after entering loop: " + scopeStack);
                CtStatement loopBody = loop.getBody();
                if (loopBody instanceof CtBlock) {
                    analyzeStatements(((CtBlock<?>) loopBody).getStatements(), scopeStack, variableIssues, usedVariables, initializedVariables);
                }
                scopeStack.pop();
                System.out.println("Exiting scope for loop");
                System.out.println("Scope stack after exiting loop: " + scopeStack);
            } else if (statement instanceof CtIf) {
                CtIf ctIf = (CtIf) statement;

                // Then branch
                scopeStack.push(new HashMap<>(scopeStack.peek()));
                System.out.println("Entering new scope for then branch");
                System.out.println("Scope stack after entering then branch: " + scopeStack);
                CtStatement thenStatement = ctIf.getThenStatement();
                if (thenStatement instanceof CtBlock) {
                    analyzeStatements(((CtBlock<?>) thenStatement).getStatements(), scopeStack, variableIssues, usedVariables, initializedVariables);
                }
                scopeStack.pop();
                System.out.println("Exiting scope for then branch");
                System.out.println("Scope stack after exiting then branch: " + scopeStack);

                // Else branch
                if (ctIf.getElseStatement() != null) {
                    scopeStack.push(new HashMap<>(scopeStack.peek()));
                    System.out.println("Entering new scope for else branch");
                    System.out.println("Scope stack after entering else branch: " + scopeStack);
                    CtStatement elseStatement = ctIf.getElseStatement();
                    if (elseStatement instanceof CtBlock) {
                        analyzeStatements(((CtBlock<?>) elseStatement).getStatements(), scopeStack, variableIssues, usedVariables, initializedVariables);
                    }
                    scopeStack.pop();
                    System.out.println("Exiting scope for else branch");
                    System.out.println("Scope stack after exiting else branch: " + scopeStack);
                }
            }
            else {
                System.out.println("Unhandled statement type: " + statement.getClass().getSimpleName());
            }

        }

        // Check for variables declared but not used or initialized
        Map<String, Integer> currentScope = scopeStack.peek();
        for (Map.Entry<String, Integer> entry : currentScope.entrySet()) {
            String variableName = entry.getKey();
            int line = entry.getValue();
            if (!usedVariables.contains(variableName) && !initializedVariables.contains(variableName)) {
                variableIssues.put(line, "Variable '" + variableName + "' declared but not initialized");
            }
        }
    }

//    private void analyzeExpression(CtExpression<?> expression, Deque<Map<String, Integer>> scopeStack, Map<Integer, String> variableIssues, Set<String> usedVariables, Set<String> initializedVariables) {
//        if (expression instanceof CtVariableAccess) {
//            CtVariableAccess<?> variableAccess = (CtVariableAccess<?>) expression;
//            String variableName = variableAccess.getVariable().getSimpleName();
//            int line = variableAccess.getPosition().getLine();
//
//            boolean declared = scopeStack.stream().anyMatch(scope -> scope.containsKey(variableName));
//            if (!declared && !usedVariables.contains(variableName)) {
//                System.out.println("Variable '" + variableName + "' assigned a value without declaration at line " + line);
//                variableIssues.put(line, "Variable '" + variableName + "' assigned a value without declaration 4");
//            } else {
//                usedVariables.add(variableName);
//            }
//        } else if (expression instanceof CtBinaryOperator) {
//            CtBinaryOperator<?> binaryOperator = (CtBinaryOperator<?>) expression;
//            analyzeExpression(binaryOperator.getLeftHandOperand(), scopeStack, variableIssues, usedVariables, initializedVariables);
//            analyzeExpression(binaryOperator.getRightHandOperand(), scopeStack, variableIssues, usedVariables, initializedVariables);
//        }
//        // Add more cases as needed for other expression types
//    }


    private boolean doesJavaFileCompile(String filePath) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(List.of(new File(filePath)));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);

        boolean success = task.call();
        if (!success) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.err.println(diagnostic.getMessage(Locale.ENGLISH));
            }
        }
        return success;
    }




}
