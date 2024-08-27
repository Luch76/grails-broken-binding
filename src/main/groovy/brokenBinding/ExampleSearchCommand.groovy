package brokenBinding;


import grails.validation.Validateable

class ExampleSearchCommand implements Validateable {
    String name

    static boolean defaultNullable() {
        true
    }
}
