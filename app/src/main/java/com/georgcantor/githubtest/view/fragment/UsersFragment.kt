package com.georgcantor.githubtest.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.georgcantor.githubtest.R
import com.georgcantor.githubtest.model.data.UserEntry
import com.georgcantor.githubtest.utils.DisposableManager
import com.georgcantor.githubtest.utils.EndlessRecyclerViewScrollListener
import com.georgcantor.githubtest.utils.openFragment
import com.georgcantor.githubtest.utils.shortToast
import com.georgcantor.githubtest.view.adapter.UsersAdapter
import com.georgcantor.githubtest.viewmodel.UsersViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_users.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class UsersFragment : Fragment() {

    companion object {
        const val PREFERENCES = "preferences"
        const val ACCOUNT = "account"
        const val USER_NAME = "user_name"
        const val IMAGE_URL = "image_url"
    }
    private lateinit var adapter: UsersAdapter
    private lateinit var viewModel: UsersViewModel
    private lateinit var manager: InputMethodManager
    private lateinit var preferences: SharedPreferences
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var name: String
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel { parametersOf() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_users, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), signInOptions)

        preferences = requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val account = arguments?.getParcelable<Parcelable>(ACCOUNT) as? GoogleSignInAccount
        account?.displayName?.let { preferences.edit().putString(USER_NAME, it).apply() }
        account?.photoUrl?.let { preferences.edit().putString(IMAGE_URL, it.toString()).apply() }

        name = if (account?.displayName == null) {
            preferences.getString(USER_NAME, "").toString()
        } else {
            account.displayName.toString()
        }

        url = if (account?.photoUrl == null) {
            preferences.getString(
                IMAGE_URL,
                "https://www.pexels.com/photo/nature-red-love-romantic-67636/"
            ).toString()
        } else {
            account.photoUrl.toString()
        }

        manager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        toolbar.setNavigationOnClickListener {
            requireActivity().drawerLayout.openDrawer(Gravity.LEFT)
            manager.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
        }

        requireActivity().logout_button.setOnClickListener {
            requireActivity().drawerLayout.closeDrawer(Gravity.LEFT)
            googleSignInClient.signOut()
            val activity = context as AppCompatActivity
            activity.openFragment(SignInFragment())
        }

        setupNavigationView()
        setupRecyclerView()
        setupEditText()
    }

    private fun setupNavigationView() {
        requireActivity().nameTextView.text = name

        Picasso.with(context)
            .load(url)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(requireActivity().userImageView)
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(
            3,
            StaggeredGridLayoutManager.VERTICAL
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = gridLayoutManager
        adapter = UsersAdapter(requireContext())
        recyclerView.adapter = adapter

        val scrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadUsers(searchEditText.text.toString().trim { it <= ' ' }, page)
            }
        }
        scrollListener.resetState()
        recyclerView.addOnScrollListener(scrollListener)

        refreshLayout.setOnRefreshListener {
            loadUsers(searchEditText.text.toString().trim { it <= ' ' }, 1)
            refreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("CheckResult")
    private fun setupEditText() {
        searchEditText.requestFocus()
        val publishSubject = PublishSubject.create<Int>()
        publishSubject
            .debounce(600, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.clearUsers()
                loadUsers(searchEditText.text.toString().trim { it <= ' ' }, 1)
            }

        searchEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                publishSubject.onNext(0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun loadUsers(query: String, page: Int) {
        val disposable = viewModel.getUsers(query, page, 30)
            .retry(3)
            .doOnSubscribe {
                refreshLayout.isRefreshing = true
            }
            .doOnEach {
                refreshLayout.isRefreshing = false
            }
            .subscribe({
                adapter.setUsers(it as MutableList<UserEntry>)
            }, {
                requireActivity().shortToast(it.message.toString())
            })

        DisposableManager.add(disposable)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().finish()
    }

    override fun onDestroy() {
        DisposableManager.dispose()
        super.onDestroy()
    }

}