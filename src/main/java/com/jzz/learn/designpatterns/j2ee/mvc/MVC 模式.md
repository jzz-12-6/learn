# MVC 模式

MVC 模式代表 Model-View-Controller（模型-视图-控制器） 模式。这种模式用于应用程序的分层开发。

- **Model（模型）** - 模型代表一个存取数据的对象或 JAVA POJO。它也可以带有逻辑，在数据变化时更新控制器。
- **View（视图）** - 视图代表模型包含的数据的可视化。
- **Controller（控制器）** - 控制器作用于模型和视图上。它控制数据流向模型对象，并在数据变化时更新视图。它使视图与模型分离开。

```java
/**
 * 模型层
 * @author jzz
 * @date 2019/5/22
 */
@Data
public class Student {
    private String rollNo;
    private String name;
}
/**
 * 视图
 * @author jzz
 * @date 2019/5/22
 */
@Slf4j
public class StudentView {
    public void printStudentDetails(String studentName, String studentRollNo){
        log.info("Student: ");
        log.info("Name: " + studentName);
        log.info("Roll No: " + studentRollNo);
    }
}
/**
 * 控制层
 * @author jzz
 * @date 2019/5/22
 */
@Data
@AllArgsConstructor
public class StudentController {
    private Student model;
    private StudentView view;
    

    public void setStudentName(String name){
        model.setName(name);
    }

    public String getStudentName(){
        return model.getName();
    }

    public void setStudentRollNo(String rollNo){
        model.setRollNo(rollNo);
    }

    public String getStudentRollNo(){
        return model.getRollNo();
    }

    public void updateView(){
        view.printStudentDetails(model.getName(), model.getRollNo());
    }
}
/**
 * @author jzz
 * @date 2019/5/22
 */
public class MVCPatternDemo {
    public static void main(String[] args) {

        //从数据库获取学生记录
        Student model  = retriveStudentFromDatabase();

        //创建一个视图：把学生详细信息输出到控制台
        StudentView view = new StudentView();

        StudentController controller = new StudentController(model, view);

        controller.updateView();

        //更新模型数据
        controller.setStudentName("John");

        controller.updateView();
    }

    private static Student retriveStudentFromDatabase(){
        Student student = new Student();
        student.setName("Robert");
        student.setRollNo("10");
        return student;
    }
}



```

