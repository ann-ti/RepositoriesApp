package com.annti.repositoriesapp.domain

import android.R
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.annti.repositoriesapp.data.model.RepositoryDownload
import com.annti.repositoriesapp.utils.AppError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


interface FileManager {

    fun writeFile(file: File, fileBytes: ByteArray)
    fun createFolder(path: String)
    fun writeToPDF(filename: String, data: ByteArray): Uri
    fun getDownloadRepos(): Flow<List<RepositoryDownload>>
}

class FileManagerImpl(private val context: Context) : FileManager {

    val applicationDirectory: String = context.filesDir.absolutePath

    override fun writeFile(file: File, fileBytes: ByteArray) {
        if (!file.exists())
            file.createNewFile()

        FileOutputStream(file).use { stream ->
            stream.write(fileBytes)
            stream.close()
        }
    }

    override fun createFolder(path: String) {
        val folder = File(path)
        if (!folder.exists()) {
            folder.mkdir()
        }
    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun getDownloadRepos(): Flow<List<RepositoryDownload>> {
        val repos = mutableListOf<RepositoryDownload>()
            context.contentResolver.query(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Downloads._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Downloads.DISPLAY_NAME))
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id)

                    repos += RepositoryDownload(id, name, uri)
                }
        }
        return flowOf(repos)
    }

    override fun writeToPDF(filename: String, data: ByteArray): Uri {
        val resolver = context.contentResolver
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/zip")
        values.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            "${Environment.DIRECTORY_DOWNLOADS}/repositories_github"
        )
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
            ?: throw AppError(AppError.Code.MESSAGE, "Не удалось получить URL файла для сохранения")
        resolver.openOutputStream(uri!!, "wt")?.use { stream ->
            stream.write(data)
        } ?: throw AppError(AppError.Code.MESSAGE, "Не удалось сохранить файл")
        return uri
    }

}