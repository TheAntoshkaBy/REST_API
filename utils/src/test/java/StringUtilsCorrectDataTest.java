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

    private String input;
    private boolean output;

    public StringUtilsCorrectDataTest(String input, boolean output) {
        this.input = input;
        this.output = output;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"43",true},{"-432",false},{"0", true},{"0", true}
        } );
    }

    @Test
    public void StringUtils_CorrectDataList_EqualsResults(){
        Assert.assertEquals(StringUtils.isPositiveNumber(input), output);
    }

    @Test(expected = NumberFormatException.class)
    public void StringUtils_IncorrectDataList_EqualsResults(){
        StringUtils.isPositiveNumber("d");
    }
}