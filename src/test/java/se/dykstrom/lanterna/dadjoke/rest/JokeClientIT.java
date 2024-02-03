package se.dykstrom.lanterna.dadjoke.rest;/*
 * Copyright 2020 Johan Dykström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JokeClientIT {

    private final JokeClient client = new JokeClient();

    @Test
    void shouldGetRandomJoke() throws Exception {
        // When
        final var joke = client.getRandomJoke();

        // Then
        assertFalse(joke.joke().isBlank());
    }

    @Test
    void shouldGetRandomJokeBySearchTerm() throws Exception {
        // When
        final var joke = client.getJokeBySearchTerm("car");

        // Then
        assertFalse(joke.joke().isBlank());
    }

    @Test
    void shouldFailWithExceptionIfNoJokeFound() {
        assertThrows(IOException.class, () -> client.getJokeBySearchTerm("foobar"));
    }
}
