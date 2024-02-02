package com.globant.movies.datasource.network.firestore

import com.globant.common.CategoryType
import com.globant.movies.model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class FirestoreNetworkDataSourceTestCalls {
    @MockK
    lateinit var db: FirebaseFirestore

    @InjectMockKs
    lateinit var firestoreDataSource: FirestoreNetworkDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `getUserMoviesList() should call correctly db query`(){
        runTest {
            //Given
            val email = "email"
            val listType = CategoryType.NOW_PLAYING_MOVIES
            val query: QuerySnapshot = mockk()

            coEvery {
                db.collection("users").document(email).collection(listType.name).get()
            } returns mockTask<QuerySnapshot>(query)

            //When
            firestoreDataSource.getUserMoviesList(listType, email)

            //Then
            coVerify { db.collection("users").document(email).collection(listType.name).get() }
        }
    }

    @Test
    fun `addMovieToList() should call correctly db query`(){
        runTest {
            //Given
            val email = "email"
            val movie = mockk<MovieItem>(relaxed = true)
            val listType = CategoryType.WATCH_LIST_MOVIES

            coEvery {
                db.collection("users").document(email).collection(listType.name)
                    .document(movie.id.toString()).set(movie)
            } returns mockTask<Void>(mockk())

            //When
            firestoreDataSource.addMovieToList(movie, listType, email)

            //Then
            coVerify {
                db.collection("users").document(email).collection(listType.name)
                    .document(movie.id.toString()).set(movie)
            }
        }
    }

    @Test
    fun `deleteMovieFromList() should call correctly db query`(){
        runTest {
            //Given
            val email = "email"
            val movieId = 123456
            val category = CategoryType.HISTORY_MOVIES

            coEvery {
                db.collection("users").document(email).collection(category.name)
                    .document(movieId.toString()).delete()
            } returns mockTask<Void>(mockk())

            //When
            firestoreDataSource.deleteMovieFromList(movieId, category, email)

            //Then
            coVerify {
                db.collection("users").document(email).collection(category.name)
                    .document(movieId.toString()).delete()
            }
        }
    }
}