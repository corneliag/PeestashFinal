package com.blinky.peestash.app;

/**
 * Created by nelly on 15/04/2015.
 */
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.blinky.peestash.app.R;

public class EventsEtbFragment extends Fragment {

    public EventsEtbFragment(){}

    String id_user = "";
    Button BtnAddEvent, BtnGetEvents;
    private TextView Demande_inscription, Nb_events;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_etb_events, container, false);

        BtnAddEvent = (Button) rootView.findViewById(R.id.btnAddEvent);
        BtnGetEvents = (Button) rootView.findViewById(R.id.btnGetEvents);
        Demande_inscription = (TextView) rootView.findViewById(R.id.DemandeInscription);
        Nb_events = (TextView) rootView.findViewById(R.id.nbEvents);

        Bundle var = getActivity().getIntent().getExtras();
        id_user = var.getString("id_user");

         BtnAddEvent.setOnClickListener(new View.OnClickListener() {

          @Override
            public void onClick(View v) {

              Intent intent = new Intent(getActivity(), AddEventActivity.class);
                intent.putExtra("id_user", id_user);
                startActivity(intent);
              /*
              Fragment newFragment = new AddEventFragment();
              Bundle bundle = new Bundle();
              bundle.putString("id_user", id_user);
              newFragment.setArguments(bundle);
              FragmentTransaction transaction = getFragmentManager().beginTransaction();
              transaction.replace(R.id.frame_container, newFragment);
              transaction.addToBackStack(null);
              transaction.commit();*/
            }
        });

/*
        BtnGetEvents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), GetEventsActivity.class);
                intent.putExtra("id_user", id_user);
                startActivity(intent);

            }
        });*/

        return rootView;
    }
}