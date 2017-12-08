package com.xceptance.xlt.nocoding.util;

import java.util.function.Consumer;

/**
 * A simple interface that mimics consumer, but is able to throw exceptions.
 * 
 * @author ckeiner
 */
@FunctionalInterface
public interface ThrowingConsumer<T> extends Consumer<T>
{

    @Override
    default void accept(final T elem)
    {
        try
        {
            acceptThrows(elem);
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    void acceptThrows(T elem) throws Exception;

}