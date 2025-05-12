package com.kwasow.musekit.store

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.kwasow.musekit.MusekitPreferences
import com.kwasow.musekit.musekitPreferences
import java.io.InputStream
import java.io.OutputStream

object MusekitPreferencesSerializer : Serializer<MusekitPreferences> {
    override val defaultValue: MusekitPreferences =
        musekitPreferences {
            nightMode = -1
            noteForkMode = -1
            automaticTunerPitch = -1
            metronomeBpm = -1
            notationStyle = -1
        }

    override suspend fun readFrom(input: InputStream): MusekitPreferences {
        try {
            return MusekitPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: MusekitPreferences, output: OutputStream) =
        t.writeTo(output)
}

val Context.musekitPreferencesDataStore: DataStore<MusekitPreferences> by dataStore(
    fileName = "musekit_preferences.pb",
    serializer = MusekitPreferencesSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler { MusekitPreferencesSerializer.defaultValue },
)
