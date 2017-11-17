package controller.localdate.test

class UrlMappings {

    static mappings = {
        get "/localDate/birthdays"(controller: "birthday", action: "birthdayLocalDate")
        get "/command/birthdays"(controller: "birthday", action: "birthdayLocalDateCommand")
        get "/date/birthdays"(controller: "birthday", action: "birthdayDate")
    }
}
