import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class Parser {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.addInputResource("path/to/your/java/file/or/directory");
        launcher.buildModel();
        CtModel model = launcher.getModel();

        for (CtClass<?> ctClass : model.getElements(new TypeFilter<>(CtClass.class))) {
            System.out.println("Class: " + ctClass.getSimpleName());
            for (CtMethod<?> method : ctClass.getMethods()) {
                System.out.println("  Method: " + method.getSimpleName());
            }
        }
    }
}
