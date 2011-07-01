package example;

import org.junit.Test;

public class FooTest extends junit.framework.Assert {

    @Test
	public void testFoo() {
	assertEquals( "foo", new Foo().foo() );
    }

}