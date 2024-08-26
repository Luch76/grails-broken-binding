package brokenBinding

import grails.artefact.Controller
import grails.converters.JSON
import grails.validation.Validateable

class ExampleController implements Controller {
    def index() {
        redirect(action: 'list'); //chain
    }

    def list(ExampleSearchCommand cmd) {
        Map retVal = ["works": "yes"];
        String s = cmd.getClass().getName();
        if (s?.contains("Example")) {
            System.out.println("s is: " + s);
        }
        Class x = cmd.getClass();

        render retVal as JSON;
    }

    def edit() {
        ExampleCommand cmdl = new ExampleCommand() // TODO: Renaming this command variable will allow the list action to work
        render "OK";
    }
}

class ExampleSearchCommand implements Validateable {
    String name

    static boolean defaultNullable() {
        true
    }
}

class ExampleCommand implements Validateable {
    String name
}
