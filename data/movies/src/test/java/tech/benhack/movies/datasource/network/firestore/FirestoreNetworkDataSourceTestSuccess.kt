package tech.benhack.movies.datasource.network.firestore

import tech.benhack.common.CategoryType
import tech.benhack.movies.model.MovieItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class FirestoreNetworkDataSourceTestSuccess {
    @MockK
    lateinit var db: FirebaseFirestore

    @InjectMockKs
    lateinit var firestoreDataSource: FirestoreNetworkDataSource

    @Before
    fun setup(){
        MockKAnnotations.init(this)
    }

    @Test
    fun `addMovieToList() should call correctly db query`(){
        runTest {
            //Given
            val email = "email"
            val movie = mockk<MovieItem>(relaxed = true)
            val listType = CategoryType.WATCH_LIST_MOVIES
            val expectedResult = true

            coEvery {
                db.collection("users").document(email).collection(listType.name)
                    .document(movie.id.toString()).set(movie)
            } returns mockTask<Void>(mockk())

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
            val expectedResult = true

            coEvery {
                db.collection("users").document(email).collection(category.name)
                    .document(movieId.toString()).delete()
            } returns mockTask<Void>(mockk())

            //When
            val response = firestoreDataSource.deleteMovieFromList(movieId, category, email)

            //Then
            assertEquals(expectedResult, response)
        }
    }
}