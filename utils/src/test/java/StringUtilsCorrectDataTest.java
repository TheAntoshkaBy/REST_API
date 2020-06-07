import com.epam.util.StringUtils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class StringUtilsCorrectDataTest extends TestCase {

    private final String input;
    private final boolean output;

    public StringUtilsCorrectDataTest(String input, boolean output) {
        this.input = input;
        this.output = output;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"43", true}, {"-432", false}, {"432.34", true}, {"-42.43", false}, {"0", true}
        });
    }

    @Test
    public void isPositiveNumber_CorrectDataList_EqualsResults() {
        Assert.assertEquals(StringUtils.isPositiveNumber(input), output);
    }
}
