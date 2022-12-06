package com.annti.repositoriesapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Context.openFile(uri: Uri, mimeType: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(intent)
}

class AppError(
    val code: Code,
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    enum class Code {
        SERVER_ERROR,
        INTERNAL_ERROR,
        TIMEOUT,
        NETWORK_CONNECTION,
        INVALID_LOGIN_OR_PASSWORD,
        SESSION_EXPIRED,
        MESSAGE
    }
}

suspend fun <T> handleNetworkErrors(block: suspend () -> T): T =
    try {
        block()
    } catch (e: UnknownHostException) {
        throw AppError(AppError.Code.NETWORK_CONNECTION, "Нет подключения к интернету", e)
    } catch (e: SocketTimeoutException) {
        throw AppError(AppError.Code.TIMEOUT, "Превышено время ожидания ответа", e)
    } catch (e: HttpException) {
        when (e.code()) {
            404 -> throw AppError(AppError.Code.SERVER_ERROR, "Ресурс не найден", e)
            else -> {
                Log.e("LK", "Network error", e)
                throw AppError(AppError.Code.SERVER_ERROR, "Сетевая ошибка: $e", e)
            }
        }
    } catch (e: AppError) {
        throw e
    } catch (e: Throwable) {
        Log.e("LK", "Unknown error", e)
        throw AppError(AppError.Code.SERVER_ERROR, "Неизвестная ошибка: $e", e)
    }

fun showErrorToUser(view: View, text: String) {
    Snackbar.make(
        view,
        text,
        Snackbar.LENGTH_LONG
    ).show()
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}