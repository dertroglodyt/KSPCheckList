package de.hdc.kspchecklist;

import android.content.*;

import org.junit.*;
import org.junit.runner.*;

import androidx.test.*;
import androidx.test.runner.*;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("de.hdc.kspchecklist", appContext.getPackageName());
    }
}
