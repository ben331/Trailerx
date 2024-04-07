package tech.benhack.home.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tech.benhack.common.CategoryType
import tech.benhack.home.R
import tech.benhack.home.databinding.FragmentProfileBinding
import tech.benhack.home.model.SectionProfileItem
import tech.benhack.home.view.components.MovieProfileListener
import tech.benhack.home.view.screens.ProfileScreen
import tech.benhack.home.viewmodel.ProfileViewModel
import tech.benhack.ui.helpers.DialogManager
import tech.benhack.ui.theme.TrailerxTheme
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment :
    Fragment(),
    MovieProfileListener
{
    private val profileViewModel:ProfileViewModel by viewModels()

    private lateinit var logoutListener: SettingsFragment.LogoutListener

    @Inject
    lateinit var dialogManager: DialogManager

    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.profileComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                val watchList by profileViewModel.watchList.observeAsState(initial = emptyList())
                val history by profileViewModel.recentViewed.observeAsState(initial = emptyList())

                TrailerxTheme {
                    ProfileScreen(
                        isGuest = profileViewModel.username.isEmpty(),
                        onLogout = ::logout,
                        sections = listOf(
                            SectionProfileItem(
                                title = stringResource(id = R.string.watch_list),
                                categoryType = CategoryType.WATCH_LIST_MOVIES,
                                movies = watchList ?: emptyList(),
                                showDescription = watchList?.isEmpty() ?: false,
                                description = stringResource(id = R.string.create_watch_list),
                                showBtn = watchList?.isEmpty() ?: false,
                                textButton = stringResource(id = R.string.start_watch_list),
                                onClick = {
                                    val action =
                                        ProfileFragmentDirections.actionProfileFragmentToHomeFragment()
                                    findNavController().navigate(action)
                                }
                            ),
                            SectionProfileItem(
                                title = stringResource(id = R.string.recently_viewed),
                                categoryType = CategoryType.HISTORY_MOVIES,
                                movies = history ?: emptyList(),
                                showDescription = history?.isEmpty() ?: false,
                                description = stringResource(id = R.string.content_recently_viewed),
                            )
                        ),
                        profileImgUrl = profileViewModel.photoUri.value.toString(),
                        onMenuClick = {
                            val action =
                                ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
                            navController.navigate(action)
                        },
                        listener = this@ProfileFragment
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        binding.profileComposeView.disposeComposition()
        profileViewModel.refresh(requireContext())
    }

    private fun setupButtons(){
        profileViewModel.isLoading.observe(viewLifecycleOwner){
            binding.refreshLayout.isRefreshing = it
        }

        binding.refreshLayout.setOnRefreshListener {
            profileViewModel.refresh(requireContext())
        }
    }

    private fun logout(){
        val parent = parentFragment?.parentFragment as NavigationFragment
        logoutListener = parent
        navController.popBackStack(R.id.home_nav_graph, true)
        logoutListener.logout()
    }

    override fun showDetails(id: Int) {
        val action = ProfileFragmentDirections.actionProfileFragmentToMovieFragment(id)
        navController.navigate(action)
    }

    override fun bookmarkAction(id: Int, category: CategoryType) {
        profileViewModel.deleteMovieFromList(id, category) { isDeleted ->
            if(isDeleted){
                profileViewModel.refresh(requireContext())
            }else{
                handleFailure(R.string.error, R.string.delete_movie_error)
            }
        }
    }

    override fun showInfo(id: Int) { }

    private fun handleFailure(title:Int, msg:Int){
        profileViewModel.isLoading.postValue(false)
        dialogManager.showAlert(requireContext(),title, msg)
    }
}