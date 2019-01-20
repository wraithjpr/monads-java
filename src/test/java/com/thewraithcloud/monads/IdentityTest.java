package com.thewraithcloud.monads;

// import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

/**
 * Unit tests for Identity.
 */
public class IdentityTest {
    @Test
    public void ShouldWrapAString() {
        String value = "My test string value";

        Identity<String> identity = Identity.of(value);

        assertEquals("String retrieved unwrapped should equal original value.", value, identity.get());
    }

    @Test
    public void ShouldMapAString() {
        String value = "My test string value";

        Identity<String> identity = Identity.of(value);
        Identity<String> newIdentity = identity.map(String::toUpperCase);

        String newValue = newIdentity.get();

        assertEquals("String should be mapped to upper case.", value.toUpperCase(), newValue);
    }

    @Test
    public void MethodsShouldChain() {
        String value = "My test string value";
        String newValue = Identity.of(value).map(String::toUpperCase).get();

        assertEquals("Result is returned by chaining #of then #map then #get  ", value.toUpperCase(), newValue);
    }

    @Test
    public void MappersShouldChain() {
        String value = "My test string value";
        String newValue = Identity.of(value).map((String s) -> s.substring(0, 5)).map(String::toUpperCase).get();

        assertEquals("Result is returned by mapper chain.", "MY TE", newValue);
    }

    @Test
    public void MappersShouldCompose() {
        Function<String, String> takeLeft5 = (String s) -> s.substring(0, 5);
        Function<String, String> toUpperCase = String::toUpperCase;
        Function<String, String> mapper = toUpperCase.compose(takeLeft5);

        String value = "My test string value";
        String newValue = Identity.of(value).map(mapper).get();

        assertEquals("Result is returned by mapper composure.", "MY TE", newValue);
    }
}
