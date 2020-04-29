package ru.sterlikoff.hw3.components

abstract class ValidateModel {

    var errors: MutableMap<String, String> = mutableMapOf()

    abstract fun validate(): Boolean

    fun addError(key: String, error: String) {
        errors[key] = error
    }

    fun hasErrors(): Boolean {
        return errors.isNotEmpty()
    }

}