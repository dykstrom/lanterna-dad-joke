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
import java.util.List;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.TextGUIThread;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class MainView {

    public final Button button = new Button("Get joke");
    public final TextBox inputBox = new TextBox(new TerminalSize(40, 1));
    public final TextBox outputBox = new TextBox(new TerminalSize(51, 10)).setReadOnly(true).setCaretWarp(true);

    private TextGUIThread guiThread;

    public void show() throws IOException {
        // Setup terminal and screen layers
        try (Terminal terminal = new DefaultTerminalFactory().createTerminal()) {
            TerminalScreen screen = new TerminalScreen(terminal);
            screen.startScreen();

            Panel inputPanel = new Panel(new GridLayout(4));
            // Row 1
            inputPanel.addComponent(new EmptySpace(TerminalSize.ZERO));
            inputPanel.addComponent(new Label("Search for joke"));
            inputPanel.addComponent(new EmptySpace(TerminalSize.ZERO));
            inputPanel.addComponent(new EmptySpace(TerminalSize.ZERO));
            // Row 2
            inputPanel.addComponent(new EmptySpace(TerminalSize.ZERO));
            inputPanel.addComponent(inputBox);
            inputPanel.addComponent(button);
            inputPanel.addComponent(new EmptySpace(TerminalSize.ZERO));

            Panel outputPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
            outputPanel.addComponent(new EmptySpace(TerminalSize.ONE));
            outputPanel.addComponent(outputBox);
            outputPanel.addComponent(new EmptySpace(TerminalSize.ONE));

            // Create main panel to hold components
            Panel panel = new Panel();
            panel.addComponent(new EmptySpace(TerminalSize.ONE));
            panel.addComponent(inputPanel);
            panel.addComponent(new EmptySpace(TerminalSize.ONE));
            panel.addComponent(outputPanel);
            panel.addComponent(new EmptySpace(TerminalSize.ONE));

            // Create window to hold the main panel
            BasicWindow window = new BasicWindow("icanhazdadjoke.com");
            window.setComponent(panel);
            window.setHints(List.of(Window.Hint.CENTERED));

            // Create start GUI
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen);
            guiThread = gui.getGUIThread();
            gui.addWindowAndWait(window);
        }
    }

    /**
     * Execute runnable on the GUI thread.
     */
    public void invokeLater(final Runnable runnable) {
        guiThread.invokeLater(runnable);
    }
}
