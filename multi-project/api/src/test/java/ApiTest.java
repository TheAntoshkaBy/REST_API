import org.junit.Test;
import static org.junit.Assert.*;

public class ApiTest {

    @Test
    public void allPositive(){
        assertTrue( Utils.isAllPositiveNumbers("32", "43", "432432", "432") );
    }

    @Test
    public void oneNegative(){
        assertFalse( Utils.isAllPositiveNumbers("32","-43","432432","432") );
    }

    @Test
    public void crazyCostumer(){
        assertTrue(Utils.isAllPositiveNumbers("fds","ff","fdsiu"));
    }
}
