package controller.localdate.test

import grails.converters.JSON
import groovy.util.logging.Slf4j

import java.time.LocalDate
import java.time.ZoneId

@Slf4j
class BirthdayController {
    static responseFormats = ['json']

    /**
     * Try using {@link java.time.LocalDate}
     *
     * This dies in https://github.com/grails/grails-core/blob/master/grails-plugin-controllers/src/main/groovy/grails/artefact/Controller.groovy#L395
     * when trying to invoke `.newInstance()` on a LocalDate
     */
    def birthdayLocalDate(LocalDate birthday) {
        if (!birthday) {
            birthday = LocalDate.now()
        }

        log.info("Birthday is {}", birthday)
        List<User> actorsBornToday = User.findAllByBirthday(birthday)
        render actorsBornToday as JSON
    }

    /**
     * Try using {@link java.time.LocalDate} inside a command
     *
     * This bypasses the problem for using a LocalDate explicitly, but then fails data binding due to
     * https://github.com/grails-plugins/grails-java8/issues/7
     */
    def birthdayLocalDateCommand(LocalDateCommand command) {
        if (!command.birthday) {
            command.birthday = LocalDate.now()
        }

        log.info("Birthday is {}", command.birthday)
        List<User> actorsBornToday = User.findAllByBirthday(command.birthday)
        render actorsBornToday as JSON
    }

    /**
     * Try using {@link java.util.Date}
     *
     * Here just to prove something works :)
     *
     * Incidentally, because java.util.Date() constructor is effectively a .now(), the value here might
     * not be what is expected by the developer as it will always have a value, but not necessarily the provided value
     */
    def birthdayDate(Date birthday) {
        if (!birthday) {
            birthday = new Date()
        }

        LocalDate localDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

        log.info("Birthday is {}", birthday)
        log.info("localDate is {}", localDate)

        List<User> actorsBornToday = User.findAllByBirthday(localDate)
        render actorsBornToday as JSON
    }
}

/**
 * Simple command object to wrap a LocalDate
 */
class LocalDateCommand {
    LocalDate birthday
}
