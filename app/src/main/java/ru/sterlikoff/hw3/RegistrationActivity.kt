package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.editLogin
import kotlinx.android.synthetic.main.activity_registration.editPassword
import kotlinx.coroutines.launch
import ru.sterlikoff.hw3.models.RegistrationRequestParams
import ru.sterlikoff.hw3.components.Repository
import splitties.toast.toast

class RegistrationActivity : AppCompatActivity(R.layout.activity_registration), ActivityUI {

    override var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        buttonRegistration.setOnClickListener {

            val registrationRequest =
                RegistrationRequestParams(
                    editLogin.text.toString(),
                    editPassword.text.toString()
                )

            if (registrationRequest.validate()) {

                lifecycleScope.launch {

                    showProgress(this@RegistrationActivity)

                    try {

                        if (Repository.registration(registrationRequest).isSuccessful) {
                            finish()
                        } else {
                            toast(getString(R.string.error_label))
                        }

                    } catch (e: Exception) {
                        toast(R.string.connection_error_label)
                    } finally {
                        hideProgress()
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