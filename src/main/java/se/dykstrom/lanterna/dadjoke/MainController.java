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

package se.dykstrom.lanterna.dadjoke;

import java.io.IOException;

import com.googlecode.lanterna.gui2.Button;
import org.apache.commons.text.WordUtils;
import se.dykstrom.lanterna.dadjoke.rest.JokeClient;

public class MainController {

    private final JokeClient jokeClient = new JokeClient();
    private final Thread.Builder.OfVirtual threadBuilder = Thread.ofVirtual().name("worker-", 0);
    private final MainView view;

    public MainController(final MainView view) {
        this.view = view;
        view.button.addListener(this::handleSearchAction);
    }

    private void handleSearchAction(final Button button) {
        final var searchTerm = view.inputBox.getText().strip();
        final var columns = view.outputBox.getSize().getColumns() - 1;
        threadBuilder.start(() -> {
            final var result = getJoke(searchTerm);
            final var wrappedText = WordUtils.wrap(result, columns);
            view.invokeLater(() -> view.outputBox.setText(wrappedText));
        });
    }

    private String getJoke(final String searchTerm) {
        try {
            if (searchTerm.isBlank()) {
                return jokeClient.getRandomJoke().joke();
            } else {
                return jokeClient.getJokeBySearchTerm(searchTerm).joke();
            }
        } catch (IOException e) {
            return "Failed to get joke: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Failed to get joke: " + e.getMessage();
        }
    }
}
