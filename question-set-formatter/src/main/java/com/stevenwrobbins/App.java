package com.stevenwrobbins;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws URISyntaxException, IOException, Docx4JException {
        App app = new App();
        app.readFile();
        app.createWordFile();
    }

    private void createWordFile() throws Docx4JException {
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File("question-set-template.docx"));
        wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Title", "New Title!");
        wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Subtitle", "New Subtitle!");
        wordMLPackage.getMainDocumentPart().addStyledParagraphOfText("Heading1", "New Question!");
        wordMLPackage.save(new java.io.File("HelloWord.docx"));
    }

    private Context loadContext = new Context() {
        private String line;
        private State state = States.START;

        @Override
        public void line(String line) {
            this.line = line;
        }

        @Override
        public String line() {
            return line;
        }

        @Override
        public State state() {
            return state;
        }

        @Override
        public void state(State state) {
            this.state = state;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    public void processLine(String line) {
        loadContext.line(line);

        while (loadContext.state().process(loadContext)) {}
    }

    public void readFile() throws URISyntaxException, IOException {
        URL fileUrl = App.class.getResource("/team-care-product.txt");

        // WORKAROUND: SROBBINS -- Windows gets leading '/' that Files.lines() doesn't like without this.
        Path filePath = Paths.get(new URI(fileUrl.toExternalForm()));

        try (Stream<String> stream = Files.lines(filePath, Charset.defaultCharset())) {
            stream.forEach(this::processLine);
        }
    }

    enum States implements State {
        START {
            public boolean process(Context context) {
                if (context.line().startsWith("*")) {
                    context.state(States.SYMPTOM);
                    return true;
                }

                return false;
            }
        },

        SUB_QUESTION {
            public boolean process(Context context) {
                if (context.line().startsWith("\t")) {
                    System.out.println("SUBQUESTION: " + context.line());
                    return false;
                }

                if (!context.line().startsWith("*")) {
                    context.state(States.QUESTION);
                    return true;
                }

                context.state(States.SYMPTOM);

                return true;
            }
        },

        QUESTION {
            public boolean process(Context context) {
                if (!context.line().startsWith("*") && !context.line().startsWith("\t")) {
                    System.out.println("QUESTION: " + context.line());
                    return false;
                }

                if (context.line().startsWith("\t")) {
                    context.state(States.SUB_QUESTION);
                    return true;
                }

                context.state(States.SYMPTOM);

                return true;
            }
        },

        SYMPTOM {
            public boolean process(Context context) {
                if (context.line().startsWith("*")) {
                    System.out.println("SYMPTOM: " + context.line());
                    return false;
                }

                context.state(States.QUESTION);

                return true;
            }
        }
    }

    interface Context {
        void line(String line);

        String line();

        State state();

        void state(State state);
    }

    interface State {
        /**
         * @return true to keep processing, false to read more data.
         */
        boolean process(Context context);
    }
}

