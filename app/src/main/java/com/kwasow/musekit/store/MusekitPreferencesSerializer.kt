package com.kwasow.musekit.store

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.kwasow.musekit.MusekitPreferences
import com.kwasow.musekit.data.MetronomeSounds
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.musekitPreferences
import java.io.InputStream
import java.io.OutputStream

object MusekitPreferencesSerializer : Serializer<MusekitPreferences> {
    override val defaultValue: MusekitPreferences =
        musekitPreferences {
            nightMode = when (Build.VERSION.SDK_INT >= 29) {
                true -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
            noteForkMode = 0
            notationStyle = NotationStyle.English.id

            automaticTunerPitch = 440
            metronomeBpm = 60
            metronomeSound = MetronomeSounds.Default.ordinal
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
