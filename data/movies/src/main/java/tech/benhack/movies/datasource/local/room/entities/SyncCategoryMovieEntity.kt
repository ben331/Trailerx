package tech.benhack.movies.datasource.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import tech.benhack.common.CategoryType
import tech.benhack.common.SyncState
import tech.benhack.movies.datasource.local.room.typeconverter.StringCategoryTypeConverter
import tech.benhack.movies.datasource.local.room.typeconverter.StringSyncStateConverter

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
    @ColumnInfo("sync_state") val syncState: SyncState
)