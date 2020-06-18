import com.epam.util.StringUtils;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class StringUtilsIncorrectDataTest extends TestCase {

    private final String input;

    public StringUtilsIncorrectDataTest(String input) {
        this.input = input;
    }

    @Parameterized.Parameters
    public static Collection<Object> data() {
        return Arrays.asList(new Object[]{
                "fds", "r343ij", "324,43", "-43243o43432"
        });
    }

    @Test(expected = NumberFormatException.class)
    public void isPositiveNumber_IncorrectDataList_NumberFormatException() {
        StringUtils.isPositiveNumber(input);
    }
}
