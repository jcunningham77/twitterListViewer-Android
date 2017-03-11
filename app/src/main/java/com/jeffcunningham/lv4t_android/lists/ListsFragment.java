package com.jeffcunningham.lv4t_android.lists;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.events.GetDefaultListSuccessEvent;
import com.jeffcunningham.lv4t_android.events.GetListOwnershipByTwitterUserFailureEvent;
import com.jeffcunningham.lv4t_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.lv4t_android.events.NoDefaultListPersistedEvent;
import com.jeffcunningham.lv4t_android.events.NoListOwnershipByTwitterUserEvent;
import com.jeffcunningham.lv4t_android.events.SetDefaultListEvent;
import com.jeffcunningham.lv4t_android.events.ViewListEvent;
import com.jeffcunningham.lv4t_android.list.TwitterListFragment;
import com.jeffcunningham.lv4t_android.lists.ui.ListsAdapter;
import com.jeffcunningham.lv4t_android.login.LoginActivity;
import com.jeffcunningham.lv4t_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.jeffcunningham.lv4t_android.util.ImageLoader;
import com.jeffcunningham.lv4t_android.util.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 1/18/17.
 */

public class ListsFragment extends Fragment {

    private static final String TAG = "ListsFragment";

    @BindView(R.id.listsRecyclerView)
    RecyclerView listsRecyclerView;
    @BindView(R.id.error)
    TextView tvError;
    @BindView(R.id.twitterAlias)
    TextView tvTwitterAlias;
    @BindView(R.id.twitterAvater)
    ImageView imgTwitterAvatar;

    //only used in landscape or large
    Button btnSignOut;



    ListsAdapter listsAdapter;
    private RecyclerView.LayoutManager listsLayoutManager;
    private String avatarImgUrl;

    @Inject
    ListsPresenterImpl listsPresenter;
    @Inject
    Logger logger;
    @Inject
    ImageLoader imageLoader;

    private String selectedConfiguration;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        ((LoginActivity) getActivity()).component().inject(this);
        ButterKnife.bind(this,view);

        this.selectedConfiguration = getString(R.string.selected_configuration);
        logger.info(TAG, "onCreateView: selectedConfiguration =" + selectedConfiguration);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvError.setVisibility(View.GONE);
        //handle header for wide screens
        if (this.selectedConfiguration.equalsIgnoreCase("layout-land")||this.selectedConfiguration.equalsIgnoreCase("layout-large")){
            tvTwitterAlias.setVisibility(View.GONE);
            btnSignOut = (Button)getView().findViewById(R.id.btnSignOut);
//            btnSignOut.setBackgroundResource(R.drawable.ic_account_box_white_24dp);

        }


        listsRecyclerView.setHasFixedSize(true);
        listsLayoutManager = new LinearLayoutManager(getActivity());
        listsRecyclerView.setLayoutManager(listsLayoutManager);
        listsAdapter = new ListsAdapter();
        listsRecyclerView.setAdapter(listsAdapter);
        



    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
        listsPresenter.start();

    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
        listsPresenter.stop();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetDefaultListSuccessEvent event){
        setDefaultListIdForAdapterLists(event.getDefaultList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetListOwnershipByTwitterUserSuccessEvent event){
        logger.info(TAG, "onMessageEvent(GetListOwnershipByTwitterUserSuccessEvent event)");
        listsAdapter.setTwitterLists(event.getTwitterLists());
        if (event.getTwitterLists().get(0)!=null){

            this.avatarImgUrl = event.getTwitterLists().get(0).getUser().getProfileImageUrlHttps();
            imageLoader.loadImageByUrlWithRoundedCorners(this.avatarImgUrl,imgTwitterAvatar);
            tvTwitterAlias.setText("@" + event.getTwitterLists().get(0).getUser().getScreenName());

        }
        listsPresenter.getDefaultListId();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoDefaultListPersistedEvent event){
        logger.info(TAG, "onMessageEvent: NoDefaultListPersistedEvent");
        Toast.makeText(this.getActivity(), getString(R.string.no_default_list_persisted),Toast.LENGTH_LONG).show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetListOwnershipByTwitterUserFailureEvent event){
        tvError.setText(getActivity().getApplicationContext().getString(R.string.retrieveListsError));
        tvError.setVisibility(View.VISIBLE);
        imgTwitterAvatar.setVisibility(View.INVISIBLE);
        tvTwitterAlias.setVisibility(View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoListOwnershipByTwitterUserEvent event){
        tvError.setText(getActivity().getApplicationContext().getString(R.string.noListsOwned));
        tvError.setVisibility(View.VISIBLE);
        imgTwitterAvatar.setVisibility(View.INVISIBLE);
        tvTwitterAlias.setVisibility(View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetDefaultListEvent event) {


        logger.info(TAG, "onClick: ITEM position PRESSED = " + String.valueOf(event.getPosition()));
        logger.info(TAG, "onClick: List Name = " + event.getSlug());
        logger.info(TAG, "onClick: List ID = " + event.getListId());


        listsPresenter.persistDefaultListId(event.getListId(),event.getSlug(),event.getListName());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewListEvent event){

        //only launch TwitterListActivity if we are in layout (phone) configuration
        //in tablet config (layout-large), TwitterListFragment is also listening to this event and
        //can refresh itself
        logger.info(TAG,"onMessageEvent - ViewListEvent, this.selectedConfiguration: " + this.selectedConfiguration);
        if (this.selectedConfiguration.equalsIgnoreCase("layout")){
//            Intent listIntent = new Intent(getActivity(), TwitterListActivity.class);
//            listIntent.putExtra("slug",event.getSlug());
//            listIntent.putExtra("listName",event.getListName());
//            listIntent.putExtra("twitterAvatarImageUrl",this.avatarImgUrl);
//            startActivity(listIntent);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            TwitterListFragment twitterListFragment = new TwitterListFragment();
            Bundle bundle = new Bundle();

            bundle.putString("slug",event.getSlug());
            bundle.putString("listName",event.getListName());
            twitterListFragment.setArguments(bundle);
            ft.replace(R.id.fragment_container, twitterListFragment,"TwitterListFragment");
            ft.addToBackStack(null);
            ft.commit();
        }


    }



    private void setDefaultListIdForAdapterLists(DefaultList defaultList){
        for (TwitterList twitterList: listsAdapter.getTwitterLists()){
            logger.info(TAG, "setDefaultListIdForAdapterLists: default list id =   " + defaultList.getListId() +  ", this list id = " + twitterList.getIdStr());
            if (defaultList.getListId().equalsIgnoreCase(twitterList.getIdStr())){
                twitterList.setDefaultList(true);
                logger.info(TAG, "setting this twitterList to default = true ");
            } else {
                twitterList.setDefaultList(false);
            }
            listsAdapter.notifyDataSetChanged();
        };
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (logger!=null){//logger may be null if we are removing this fragment from portait container on orientation change
            logger.info(TAG, "onDestroy: ");
        }

        listsRecyclerView = null;
    }
}