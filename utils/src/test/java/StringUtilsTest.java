import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class StringUtilsTest extends TestCase {

    private String input;
    private boolean output;

    public StringUtilsTest(String input, boolean output) {
        this.input = input;
        this.output = output;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"43",true},{"-432",false},{"dsa", true},{"0", true}
        } );
    }

    @Test
    public void test(){
        Assert.assertEquals(StringUtils.isPositiveNumber(input), output);
    }
}