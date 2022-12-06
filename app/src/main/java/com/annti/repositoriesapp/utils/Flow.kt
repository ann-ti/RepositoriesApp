package com.annti.repositoriesapp.utils

import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun SearchView.onQueryTextChange(): Flow<String> {
    return callbackFlow {
        val textChangedListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                trySendBlocking(newText.orEmpty())
                return true
            }

        }
        this@onQueryTextChange.setOnQueryTextListener(textChangedListener)
        awaitClose {
            this@onQueryTextChange.setOnQueryTextListener(null)
        }
    }

}