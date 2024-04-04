package org.profile;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject;
import java.io.*;
import java.util.jar.*;
import java.nio.file.*;

public class Executor {
    private static Executor instance = null;

    private Executor() {
    }

    public static synchronized Executor getInstance() {
        if (instance == null) {
            instance = new Executor();
        }
        return instance;
    }

    private void createJar(String jarFileName, String classFilePath, String entryPoint) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, entryPoint);

        JarOutputStream target = new JarOutputStream(new FileOutputStream(jarFileName), manifest);
        File classFile = new File(classFilePath);
        addToJar(classFile, target);
        target.close();
    }

    private void addToJar(File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            if (source.isDirectory()) {
                String name = source.getPath().replace("\\", "/");
                if (!name.isEmpty()) {
                    if (!name.endsWith("/"))
                        name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile : source.listFiles())
                    addToJar(nestedFile, target);
                return;
            }

            JarEntry entry = new JarEntry(source.getPath().replace("\\", "/"));
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true) {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        } finally {
            if (in != null)
                in.close();
        }
    }

    public void execute(String filePath, String fullClassName, String graphPath, String logPath) throws RuntimeException {
        // Delete the old log file
        try {
            Files.deleteIfExists(Paths.get(logPath));
        } catch (IOException e) {
            System.out.println("Can't delete the old log file" + e);
        }

        String agentPath = ProfilingAgent.getAgentPath();

        try {
            // Step 1: Compile the Java file
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

            File sourceFile = new File(filePath);
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourceFile);
            boolean compilationResult = compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();

            if (!compilationResult) {
                System.out.println("Compilation Failed");
                throw new RuntimeException();
            }
            System.out.println("Compilation is successful");
            fileManager.close();

            // Step 2: Create a JAR file from the compiled classes
            // Assuming the compiled class file name matches the source file name but with
            // .class extension
            String classFilePath = filePath.replace(".java", ".class");
            String jarFileName = filePath.replace(".java", ".jar");
            createJar(jarFileName, classFilePath, fullClassName);

            // Step 3: Execute the JAR file with the Java agent
            executeJavaFileWithAgent(filePath, agentPath, jarFileName, graphPath, fullClassName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeJavaFileWithAgent(String filePath, String agentJarPath, String jarToRun, String graphPath,
            String fullClassName)
            throws Exception {

        System.out.println("Running the Java application with the agent...");

        ProcessBuilder builder = new ProcessBuilder(
                "java",
                "-javaagent:" + agentJarPath + "=" + graphPath + ":" + fullClassName,
                "-cp", jarToRun
                        + ":libs/byte-buddy-1.14.12.jar"
                        + ":libs/asm-9.6.jar" + ":libs/asm-commons-9.6.jar"
                        + ":libs/log4j-api-2.23.1.jar" + ":libs/log4j-core-2.23.1.jar"
                        + ":libs/log4j-1.2-api-2.23.1.jar"
                        + ":libs/commons-lang3-3.14.0.jar",
                fullClassName);
        // builder.inheritIO();
        Process process = builder.start();
        process.waitFor();
        System.out.println("Java application with the agent finished.");
    }
}
