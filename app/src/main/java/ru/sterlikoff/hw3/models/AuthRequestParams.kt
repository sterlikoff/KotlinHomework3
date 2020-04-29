package ru.sterlikoff.hw3.models

import ru.sterlikoff.hw3.components.LoginPasswordModel

class AuthRequestParams(username: String, password: String) : LoginPasswordModel(
    username,
    password
)