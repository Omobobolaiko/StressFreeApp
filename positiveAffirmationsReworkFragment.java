package com.example.baads.positiveThoughts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.baads.R;
import com.example.baads.databinding.ActivityPositiveThoughtsBinding;


/**positiveAffrmationsReworkFragment
 * Class that deals with setting page functionality and allowing the creation of threads to run notifications in backgrounds.
 *
 * source: https://www.tutorialspoint.com/how-to-create-a-thread-in-java
 * Dealing with creation of threads in java
 *
 * https://www.youtube.com/watch?v=xSrVWFCtgaE
 * Followed their tutorial to create notifications for user.
 * Modified code in order to work with threads and no longer use receivers
 *
 */
public class positiveAffirmationsReworkFragment extends Fragment {

    private ActivityPositiveThoughtsBinding binding;
    public static boolean isSwitchFlipped = false;
    private positiveAffirmationsThread thread;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ActivityPositiveThoughtsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Resets page back to user saved settings.
    private void resetPageToSave() {
        Switch thoughtEnabler = getActivity().findViewById(R.id.positiveThoughtEnabler);
        thoughtEnabler.setChecked(isSwitchFlipped);
    }

    /**buttonAction()
     *
     * @throws InterruptedException
     * Source: https://www.tutorialspoint.com/how-to-create-a-thread-in-java
     *  Threads in java
     * Source: https://www.youtube.com/watch?v=xSrVWFCtgaE
     *  Notifications
     *
     *  Combined both of these so that notifications could be used in threads.
     */
    private void buttonAction() throws InterruptedException {
        isSwitchFlipped = !isSwitchFlipped;
        if(isSwitchFlipped) {
            thread = new positiveAffirmationsThread(getActivity());
        }else {
            Toast.makeText(getActivity().getApplication(), "Positive Affirmations Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetPageToSave();

        Button button = getActivity().findViewById(R.id.positiveThoughtEnabler);
        button.setOnClickListener(e-> {
            try {
                buttonAction();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}