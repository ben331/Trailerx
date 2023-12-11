package com.globant.imdb.data.database.entities.movie

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import com.globant.imdb.data.database.typeConverters.StringCategoryTypeConverter
import com.globant.imdb.data.database.typeConverters.StringSyncStateConverter
import com.globant.imdb.domain.model.SyncCategoryMovieItem

fun SyncCategoryMovieItem.toDatabase(): SyncCategoryMovieEntity{
    return SyncCategoryMovieEntity(idMovie, idCategory, syncState)
}

enum class SyncState {
    PENDING_TO_ADD,
    PENDING_TO_DELETE
}


@Entity(
    tableName = "sync_category_movie",
    primaryKeys = ["idMovie", "idCategory"],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["id"],
            childColumns = ["idMovie"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(StringCategoryTypeConverter::class)
data class SyncCategoryMovieEntity(
    @ColumnInfo(index = true)       val idMovie: Int,
    @ColumnInfo(index = true)       val idCategory: CategoryType,
    @TypeConverters(StringSyncStateConverter::class)
    @ColumnInfo("sync_state") val syncState:SyncState
)