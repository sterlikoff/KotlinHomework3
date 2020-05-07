package ru.sterlikoff.hw3

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

const val AUTHENTICATED_SHARED_KEY = "authenticated_shared_key"
const val API_SHARED_FILE = "API_shared_file"

interface ActivityUI {

    var dialog: ProgressDialog?

    fun showProgress(context: Context) {

        if (dialog == null) {
            dialog = ProgressDialog(context)
        }

        dialog?.apply {
            setMessage(context.getString(R.string.please_wait_label))
            setTitle(context.getString(R.string.loading_data_label))
            setCancelable(false)
            show()
        }

    }


    fun hideProgress() {
        dialog?.dismiss()
    }

    fun getToken(context: Context): String? =
        context.getSharedPreferences(API_SHARED_FILE, AppCompatActivity.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY,
            ""
        )

    fun isAuthenticated(context: Context) = getToken(context)?.isNotEmpty() ?: false

    fun setUserAuth(token: String, context: Context) =
        context.getSharedPreferences(API_SHARED_FILE, AppCompatActivity.MODE_PRIVATE).edit {
            putString(AUTHENTICATED_SHARED_KEY, token)
        }

    fun logout(context: Context) =
        context.getSharedPreferences(API_SHARED_FILE, AppCompatActivity.MODE_PRIVATE).edit {
            remove(AUTHENTICATED_SHARED_KEY)
        }


}