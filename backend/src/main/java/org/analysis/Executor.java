package org.profile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Executor {
    public void executeJavaFileWithAgent(String filePath, String agentJarPath, String jarToRun, String fullClassName) throws Exception {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        File sourceFile = new File(filePath);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromFiles(java.util.Collections.singletonList(sourceFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);

        if (task.call()) {
            System.out.println("Compilation is successful");
        } else {
            System.out.println("Compilation Failed");
            return;
        }

        // // 2. Construct classpath including the directory of the compiled classes.
        // Path compiledClassesDir = Paths.get(sourceFile.getParent(), "bin");
        // Files.createDirectories(compiledClassesDir);
        // URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] {
        // compiledClassesDir.toUri().toURL() });

        // // 3. Extract the class name and package from the file path.
        // String className = extractClassNameFromFilePath(sourceFilePath);

        // // 4. Construct the command to run the Java application with the agent.
        // ProcessBuilder builder = new ProcessBuilder(
        // "java", "-javaagent:" + agentJarPath, "-cp", compiledClassesDir.toString(),
        // className);
        // builder.inheritIO();

        // // 5. Start the process and capture the output.
        // Process process = builder.start();
        // process.waitFor();
        System.out.println("Running the Java application with the agent...");

        // ProcessBuilder builder = new ProcessBuilder(
        //     "java", 
        //     "-javaagent:build/libs/backend-agent.jar", // Your agent
        //     "-javaagent:byte-buddy-agent-1.14.12.jar", // Byte Buddy agent
        //     "-cp", "examples/Simple.jar:byte-buddy-1.14.12.jar", // Classpath
        //     "Simple" // The main class name of your application
        // );

        ProcessBuilder builder = new ProcessBuilder(
            "java", 
            // "-javaagent:" + agentJarPath, 
            "-javaagent:" + "build/libs/backend-agent.jar",
            "-javaagent:" + "byte-buddy-agent-1.14.12.jar",
            "-cp", "examples/Simple.jar:" + "byte-buddy-1.14.12.jar",
            // fullClassName,
            "Simple"
        );
        builder.inheritIO();
        Process process = builder.start();
        process.waitFor();
        System.out.println("Java application with the agent finished.");
    }

    public void executeJavaFile(String filePath) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int compilationResult = compiler.run(null, null, null, filePath);
        if (compilationResult == 0) {
            System.out.println("Compilation is successful");
        } else {
            System.out.println("Compilation Failed");
            return;
        }

        // Load the class
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { new File("examples").toURI().toURL() });
        System.out.println("classLoader: " + classLoader.getURLs());

        String className = extractClassNameFromFilePath(filePath);
        Class<?> cls = Class.forName(className, true, classLoader);
        System.out.println("Loaded class name: " + cls.getName());

        Method main = cls.getMethod("main", String[].class);
        String[] args = null; // Arguments to pass to the main method, if any
        main.invoke(null, (Object) args); // Static method doesn't have an instance
    }

    // Convert file path to class name
    // examples/Simple.java -> examples.Simple
    private String extractClassNameFromFilePath(String filePath) {
        // filePath = filePath.replaceAll("[/\\\\]", "."); // Replace file separators
        // with dots
        // filePath = filePath.replaceAll(".class$", ""); // Remove the .class extension
        // if (filePath.startsWith(".")) {
        // filePath = filePath.substring(1); // Remove leading dot, if exists
        // }
        // return filePath;
        return "Simple";
    }

    public static void main(String[] args) throws Exception {
        // new Executor().executeJavaFile("examples/Simple.java");
        new Executor().executeJavaFileWithAgent("examples/Simple.java", "build/libs/backend-agent.jar", "examples/Simple.jar", "Simple");
    }
}
