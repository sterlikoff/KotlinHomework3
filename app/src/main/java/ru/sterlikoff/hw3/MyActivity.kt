package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

const val AUTHENTICATED_SHARED_KEY = "authenticated_shared_key"
const val API_SHARED_FILE = "API_shared_file"

abstract class MyActivity: AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null

    protected fun showProgress() {

        if (dialog == null) {
            dialog = ProgressDialog(this)
        }

        dialog?.apply {
            setMessage("Пожалуйста подождите...")
            setTitle("Загрузка данных")
            setCancelable(false)
            show()
        }

    }

    protected fun hideProgress() {
        dialog?.dismiss()
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    protected fun getToken(): String? =
        getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE).getString(AUTHENTICATED_SHARED_KEY, "")

    protected fun isAuthenticated() = getToken()?.isNotEmpty() ?: false

    protected fun setUserAuth(token: String) =

        getSharedPreferences(API_SHARED_FILE, MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .commit()

}