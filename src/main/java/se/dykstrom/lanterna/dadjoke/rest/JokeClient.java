/*
 * Copyright 2024 Johan Dykström
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

package se.dykstrom.lanterna.dadjoke.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import se.dykstrom.lanterna.dadjoke.model.Joke;
import se.dykstrom.lanterna.dadjoke.model.JokeList;

public class JokeClient {

    private static final String BASE_URL = "https://icanhazdadjoke.com";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    public Joke getRandomJoke() throws IOException, InterruptedException {
        return makeRequest(BASE_URL, Joke.class);
    }

    public Joke getJokeBySearchTerm(final String searchTerm) throws IOException, InterruptedException {
        final var listWithFirstJoke = makeRequest(searchUrl(searchTerm, 1), JokeList.class);

        // Find out how many jokes there are in total, and pick a random one
        final var totalNumberOfJokes = listWithFirstJoke.total_jokes();
        if (totalNumberOfJokes == 0) {
            throw new IOException("No jokes about '" + searchTerm + "' found.");
        }

        final var index = random.nextInt(totalNumberOfJokes) + 1;
        final var listWithRandomJoke = makeRequest(searchUrl(searchTerm, index), JokeList.class);
        return listWithRandomJoke.results()[0];
    }

    private static String searchUrl(final String searchTerm, final int page) {
        return BASE_URL + "/search?limit=1&term=" + searchTerm + "&page=" + page;
    }

    private <T> T makeRequest(final String url, final Class<T> type) throws IOException, InterruptedException {
        final var request = HttpRequest.newBuilder(URI.create(url))
                                       .header("Accept", "application/json")
                                       .header("User-Agent", "lanterna-dad-joke (https://github.com/dykstrom/lanterna-dad-joke)")
                                       .GET()
                                       .build();
        try (final var httpClient = HttpClient.newHttpClient()) {
            final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), type);
        }
    }
}
