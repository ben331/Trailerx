package com.globant.movies.datasource.local.room.typeconverter

import androidx.room.TypeConverter
import com.globant.common.SyncState

class StringSyncStateConverter {
    @TypeConverter
    fun fromString(value:String): SyncState {
        return SyncState.valueOf(value)
    }

    @TypeConverter
    fun toString(syncState: SyncState): String {
        return syncState.name
    }
}