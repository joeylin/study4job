import java.lang.reflect.Method;
import java.util.ServiceLoader;

import com.spring.boot.demo.bean.MyRobot;
import com.spring.boot.demo.bean.MyRobotTwo;
import com.spring.boot.demo.bean.Robot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author joe.ly
 * @date 2023/5/25
 */

public class MyClassTest {

    @Test
    public void testRobotService() throws Exception {
        ServiceLoader<Robot> services = ServiceLoader.load(Robot.class);
        services.forEach(Robot::sayHello);
        assertEquals(true, true);

        Class<?> clazz = Class.forName("com.spring.boot.demo.bean.MyRobot");
        Method[] methods = clazz.getDeclaredMethods();
        System.out.println(methods.length);
    }
}
