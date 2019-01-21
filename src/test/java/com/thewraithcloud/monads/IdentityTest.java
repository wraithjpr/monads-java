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
    public void shouldWrapAString() {
        String value = "My test string value";

        Identity<String> identity = Identity.of(value);

        assertEquals("String retrieved unwrapped should equal original value.", value, identity.get());
    }

    @Test
    public void shouldMapAString() {
        String value = "My test string value";

        Identity<String> identity = Identity.of(value);
        Identity<String> newIdentity = identity.map(String::toUpperCase);

        String newValue = newIdentity.get();

        assertEquals("String should be mapped to upper case.", value.toUpperCase(), newValue);
    }

    @Test
    public void shouldChainMethods() {
        String value = "My test string value";
        String newValue = Identity.of(value).map(String::toUpperCase).get();

        assertEquals("String should be mapped to upper case.", value.toUpperCase(), newValue);
    }

    @Test
    public void shouldChainMappers() {
        String value = "My test string value";
        String newValue = Identity.of(value).map((String s) -> s.substring(0, 5)).map(String::toUpperCase).get();

        assertEquals("String should be truncated to left 5 characters and transformed to upper case.", "MY TE",
                newValue);
    }

    @Test
    public void shouldComposeMappers() {
        Function<String, String> takeLeft5 = (String s) -> s.substring(0, 5);
        Function<String, String> toUpperCase = String::toUpperCase;
        Function<String, String> mapper = toUpperCase.compose(takeLeft5);

        String value = "My test string value";
        String newValue = Identity.of(value).map(mapper).get();

        assertEquals("String should be truncated to left 5 characters and transformed to upper case.", "MY TE",
                newValue);
    }

    @Test
    public void shouldFlatmapAString() {
        Function<String, Identity<String>> toUpperCaseM = (String s) -> {
            Identity<String> result = Identity.of(s.toUpperCase());
            return result;
        };

        String value = "My test string value";
        String newValue = Identity.of(value).flatMap(toUpperCaseM).get();

        assertEquals("String should be mapped to upper case.", value.toUpperCase(), newValue);
    }

    @Test
    public void shouldChainFlatmappers() {
        Function<String, Identity<String>> takeLeft5M = (String s) -> Identity.of(s.substring(0, 5));
        Function<String, Identity<String>> toUpperCaseM = (String s) -> Identity.of(s.toUpperCase());

        String value = "My test string value";
        String newValue = Identity.of(value).flatMap(takeLeft5M).flatMap(toUpperCaseM).get();

        assertEquals("String should be truncated to left 5 characters and transformed to upper case.", "MY TE",
                newValue);
    }

    @Test
    public void shouldComposeFlatmappers() {
        Function<String, Identity<String>> takeLeft5M = (String s) -> Identity.of(s.substring(0, 5));
        Function<String, Identity<String>> toUpperCaseM = (String s) -> Identity.of(s.toUpperCase());
        Function<String, Identity<String>> flatMapper = takeLeft5M.andThen(Identity::get).andThen(toUpperCaseM);

        String value = "My test string value";
        String newValue = Identity.of(value).flatMap(flatMapper).get();

        assertEquals("String should be truncated to left 5 characters and transformed to upper case.", "MY TE",
                newValue);
    }
}
