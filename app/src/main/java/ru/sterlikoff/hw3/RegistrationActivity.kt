package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.editLogin
import kotlinx.android.synthetic.main.activity_registration.editPassword
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.models.RegistrationRequestParams
import ru.sterlikoff.hw3.components.Repository

class RegistrationActivity : MyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        buttonRegistration.setOnClickListener {

            val registrationRequest =
                RegistrationRequestParams(
                    editLogin.text.toString(),
                    editPassword.text.toString()
                )

            if (registrationRequest.validate()) {

                launch {

                    val dialog = ProgressDialog(this@RegistrationActivity).apply {
                        setMessage("Пожалуйста подождите...")
                        setTitle("Загрузка данных")
                        setCancelable(false)
                        show()
                    }

                    val response = Repository.registration(registrationRequest)

                    dialog.dismiss()

                    if (response.isSuccessful) {
                        finish()
                    } else {
                        showMessage(getString(R.string.error_label))
                    }

                }

            } else {

                registrationRequest.errors.forEach {

                    val field = when (it.key) {
                        "username" -> editLogin
                        "password" -> editPassword
                        else -> throw Exception("Неизвестная ошибка")
                    }

                    field.error = it.value

                }

            }

        }

    }

}