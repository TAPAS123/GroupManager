package group.manager;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTabs extends Fragment {

    /*TabLayout tabLayout;
    ViewPager viewPager;
    Adapter_ViewPager adapter_viewPager;
    String Str_club,Logname,Logclubid,LogclubName,Tab2Name="";
    Bundle bundle;*/

    public FragmentTabs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);
        /*tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        adapter_viewPager = new Adapter_ViewPager(getChildFragmentManager());

        Logname = this.getArguments().getString("Clt_Log");
        Logclubid = this.getArguments().getString("Clt_LogID");
        LogclubName = this.getArguments().getString("Clt_ClubName");
        Str_club = this.getArguments().getString("UserClubName");*/


       /* bundle = new Bundle();
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("Clt_ClubName",LogclubName);
        bundle.putString("UserClubName",Str_club);
        Fragment fragmentHome = new FragmentHome();
        fragmentHome.setArguments(bundle);
        adapter_viewPager.addFragment(fragmentHome, "Home");

        bundle = new Bundle();
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("UserClubName",Str_club);
        Fragment fragmentaboutus = new Fragment_Aboutus();
        fragmentaboutus.setArguments(bundle);
        adapter_viewPager.addFragment(fragmentaboutus,"About Us");


        bundle = new Bundle();
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("Clt_ClubName",LogclubName);
        bundle.putString("UserClubName",Str_club);
        Fragment Frg_Edu_Content = new Fragment_Education_Content();
        Frg_Edu_Content.setArguments(bundle);
        adapter_viewPager.addFragment(Frg_Edu_Content,"Incubation");


//        bundle = new Bundle();
//        bundle.putInt("Count",999999);
//        bundle.putInt("POstion",0);
//        bundle.putString("Clt_Log",Logname);
//        bundle.putString("Clt_LogID",Logclubid);
//        bundle.putString("Clt_ClubName",LogclubName);
//        bundle.putString("UserClubName",Str_club);
//        Fragment fragmentwhatsnew = new FragmentWhatsnew();
//        fragmentwhatsnew.setArguments(bundle);
//        adapter_viewPager.addFragment(fragmentwhatsnew,"News");

        bundle = new Bundle();
        bundle.putString("Eventschk","1");
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("Clt_ClubName",LogclubName);
        bundle.putString("UserClubName",Str_club);
        Fragment fragmentevents = new FragmentICSIEvents();
        fragmentevents.setArguments(bundle);
        adapter_viewPager.addFragment(fragmentevents, "Events");


        bundle = new Bundle();
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("Clt_ClubName",LogclubName);
        bundle.putString("UserClubName",Str_club);
        Fragment frag_DirectoryMain = new Fragment_Directory_Main();
        frag_DirectoryMain.setArguments(bundle);
        adapter_viewPager.addFragment(frag_DirectoryMain,"Directory");

        bundle = new Bundle();
        bundle.putString("MTitle","Opinion Poll");
        bundle.putString("ComeFrom","1");
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("UserClubName",Str_club);
        Fragment fragmentOpPoll = new Fragment_OpinionPoll();
        fragmentOpPoll.setArguments(bundle);
        adapter_viewPager.addFragment(fragmentOpPoll, "Opinion Poll / Quiz");


        bundle = new Bundle();
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("Clt_ClubName",LogclubName);
        bundle.putString("UserClubName",Str_club);
        Fragment fragmentSug = new FragmentSuggestion();
        fragmentSug.setArguments(bundle);
        adapter_viewPager.addFragment(fragmentSug, "Suggestions");


        bundle = new Bundle();
        bundle.putString("NEType","Event");
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("UserClubName",Str_club);
        Fragment fragmentgallery = new Fragment_Gallery();
        fragmentgallery.setArguments(bundle);
        adapter_viewPager.addFragment(fragmentgallery, "Gallery");



        Bundle bundle = new Bundle();
        bundle.putString("Clt_Log",Logname);
        bundle.putString("Clt_LogID",Logclubid);
        bundle.putString("UserClubName",Str_club);
        Fragment fragmentContactus = new ContactUs();
        fragmentContactus.setArguments(bundle);
        adapter_viewPager.addFragment(fragmentContactus, "Contact Us");

        viewPager.setAdapter(adapter_viewPager);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();*/

        return  rootView;
    }

   /* private void changeTabsFont() {
        Typeface face=Typeface.createFromAsset(getContext().getAssets(), "calibri.ttf");
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(face);
                }
            }
        }
    }*/
}
