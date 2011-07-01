package example;

import org.junit.Test;

public class FooTest {

    @Test
	public void testFoo() {
	assertEquals( "foo", new Foo().foo() );
    }

}