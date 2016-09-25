// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.utils;

import java.time.Duration;
import java.time.Instant;

public class MeasurementUtils {

    /**
     * Java 1.8 doesn't have a Consumer without parameters (why not?), so we
     * are defining a FunctionalInterface with a nullary function.
     *
     * I call it Action, so I am consistent with .NET.
     */
    @FunctionalInterface
    public interface Action {

        void invoke();

    }

    public static void MeasureElapsedTime(String description, Action action) {
        Duration duration = MeasureElapsedTime(action);

        System.out.println(String.format("[%s] %s", description, duration));
    }

    public static Duration MeasureElapsedTime(Action action) {
        Instant start = Instant.now();

        action.invoke();

        Instant end = Instant.now();

        return Duration.between(start, end);
    }
}
