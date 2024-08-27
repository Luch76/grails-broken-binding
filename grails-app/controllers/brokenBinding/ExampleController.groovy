package brokenBinding

import grails.artefact.Controller
import grails.converters.JSON
import grails.validation.Validateable

class ExampleController implements Controller {

    private void foobar() {
        String cmd;
    }

    def list(ExampleSearchCommand cmd) {
        Map retVal = ["name": null];

        retVal.name = cmd.name;
        respond retVal;
    }

    def listWithBinding() {
        Map retVal = ["name": null];
        ExampleSearchCommand cmd = new ExampleSearchCommand();

        bindData(cmd, params)

        retVal.name = cmd.name;
        respond retVal;
    }

}




