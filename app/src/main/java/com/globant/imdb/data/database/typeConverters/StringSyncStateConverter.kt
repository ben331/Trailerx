package com.globant.imdb.data.database.typeConverters

import androidx.room.TypeConverter
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.SyncState

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