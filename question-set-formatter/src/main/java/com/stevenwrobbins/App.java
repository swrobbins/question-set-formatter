package com.stevenwrobbins;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class App {
    public App() {
    }

    public static void main(String[] args) {
        App app = new App();
        app.readFile();
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

    public void processLine(String line) {
        loadContext.line(line);

        while (loadContext.state().process(loadContext)) {
            ;
        }
    }

    public void readFile() {
        try (Stream<String> stream = Files.lines(Paths.get(this.getClass().getResource("/team-care-product.txt").getFile()),
                Charset.defaultCharset())) {
            stream.forEach(line -> processLine(line));
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    enum States implements State {
        START {
            public boolean process(Context context) {
                if (context.line().startsWith("*")) {
                    context.state(States.SYMPTOM);
                    return true;
                }
                ;

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
        };
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

