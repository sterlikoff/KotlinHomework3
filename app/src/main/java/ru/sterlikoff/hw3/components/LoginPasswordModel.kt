package ru.sterlikoff.hw3.components

abstract class LoginPasswordModel(
    private val username: String,
    private val password: String
) : ValidateModel() {

    override fun validate(): Boolean {

        errors.clear()

        if (username.isEmpty()) {
            addError("username", "Логин не может быть пустым")
        }

        if (username.length > 10) {
            addError("username", "Максимальная длина логина - 10 символов")
        }

        if (password.isEmpty()) {
            addError("password", "Пароль не может быть пустым")
        }

        if (password.length < 6) {
            addError("password", "Минимальная длина пароля - 6 символов")
        }

        if (password.length > 15) {
            addError("password", "Максимальная длина пароля - 15 символов")
        }

        return !hasErrors()

    }

}