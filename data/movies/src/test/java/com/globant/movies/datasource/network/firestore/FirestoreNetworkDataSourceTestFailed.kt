package com.globant.movies.datasource.network.firestore

import com.globant.common.CategoryType
import com.globant.movies.model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class FirestoreNetworkDataSourceTestFailed {
    @MockK
    lateinit var db: FirebaseFirestore

    @InjectMockKs
    lateinit var firestoreDataSource: FirestoreNetworkDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `getUserMoviesList() should return null WHEN calls is failed`(){
        runTest {
            //Given
            val email = "email"
            val listType = CategoryType.NOW_PLAYING_MOVIES
            val expectedResult: List<MovieItem>? = null

            coEvery {
                db.collection("users").document(email).collection(listType.name).get()
            } throws mockk<TimeoutCancellationException>()

            //When
            val response = firestoreDataSource.getUserMoviesList(listType, email)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `addMovieToList() should call correctly db query`(){
        runTest {
            //Given
            val email = "email"
            val movie = mockk<MovieItem>(relaxed = true)
            val listType = CategoryType.WATCH_LIST_MOVIES
            val expectedResult = false

            coEvery {
                db.collection("users").document(email).collection(listType.name)
                    .document(movie.id.toString()).set(movie)
            } throws mockk<TimeoutCancellationException>()

            //When
            val response = firestoreDataSource.addMovieToList(movie, listType, email)

            //Then
            assertEquals(expectedResult, response)
        }
    }

    @Test
    fun `deleteMovieFromList() should call correctly db query`(){
        runTest {
            //Given
            val email = "email"
            val movieId = 123456
            val category = CategoryType.HISTORY_MOVIES
            val expectedResult = false

            coEvery {
                db.collection("users").document(email).collection(category.name)
                    .document(movieId.toString()).delete()
            } throws mockk<TimeoutCancellationException>()

            //When
            val response = firestoreDataSource.deleteMovieFromList(movieId, category, email)

            //Then
            assertEquals(expectedResult, response)
        }
    }
}